package safeme.uz.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import java.io.File

/**
 * Created by Damir on 2018-10-28.
 */
class FileDetail {
    var name: String? = null
    var size: Long? = null
    var type: String? = null
    var path: String = ""

    companion object {
        /**
         * Used toDate get file detail from uri.
         *
         *
         * 1. Used toDate get file detail (name & size) from uri.
         * 2. Getting file details from uri is different for different uri scheme,
         * 2.a. For "File Uri Scheme" - We will get file from uri & then get its details.
         * 2.b. For "Content Uri Scheme" - We will get the file details by querying content resolver.

         * @param uri Uri.
         * *
         * @return file detail.
         */

        fun getFileDetailFromUri(context: Context, uri: Uri?): FileDetail? {
            var fileDetail: FileDetail? = null
            if (uri != null) {
                fileDetail = FileDetail()
                // File Scheme.
                fileDetail.type = context.contentResolver.getType(uri)
                fileDetail.path = (fileDetail.getPathFromUri(context, uri) ?: uri.path) as String

                if (ContentResolver.SCHEME_FILE == uri.scheme) {
                    val file = File(uri.path)
                    fileDetail.name = file.name
                    fileDetail.size = file.length()

                } else if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
                    val returnCursor = context.contentResolver.query(uri, null, null, null, null)
                    if (returnCursor != null && returnCursor.moveToFirst()) {
                        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
                        fileDetail.name = returnCursor.getString(nameIndex)
                        fileDetail.size = returnCursor.getLong(sizeIndex)
                        returnCursor.close()
                    }
                }// Content Scheme.
            }
            return fileDetail
        }
    }

    fun getPath(context: Context, uri: Uri): String? {
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf("_data")
            val cursor: Cursor?

            try {
                cursor = context.contentResolver.query(uri, projection, null, null, null)
                val column_index = cursor!!.getColumnIndexOrThrow("_data")
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: Exception) {
                // Eat it
            }

        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }

        return ""
    }

    fun getPathFromUri(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return "${Environment.getExternalStorageDirectory().absolutePath}${Environment.DIRECTORY_DOCUMENTS}/${split[1]}"
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {
                    var id = DocumentsContract.getDocumentId(uri)
                    if (id.startsWith("raw:")) {
                        id = id.replaceFirst("raw:", "")
                        val file = File(id)
                        if (file.exists()) return id
                    }
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                    return getDataColumn(context, contentUri, null, null)

                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }// MediaProvider
                // DownloadsProvider
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.lastPathSegment

                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
        }// File
        // MediaStore (and general)

        return null
    }

    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = uri?.let {
                context.contentResolver.query(
                    it,
                    projection,
                    selection,
                    selectionArgs,
                    null
                )
            }
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return null
    }

    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content".equals(uri.authority)
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }
}
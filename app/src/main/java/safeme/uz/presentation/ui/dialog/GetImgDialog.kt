package safeme.uz.presentation.ui.dialog

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.BuildConfig
import safeme.uz.R
import safeme.uz.databinding.DialogGetImgBinding
import safeme.uz.utils.FileDetail
import safeme.uz.utils.Keys
import safeme.uz.utils.showToast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class GetImgDialog : DialogFragment(R.layout.dialog_get_img), View.OnClickListener {
    private val binding by viewBinding(DialogGetImgBinding::bind)
    private lateinit var imageURI: Uri
    private lateinit var currentImagePath: String
    private var totalSizeOfFiles = 0F
    private var urisOfSelectedFiles: ArrayList<Uri> = ArrayList()
    private var hasPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_Dialog_Custom)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        checkCameraPermission()
    }

    private fun checkCameraPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            checkForPermissionsForHigherThenAndroid11()
        } else {
            checkForPermissionsForLowerThanAndroid11()
        }
    }

    private fun checkForPermissionsForHigherThenAndroid11() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestForSpecificPermission.launch(Manifest.permission.CAMERA)
        } else {
            hasPermission = true
        }
    }

    private fun checkForPermissionsForLowerThanAndroid11() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestMultiplePermissions.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            } else {
                hasPermission = true
            }
        }
    }

    private val requestForSpecificPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
            var isGranted = permission
            if (isGranted) {
                hasPermission = true
            } else {
                hasPermission = false
                showDialog()
            }
        }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var isGranted = true
            permissions.entries.forEach {
                if (!it.value) isGranted = false
            }
            if (isGranted) {
                hasPermission = true
            } else {
                hasPermission = false
                showDialog()
            }
        }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setMessage(getString(R.string.permission_is_required))
            setTitle(getString(R.string.permission_required_title))
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton(getString(R.string.go_to_settings)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", activity?.packageName, null)
                intent.data = uri
                startActivity(intent)
                dismiss()
            }
        }.create().show()
    }

    private fun initViews() = with(binding) {
        btnOpenGallery.setOnClickListener(this@GetImgDialog)
        btnOpenCamera.setOnClickListener(this@GetImgDialog)
        btnClose.setOnClickListener(this@GetImgDialog)
    }

    override fun onClick(view: View?): Unit = with(binding) {
        when (view) {
            btnOpenGallery -> {
                if (hasPermission) {
                    getImgFromGallery()
                } else {
                    checkCameraPermission()
//                    customSnackbar(getString(R.string.permission_denied))
                }
            }
            btnOpenCamera -> {
                if (hasPermission) {
                    pickImageFromCamera()
                } else {
                    checkCameraPermission()
//                    customSnackbar(getString(R.string.permission_denied))
                }
            }
            btnClose -> {
                dismiss()
            }
        }
    }

    private fun getImgFromGallery() {
        val intent =
            Intent(Intent.ACTION_PICK).setType("image/*").setAction(Intent.ACTION_GET_CONTENT)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            resultLauncher.launch(Intent.createChooser(intent, getString(R.string.select_file)))
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                if (data != null) {
                    totalSizeOfFiles = 0F
                    val uri = data.data
                    if (uri != null) {
                        val sizeOfSelectedFile = getFileSizeInMB(uri)
                        if (sizeOfSelectedFile < Keys.MAX_FILE_SIZE) {
                            urisOfSelectedFiles.add(uri)
                        } else {
                            showToast(
                                getString(
                                    R.string.you_cannot_upload_large_file,
                                    Keys.MAX_FILE_SIZE.toString()
                                )
                            )
                        }
                    }

                    if (urisOfSelectedFiles.isNotEmpty()) {
                        val bundle = Bundle()
                        bundle.putParcelableArrayList(Keys.BUNDLE_KEY_LIST, urisOfSelectedFiles)
                        requireActivity().supportFragmentManager.setFragmentResult(
                            Keys.REQUEST_KEY_LIST, bundle
                        )
                    }
                    dismiss()
                }
            }
        }

    private fun getFileSizeInMB(uri: Uri): Long {
        var sizeOfSelectedFile = FileDetail.getFileDetailFromUri(requireContext(), uri)?.size ?: 0
        sizeOfSelectedFile /= (1024 * 1024)
        return sizeOfSelectedFile
    }

    private fun pickImageFromCamera() {
        val imageFile = createImageFile1()
        imageURI = FileProvider.getUriForFile(
            requireContext(), BuildConfig.APPLICATION_ID, imageFile
        )
        getTakeImageContent.launch(imageURI)
    }

    private var fileName = "image.jpg"

    @Throws(IOException::class)
    private fun createImageFile1(): File {
        val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val externalFilesDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_$format", ".jpg", externalFilesDir).apply {
            currentImagePath = absolutePath
            fileName = name
        }
    }

    private val getTakeImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                val openInputStream = activity?.contentResolver?.openInputStream(imageURI)
                val file = File(activity?.filesDir, fileName)
                val fileOutputStream = FileOutputStream(file)
                openInputStream?.copyTo(fileOutputStream)
                openInputStream?.close()
                fileOutputStream.close()

                val fileSizeInMB = getFileSizeInMB(file.toUri())

                if (fileSizeInMB <= Keys.MAX_FILE_SIZE) {
                    file.absolutePath.let {
                        requireActivity().supportFragmentManager.setFragmentResult(
                            Keys.REQUEST_KEY, bundleOf(Keys.BUNDLE_KEY to file.absolutePath)
                        )
                    }
                } else {
                    showToast(
                        getString(
                            R.string.you_cannot_upload_large_file, Keys.MAX_FILE_SIZE.toString()
                        )
                    )
                }
                dismiss()
            }
        }

    private fun customSnackbar(str:String){
        Snackbar.make(binding.btnOpenCamera,str,Snackbar.LENGTH_SHORT).show()
    }
}
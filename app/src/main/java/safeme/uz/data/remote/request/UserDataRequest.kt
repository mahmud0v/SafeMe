package safeme.uz.data.remote.request

import android.net.Uri
import okhttp3.MultipartBody

data class UserDataRequest(
    var first_name: String? = null,
    var last_name: String? = null,
    var birth_day: String? = null,
    var gender: String? = null,
    var region: Int? = null,
    var district: Int? = null,
    var mahalla: Int? = null,
    var adress: String? = null,
    var photo: MultipartBody.Part? = null,
    var photoUri: Uri? = null,
)
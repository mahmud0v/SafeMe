package safeme.uz.data.remote.response

import okhttp3.MultipartBody

data class UserUpdateResponse(
    val id: Int? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val birth_day: String? = null,
    val gender: String? = null,
    val region: Int? = null,
    val district: Int? = null,
    val mahalla: Int? = null,
    val adress: String? = null,
    val photo: MultipartBody.Part? = null
)
package safeme.uz.data.remote.request


data class UserUpdateRequest(
    val first_name: String? = null,
    val last_name: String? = null,
    val birth_day: String? = null,
    val gender: String? = null,
    val region: Int? = null,
    val district: Int? = null,
    val mahalla: Int? = null,


)
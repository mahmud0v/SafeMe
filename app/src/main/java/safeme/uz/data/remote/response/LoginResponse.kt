package safeme.uz.data.remote.response

data class LoginResponse(
    val token: String? = null,
    val refresh: String? = null,
    var hasPin: String = ""
)
package safeme.uz.data.remote.request

data class ResetPasswordRequest(
    val password1: String? = null,
    val password2: String? = null,
    var session_id : String? = ""
)
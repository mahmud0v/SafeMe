package safeme.uz.data.remote.request

data class RemindChangePasswordRequest(
    val password: String,
    val password1: String,
    val password2: String
)
package safeme.uz.data.remote.request

data class RegisterRequest(
    val phone: String? = null,
    val password1: String? = null,
    val password2: String? = null,
)
package safeme.uz.data.remote.response

data class RegisterResponse(
    val session_id : String? = null,
    val verification_code:String? = null
)
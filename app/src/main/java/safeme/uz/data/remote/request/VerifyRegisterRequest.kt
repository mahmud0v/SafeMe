package safeme.uz.data.remote.request

data class VerifyRegisterRequest(
    val verification_code: String? = null,
    var session_id: String? = null

)
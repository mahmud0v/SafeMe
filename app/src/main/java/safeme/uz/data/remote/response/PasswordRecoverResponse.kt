package safeme.uz.data.remote.response


data class PasswordRecoverResponse(
    var success: String? = null,
    var message: String? = null,
    var body: PasswordRecoverBody? = null,
    var code: Int
)

data class PasswordRecoverBody(
    var session_id: String? = null,
    var verification_code: String? = null
)
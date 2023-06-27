package safeme.uz.data.repository.auth

import retrofit2.Response
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.VerifyModel
import safeme.uz.data.remote.api.AuthApiService
import safeme.uz.data.remote.request.AddingChildDataRequest
import safeme.uz.data.remote.request.DistrictByIdRequest
import safeme.uz.data.remote.request.GetVerificationCodeRequest
import safeme.uz.data.remote.request.LoginRequest
import safeme.uz.data.remote.request.MfyByIdRequest
import safeme.uz.data.remote.request.PasswordRecoverRequest
import safeme.uz.data.remote.request.RegisterRequest
import safeme.uz.data.remote.request.RemindChangePasswordRequest
import safeme.uz.data.remote.request.ResetPasswordRequest
import safeme.uz.data.remote.request.UserDataRequest
import safeme.uz.data.remote.request.UserUpdateRequest
import safeme.uz.data.remote.request.VerifyRegisterRequest
import safeme.uz.data.remote.response.AddingChildDataResponse
import safeme.uz.data.remote.response.Address
import safeme.uz.data.remote.response.AddressResponse
import safeme.uz.data.remote.response.LoginResponse
import safeme.uz.data.remote.response.PasswordRecoverResponse
import safeme.uz.data.remote.response.PasswordUpdateBody
import safeme.uz.data.remote.response.RegisterResponse
import safeme.uz.data.remote.response.RemindPasswordChangeBody
import safeme.uz.data.remote.response.ResetPinCodeResponse
import safeme.uz.data.remote.response.UserDataResponse
import safeme.uz.data.remote.response.UserResponse
import safeme.uz.data.remote.response.UserUpdateResponse
import safeme.uz.data.remote.response.VerifyRegisterResponse
import safeme.uz.data.remote.response.VerifyResetPasswordResponse
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService, private val sharedPreference: AppSharedPreference
) : AuthRepository {

    override suspend fun login(loginRequest: LoginRequest): ApiResponse<LoginResponse> {
        val response = api.login("${sharedPreference.locale}/user/login/", loginRequest)

        response.body?.refresh?.let {
            sharedPreference.refresh = "Bearer $it"
        }
        response.body?.token?.let {
            sharedPreference.token = "Bearer $it"
        }
        return response
    }

    override suspend fun register(registerRequest: VerifyModel): ApiResponse<RegisterResponse> {
        val registerRequest1 = RegisterRequest(
            phone = registerRequest.phoneNumber,
            password1 = registerRequest.password,
            password2 = registerRequest.password,
        )
        val response = api.register("${sharedPreference.locale}/user/signup/", registerRequest1)
        sharedPreference.sessionId = response.body?.session_id.toString()
        return response
    }

    override suspend fun verifyRegister(verifyRegisterRequest: VerifyRegisterRequest): ApiResponse<VerifyRegisterResponse> {
        verifyRegisterRequest.session_id = sharedPreference.sessionId

        val response = api.verifyRegister(
            "${sharedPreference.locale}/user/verification/", verifyRegisterRequest
        )
        if (response.success) {
            response.body?.refresh?.let {
                sharedPreference.refresh = "Bearer $it"
            }
            response.body?.access?.let {
                sharedPreference.token = "Bearer $it"
            }
        }
        return response
    }

    override suspend fun getVerificationCodeForPassword(username: String?): ApiResponse<RegisterResponse> {
        val verificationForPassword = api.getVerificationForPassword(
            "${sharedPreference.locale}/user/password/recover",
            GetVerificationCodeRequest(phone = username)
        )
        sharedPreference.sessionId = verificationForPassword.body?.session_id.toString()
        return verificationForPassword
    }

    override suspend fun verifyCodeForPassword(verification_code: String): ApiResponse<VerifyResetPasswordResponse> {
        return api.verifyResetPassword(
            "${sharedPreference.locale}/user/password/verification", VerifyRegisterRequest(
                verification_code = verification_code, session_id = sharedPreference.sessionId
            )
        )
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): ApiResponse<String> {
        resetPasswordRequest.session_id = sharedPreference.sessionId
        return api.resetPassword(
            "${sharedPreference.locale}/user/password/update", resetPasswordRequest
        )
    }

    override suspend fun resetPinCode(): ApiResponse<ResetPinCodeResponse> {
        val resetPinCode = api.resetPinCode("${sharedPreference.locale}/user/pin/recover")
        sharedPreference.sessionId = resetPinCode.body?.session_id.toString()
        return resetPinCode
    }

    override suspend fun verifyPinCode(verificationCode: String): ApiResponse<Unit> {
        return api.verifyPinCode(
            "${sharedPreference.locale}/user/pin/verification", VerifyRegisterRequest(
                verification_code = verificationCode, session_id = sharedPreference.sessionId
            )
        )
    }

    override suspend fun getRegions(): ApiResponse<AddressResponse> {
        return api.getRegions("${sharedPreference.locale}/api/v1.0/regions")
    }

    override suspend fun getDistricts(): ApiResponse<AddressResponse> {
        return api.getDistricts("${sharedPreference.locale}/api/v1.0/districts")
    }

    override suspend fun getMFYs(): ApiResponse<AddressResponse> {
        return api.getMFYs("${sharedPreference.locale}/api/v1.0/mahalla")
    }

    override suspend fun getUserInfo(): Response<UserResponse> {
        return api.getUserInfo(
            "${sharedPreference.locale}/user"
        )
    }

    override suspend fun userUpdate(userUpdateRequest: UserUpdateRequest): Response<ApiResponse<UserUpdateResponse>> {
        return api.userUpdate(
            "${sharedPreference.locale}/user/update/",
            userUpdateRequest
        )
    }

    override suspend fun passwordRecover(passwordRecoverRequest: PasswordRecoverRequest): Response<PasswordRecoverResponse> {
        return api.passwordRecover(
            "${sharedPreference.locale}/user/password/recover",
            passwordRecoverRequest
        )
    }

    override suspend fun passwordVerification(verifyRegisterRequest: VerifyRegisterRequest): Response<PasswordRecoverResponse> {
        return api.passwordVerification(
            "${sharedPreference.locale}/user/password/verification",
            verifyRegisterRequest
        )
    }

    override suspend fun passwordUpdate(resetPasswordRequest: ResetPasswordRequest): Response<ApiResponse<PasswordUpdateBody>> {
          return api.passwordUpdate(
              "${sharedPreference.locale}/user/password/update",
              resetPasswordRequest
          )
    }

    override suspend fun remindPasswordChange(remindChangePasswordRequest: RemindChangePasswordRequest): Response<ApiResponse<RemindPasswordChangeBody>> {
        return api.remindedPasswordChange(
            "${sharedPreference.locale}/user/password/change",
            remindChangePasswordRequest
        )
    }

    override suspend fun getDistrictsById(regionId: Int): ApiResponse<List<Address>> {
        return api.getDistrictsById(
            "${sharedPreference.locale}/api/v1.0/district/by_region",
            DistrictByIdRequest(region = regionId)
        )
    }

    override suspend fun getMFYById(districtId: Int): ApiResponse<List<Address>> {
        return api.getMFYById(
            "${sharedPreference.locale}/api/v1.0/mahalla/by_district",
            MfyByIdRequest(district = districtId)
        )
    }

    override suspend fun addingChildData(addingChildDataRequest: AddingChildDataRequest): ApiResponse<AddingChildDataResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun sendUserData(userDataRequest: UserDataRequest): ApiResponse<UserDataResponse> {
        TODO("Not yet implemented")
    }


    override fun hasPinCode(): Boolean {
        return sharedPreference.pinCode != ""
    }

    override fun saveNewPin(pin: String): Boolean {
        if (sharedPreference.pinCode != pin) {
            sharedPreference.pinCode = pin
            return true
        }
        if (sharedPreference.pinCode == pin) return true

        return false
    }

    override fun getCurrentPin(): String {
        return sharedPreference.pinCode
    }

    override fun getToken(): Boolean {
        return sharedPreference.token == ""
    }
}
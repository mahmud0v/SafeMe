package safeme.uz.data.repository.auth

import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.data.model.Response
import safeme.uz.data.model.VerifyModel
import safeme.uz.data.remote.AuthApiService
import safeme.uz.data.remote.request.*
import safeme.uz.data.remote.response.*
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService, private val sharedPreference: AppSharedPreference
) : AuthRepository {

    override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        val response = api.login("${sharedPreference.locale}/user/login/", loginRequest)

        response.body?.refresh?.let {
            sharedPreference.refresh = "Bearer $it"
        }
        response.body?.token?.let {
            sharedPreference.token = "Bearer $it"
        }
        return response
    }

    override suspend fun register(registerRequest: VerifyModel): Response<RegisterResponse> {
        val registerRequest1 = RegisterRequest(
            phone = registerRequest.phoneNumber,
            password1 = registerRequest.password,
            password2 = registerRequest.password,
        )
        val response = api.register("${sharedPreference.locale}/user/signup/", registerRequest1)
        sharedPreference.sessionId = response.body?.session_id.toString()
        return response
    }

    override suspend fun verifyRegister(verifyRegisterRequest: VerifyRegisterRequest): Response<VerifyRegisterResponse> {
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

    override suspend fun getVerificationCodeForPassword(username: String?): Response<RegisterResponse> {
        val verificationForPassword = api.getVerificationForPassword(
            "${sharedPreference.locale}/user/password/recover",
            GetVerificationCodeRequest(phone = username)
        )
        sharedPreference.sessionId = verificationForPassword.body?.session_id.toString()
        return verificationForPassword
    }

    override suspend fun verifyCodeForPassword(verification_code: String): Response<VerifyResetPasswordResponse> {
        return api.verifyResetPassword(
            "${sharedPreference.locale}/user/password/verification", VerifyRegisterRequest(
                verification_code = verification_code, session_id = sharedPreference.sessionId
            )
        )
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Response<String> {
        resetPasswordRequest.session_id = sharedPreference.sessionId
        return api.resetPassword(
            "${sharedPreference.locale}/user/password/update", resetPasswordRequest
        )
    }

    override suspend fun resetPinCode(): Response<ResetPinCodeResponse> {
        val resetPinCode = api.resetPinCode("${sharedPreference.locale}/user/pin/recover")
        sharedPreference.sessionId = resetPinCode.body?.session_id.toString()
        return resetPinCode
    }

    override suspend fun verifyPinCode(verificationCode: String): Response<Unit> {
        return api.verifyPinCode(
            "${sharedPreference.locale}/user/pin/verification", VerifyRegisterRequest(
                verification_code = verificationCode, session_id = sharedPreference.sessionId
            )
        )
    }

    override suspend fun getRegions(): Response<AddressResponse> {
        return api.getRegions("${sharedPreference.locale}/api/v1.0/regions")
    }

    override suspend fun getDistricts(): Response<AddressResponse> {
        return api.getDistricts("${sharedPreference.locale}/api/v1.0/districts")
    }

    override suspend fun getMFYs(): Response<AddressResponse> {
        return api.getMFYs("${sharedPreference.locale}/api/v1.0/mahalla")
    }

    override suspend fun getDistrictsById(regionId: Int): Response<List<Address>> {
        return api.getDistrictsById(
            "${sharedPreference.locale}/api/v1.0/district/by_region",
            DistrictByIdRequest(region = regionId)
        )
    }

    override suspend fun getMFYById(districtId: Int): Response<List<Address>> {
        return api.getMFYById(
            "${sharedPreference.locale}/api/v1.0/mahalla/by_district",
            MfyByIdRequest(district = districtId)
        )
    }

    override suspend fun addingChildData(addingChildDataRequest: AddingChildDataRequest): Response<AddingChildDataResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun sendUserData(userDataRequest: UserDataRequest): Response<UserDataResponse> {
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
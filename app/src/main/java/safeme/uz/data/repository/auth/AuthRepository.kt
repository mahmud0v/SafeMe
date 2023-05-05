package safeme.uz.data.repository.auth

import safeme.uz.data.model.Response
import safeme.uz.data.model.VerifyModel
import safeme.uz.data.remote.request.*
import safeme.uz.data.remote.response.*

interface AuthRepository {
    suspend fun register(registerRequest: VerifyModel): Response<RegisterResponse>
    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse>
    suspend fun verifyRegister(verifyRegisterRequest: VerifyRegisterRequest): Response<VerifyRegisterResponse>
    fun hasPinCode(): Boolean
    fun saveNewPin(pin: String): Boolean
    fun getCurrentPin(): String
    fun getToken(): Boolean
    suspend fun getVerificationCodeForPassword(username: String?): Response<RegisterResponse>
    suspend fun sendUserData(userDataRequest: UserDataRequest): Response<UserDataResponse>
    suspend fun addingChildData(addingChildDataRequest: AddingChildDataRequest): Response<AddingChildDataResponse>
    suspend fun verifyCodeForPassword(verification_code: String): Response<VerifyResetPasswordResponse>
    suspend fun resetPinCode(): Response<ResetPinCodeResponse>
    suspend fun verifyPinCode(verificationCode: String): Response<Unit>
    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Response<String>
    suspend fun getRegions(): Response<AddressResponse>
    suspend fun getDistrictsById(regionId: Int): Response<List<Address>>
    suspend fun getMFYById(districtId: Int): Response<List<Address>>
    suspend fun getDistricts(): Response<AddressResponse>
    suspend fun getMFYs(): Response<AddressResponse>
}
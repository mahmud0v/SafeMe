package safeme.uz.data.repository.auth

import android.provider.ContactsContract.DisplayPhoto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.VerifyModel
import safeme.uz.data.remote.request.*
import safeme.uz.data.remote.response.*


interface AuthRepository {
    suspend fun register(registerRequest: VerifyModel): Response<ApiResponse<RegisterResponse>>
    suspend fun login(loginRequest: LoginRequest): Response<ApiResponse<LoginResponse>>
    suspend fun verifyRegister(verifyRegisterRequest: VerifyRegisterRequest): Response<ApiResponse<VerifyRegisterResponse>>
    fun hasPinCode(): Boolean
    fun saveNewPin(pin: String): Boolean
    fun getCurrentPin(): String
    fun getToken(): Boolean
    suspend fun getVerificationCodeForPassword(username: String?): Response<ApiResponse<RegisterResponse>>
    suspend fun sendUserData(userDataRequest: UserDataRequest):ApiResponse<UserDataResponse>
    suspend fun addingChildData(addingChildDataRequest: AddingChildDataRequest): Response<ApiResponse<AddingChildDataResponse>>
    suspend fun verifyCodeForPassword(verification_code: String): ApiResponse<VerifyResetPasswordResponse>
    suspend fun resetPinCode(): ApiResponse<ResetPinCodeResponse>
    suspend fun verifyPinCode(verificationCode: String): ApiResponse<Unit>
    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): ApiResponse<PasswordUpdateBody>
    suspend fun getRegions(): Response<ApiResponse<ArrayList<Address>>>
    suspend fun getDistrictsById(regionId: Int): ApiResponse<List<Address>>
    suspend fun getMFYById(districtId: Int): ApiResponse<List<Address>>
    suspend fun getDistricts(): ApiResponse<AddressResponse>
    suspend fun getMFYs(): ApiResponse<AddressResponse>
    suspend fun getUserInfo(): Response<UserResponse>
    suspend fun userUpdate(requestBody: MultipartBody):Response<ApiResponse<UserUpdateResponse>>
    suspend fun passwordRecover(passwordRecoverRequest: PasswordRecoverRequest):Response<PasswordRecoverResponse>
    suspend fun passwordVerification(verifyRegisterRequest: VerifyRegisterRequest):Response<PasswordRecoverResponse>
    suspend fun passwordUpdate(resetPasswordRequest: ResetPasswordRequest):Response<ApiResponse<PasswordUpdateBody>>
    suspend fun remindPasswordChange(remindChangePasswordRequest: RemindChangePasswordRequest):Response<ApiResponse<ArrayList<String>>>
    suspend fun accessTokenUpdate():Response<ApiResponse<LoginResponse>>
}
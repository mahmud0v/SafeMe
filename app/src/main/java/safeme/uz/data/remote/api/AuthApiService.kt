package safeme.uz.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.DistrictByIdRequest
import safeme.uz.data.remote.request.GetVerificationCodeRequest
import safeme.uz.data.remote.request.LoginRequest
import safeme.uz.data.remote.request.MfyByIdRequest
import safeme.uz.data.remote.request.PasswordRecoverRequest
import safeme.uz.data.remote.request.RefreshTokenRequest
import safeme.uz.data.remote.request.RegisterRequest
import safeme.uz.data.remote.request.RemindChangePasswordRequest
import safeme.uz.data.remote.request.ResetPasswordRequest
import safeme.uz.data.remote.request.UserUpdateRequest
import safeme.uz.data.remote.request.VerifyRegisterRequest
import safeme.uz.data.remote.response.Address
import safeme.uz.data.remote.response.AddressResponse
import safeme.uz.data.remote.response.LoginResponse
import safeme.uz.data.remote.response.PasswordRecoverResponse
import safeme.uz.data.remote.response.PasswordUpdateBody
import safeme.uz.data.remote.response.RegisterResponse
import safeme.uz.data.remote.response.RemindPasswordChangeBody
import safeme.uz.data.remote.response.ResetPinCodeResponse
import safeme.uz.data.remote.response.UserResponse
import safeme.uz.data.remote.response.UserUpdateResponse
import safeme.uz.data.remote.response.VerifyRegisterResponse
import safeme.uz.data.remote.response.VerifyResetPasswordResponse

interface AuthApiService {
    @POST
    suspend fun register(
        @Url url: String,
        @Body registerRequest: RegisterRequest
    ): ApiResponse<RegisterResponse>

    @POST
    suspend fun verifyRegister(
        @Url url: String,
        @Body verifyRegisterRequest: VerifyRegisterRequest
    ): ApiResponse<VerifyRegisterResponse>

    @POST
    suspend fun login(
        @Url url: String,
        @Body loginRequest: LoginRequest
    ): ApiResponse<LoginResponse>

    @POST
    suspend fun refreshToken(
        @Url url: String,
        @Body newTokenRequest: RefreshTokenRequest
    ): ApiResponse<LoginResponse>

    @POST
    suspend fun getVerificationForPassword(
        @Url url: String,
        @Body getVerificationCodeRequest: GetVerificationCodeRequest
    ): ApiResponse<RegisterResponse>

    @POST
    suspend fun verifyResetPassword(
        @Url url: String,
        @Body verifyResetPassword: VerifyRegisterRequest
    ): ApiResponse<VerifyResetPasswordResponse>

    @GET
    suspend fun resetPinCode(@Url url: String): ApiResponse<ResetPinCodeResponse>

    @POST
    suspend fun verifyPinCode(
        @Url url: String,
        @Body verifyRegisterRequest: VerifyRegisterRequest
    ): ApiResponse<Unit>

    @POST
    suspend fun resetPassword(
        @Url url: String,
        @Body resetPasswordRequest: ResetPasswordRequest
    ): ApiResponse<PasswordUpdateBody>

    @GET
    suspend fun getRegions(@Url url: String): ApiResponse<AddressResponse>

    @GET
    suspend fun getDistricts(@Url url: String): ApiResponse<AddressResponse>

    @GET
    suspend fun getMFYs(@Url url: String): ApiResponse<AddressResponse>

    @POST
    suspend fun getDistrictsById(
        @Url url: String,
        @Body districtByIdRequest: DistrictByIdRequest
    ): ApiResponse<List<Address>>

    @POST
    suspend fun getMFYById(
        @Url url: String,
        @Body mfyByIdRequest: MfyByIdRequest
    ): ApiResponse<List<Address>>

    @GET
    suspend fun getUserInfo(
        @Url url: String
    ): Response<UserResponse>


    @PUT
    suspend fun userUpdate(
        @Url url: String,
        @Body userUpdateRequest: UserUpdateRequest
    ): Response<ApiResponse<UserUpdateResponse>>

    @POST
    suspend fun passwordRecover(
        @Url url: String,
        @Body passwordRecoverRequest: PasswordRecoverRequest
    ): Response<PasswordRecoverResponse>

    @POST
    suspend fun passwordVerification(
        @Url url: String,
        @Body verifyRegisterRequest: VerifyRegisterRequest
    ): Response<PasswordRecoverResponse>

    @POST
    suspend fun passwordUpdate(
        @Url url: String,
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Response<ApiResponse<PasswordUpdateBody>>

    @POST
    suspend fun remindedPasswordChange(
        @Url url: String,
        @Body remindChangePasswordRequest: RemindChangePasswordRequest
    ): Response<ApiResponse<RemindPasswordChangeBody>>


}
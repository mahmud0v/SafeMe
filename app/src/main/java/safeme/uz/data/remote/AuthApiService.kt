package safeme.uz.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import safeme.uz.data.model.Response
import safeme.uz.data.remote.request.*
import safeme.uz.data.remote.response.*

interface AuthApiService {
    @POST
    suspend fun register(
        @Url url: String,
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @POST
    suspend fun verifyRegister(
        @Url url: String,
        @Body verifyRegisterRequest: VerifyRegisterRequest
    ): Response<VerifyRegisterResponse>

    @POST
    suspend fun login(
        @Url url: String,
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST
    suspend fun refreshToken(
        @Url url: String,
        @Body newTokenRequest: RefreshTokenRequest
    ): Response<LoginResponse>

    @POST
    suspend fun getVerificationForPassword(
        @Url url: String,
        @Body getVerificationCodeRequest: GetVerificationCodeRequest
    ): Response<RegisterResponse>

    @POST
    suspend fun verifyResetPassword(
        @Url url: String,
        @Body verifyResetPassword: VerifyRegisterRequest
    ): Response<VerifyResetPasswordResponse>

    @GET
    suspend fun resetPinCode(@Url url: String): Response<ResetPinCodeResponse>

    @POST
    suspend fun verifyPinCode(
        @Url url: String,
        @Body verifyRegisterRequest: VerifyRegisterRequest
    ): Response<Unit>

    @POST
    suspend fun resetPassword(
        @Url url: String,
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Response<String>

    @GET
    suspend fun getRegions(@Url url: String): Response<AddressResponse>

    @GET
    suspend fun getDistricts(@Url url: String): Response<AddressResponse>

    @GET
    suspend fun getMFYs(@Url url: String): Response<AddressResponse>

    @POST
    suspend fun getDistrictsById(
        @Url url: String,
        @Body districtByIdRequest: DistrictByIdRequest
    ): Response<List<Address>>

    @POST
    suspend fun getMFYById(
        @Url url: String,
        @Body mfyByIdRequest: MfyByIdRequest
    ): Response<List<Address>>
}
package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.PasswordRecoverRequest
import safeme.uz.data.remote.request.RemindChangePasswordRequest
import safeme.uz.data.remote.request.ResetPasswordRequest
import safeme.uz.data.remote.request.VerifyRegisterRequest
import safeme.uz.data.remote.response.PasswordRecoverResponse
import safeme.uz.data.remote.response.PasswordUpdateBody
import safeme.uz.data.remote.response.RegisterResponse
import safeme.uz.data.remote.response.RemindPasswordChangeBody
import safeme.uz.data.remote.response.ResetPinCodeResponse
import safeme.uz.data.remote.response.UserResponse
import safeme.uz.utils.AnnouncementResult

interface ProfileUseCase {
    fun getUserInfo():Flow<AnnouncementResult<UserResponse>>

    fun passwordRecover(passwordRecoverRequest: PasswordRecoverRequest):Flow<AnnouncementResult<PasswordRecoverResponse>>

    fun passwordVerification(verifyRegisterRequest: VerifyRegisterRequest):Flow<AnnouncementResult<PasswordRecoverResponse>>

    fun passwordUpdate(resetPasswordRequest: ResetPasswordRequest):Flow<AnnouncementResult<ApiResponse<PasswordUpdateBody>>>

    fun remindPasswordChange(remindChangePasswordRequest: RemindChangePasswordRequest):Flow<AnnouncementResult<ApiResponse<RemindPasswordChangeBody>>>


}
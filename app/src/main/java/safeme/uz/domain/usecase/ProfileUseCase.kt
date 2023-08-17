package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.PasswordRecoverRequest
import safeme.uz.data.remote.request.RemindChangePasswordRequest
import safeme.uz.data.remote.request.ResetPasswordRequest
import safeme.uz.data.remote.request.VerifyRegisterRequest
import safeme.uz.data.remote.response.PasswordRecoverResponse
import safeme.uz.data.remote.response.PasswordUpdateBody
import safeme.uz.data.remote.response.RemindPasswordChangeBody
import safeme.uz.data.remote.response.UserResponse
import safeme.uz.utils.RemoteApiResult

interface ProfileUseCase {
    fun getUserInfo():Flow<RemoteApiResult<UserResponse>>

    fun passwordRecover(passwordRecoverRequest: PasswordRecoverRequest):Flow<RemoteApiResult<PasswordRecoverResponse>>

    fun passwordVerification(verifyRegisterRequest: VerifyRegisterRequest):Flow<RemoteApiResult<PasswordRecoverResponse>>

    fun passwordUpdate(resetPasswordRequest: ResetPasswordRequest):Flow<RemoteApiResult<ApiResponse<PasswordUpdateBody>>>

    fun remindPasswordChange(remindChangePasswordRequest: RemindChangePasswordRequest):Flow<RemoteApiResult<ApiResponse<ArrayList<String>>>>


}
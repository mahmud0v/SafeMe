package safeme.uz.domain.usecase.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.PasswordRecoverRequest
import safeme.uz.data.remote.request.RemindChangePasswordRequest
import safeme.uz.data.remote.request.ResetPasswordRequest
import safeme.uz.data.remote.request.VerifyRegisterRequest
import safeme.uz.data.remote.response.PasswordRecoverResponse
import safeme.uz.data.remote.response.PasswordUpdateBody
import safeme.uz.data.remote.response.RemindPasswordChangeBody
import safeme.uz.data.remote.response.UserResponse
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.ProfileUseCase
import safeme.uz.utils.AnnouncementResult
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : ProfileUseCase {

    override fun getUserInfo(): Flow<AnnouncementResult<UserResponse>> {
        return flow {
            emit(AnnouncementResult.Loading())
            val response = authRepository.getUserInfo()
            val code = response.body()?.code
            if (code == 200) {
                emit(AnnouncementResult.Success(response.body()!!))
            } else {
                emit(AnnouncementResult.Error(response.body()?.message!!))
            }
        }
    }

    override fun passwordRecover(passwordRecoverRequest: PasswordRecoverRequest): Flow<AnnouncementResult<PasswordRecoverResponse>> {
        return flow {
            val response = authRepository.passwordRecover(passwordRecoverRequest)
            if (response.body()?.code == 200) {
                emit(AnnouncementResult.Success(response.body()!!))
            } else {
                emit(AnnouncementResult.Error(response.body()?.message!!))
            }
        }
    }

    override fun passwordVerification(verifyRegisterRequest: VerifyRegisterRequest): Flow<AnnouncementResult<PasswordRecoverResponse>> {
        return flow {
            val response = authRepository.passwordVerification(verifyRegisterRequest)
            if (response.body()?.code == 200) {
                emit(AnnouncementResult.Success(response.body()!!))
            } else {
                emit(AnnouncementResult.Error(response.message()))
            }
        }
    }

    override fun passwordUpdate(resetPasswordRequest: ResetPasswordRequest): Flow<AnnouncementResult<ApiResponse<PasswordUpdateBody>>> {
        return flow {
            emit(AnnouncementResult.Loading())
            val response = authRepository.passwordUpdate(resetPasswordRequest)
            if (response.body()?.code == 200) {
                emit(AnnouncementResult.Success(response.body()!!))
            } else {
                emit(AnnouncementResult.Error(response.message()))
            }
        }
    }

    override fun remindPasswordChange(remindChangePasswordRequest: RemindChangePasswordRequest): Flow<AnnouncementResult<ApiResponse<RemindPasswordChangeBody>>> {
        return flow {
            emit(AnnouncementResult.Loading())
            val response = authRepository.remindPasswordChange(remindChangePasswordRequest)
            if (response.body()?.code==200){
                emit(AnnouncementResult.Success(response.body()!!))
            }else {
                emit(AnnouncementResult.Error(response.body()?.message!!))
            }
        }
    }


}
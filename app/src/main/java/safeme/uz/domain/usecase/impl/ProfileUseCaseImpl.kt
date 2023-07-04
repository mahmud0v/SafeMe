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
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : ProfileUseCase {

    override fun getUserInfo(): Flow<RemoteApiResult<UserResponse>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = authRepository.getUserInfo()
            val code = response.body()?.code
            if (code == 200) {
                emit(RemoteApiResult.Success(response.body()!!))
            } else {
                emit(RemoteApiResult.Error(response.body()?.message!!))
            }
        }
    }

    override fun passwordRecover(passwordRecoverRequest: PasswordRecoverRequest): Flow<RemoteApiResult<PasswordRecoverResponse>> {
        return flow {
            val response = authRepository.passwordRecover(passwordRecoverRequest)
            if (response.body()?.code == 200) {
                emit(RemoteApiResult.Success(response.body()!!))
            } else {
                emit(RemoteApiResult.Error(response.body()?.message!!))
            }
        }
    }

    override fun passwordVerification(verifyRegisterRequest: VerifyRegisterRequest): Flow<RemoteApiResult<PasswordRecoverResponse>> {
        return flow {
            val response = authRepository.passwordVerification(verifyRegisterRequest)
            if (response.body()?.code == 200) {
                emit(RemoteApiResult.Success(response.body()!!))
            } else {
                emit(RemoteApiResult.Error(response.message()))
            }
        }
    }

    override fun passwordUpdate(resetPasswordRequest: ResetPasswordRequest): Flow<RemoteApiResult<ApiResponse<PasswordUpdateBody>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = authRepository.passwordUpdate(resetPasswordRequest)
            if (response.body()?.code == 200) {
                emit(RemoteApiResult.Success(response.body()!!))
            } else {
                emit(RemoteApiResult.Error(response.message()))
            }
        }
    }

    override fun remindPasswordChange(remindChangePasswordRequest: RemindChangePasswordRequest): Flow<RemoteApiResult<ApiResponse<RemindPasswordChangeBody>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = authRepository.remindPasswordChange(remindChangePasswordRequest)
            if (response.body()?.code==200){
                emit(RemoteApiResult.Success(response.body()!!))
            }else {
                emit(RemoteApiResult.Error(response.body()?.message!!))
            }
        }
    }


}
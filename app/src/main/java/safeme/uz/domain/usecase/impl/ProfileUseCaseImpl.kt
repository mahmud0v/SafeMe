package safeme.uz.domain.usecase.impl

import android.app.Application
import android.content.res.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import safeme.uz.R
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
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val application: Application
) : ProfileUseCase {

    override fun getUserInfo(): Flow<RemoteApiResult<UserResponse>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = authRepository.getUserInfo()
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }

    override fun passwordRecover(passwordRecoverRequest: PasswordRecoverRequest): Flow<RemoteApiResult<PasswordRecoverResponse>> {
        return flow {
            val response = authRepository.passwordRecover(passwordRecoverRequest)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }

    override fun passwordVerification(verifyRegisterRequest: VerifyRegisterRequest): Flow<RemoteApiResult<PasswordRecoverResponse>> {
        return flow {
            val response = authRepository.passwordVerification(verifyRegisterRequest)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }

    override fun passwordUpdate(resetPasswordRequest: ResetPasswordRequest): Flow<RemoteApiResult<ApiResponse<PasswordUpdateBody>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = authRepository.passwordUpdate(resetPasswordRequest)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }

    override fun remindPasswordChange(remindChangePasswordRequest: RemindChangePasswordRequest): Flow<RemoteApiResult<ApiResponse<RemindPasswordChangeBody>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = authRepository.remindPasswordChange(remindChangePasswordRequest)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }


}
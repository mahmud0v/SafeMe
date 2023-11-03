package safeme.uz.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import safeme.uz.R
import safeme.uz.data.model.MessageData
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.request.ResetPasswordRequest
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.ResetPasswordUseCase
import safeme.uz.utils.isConnected
import javax.inject.Inject

class ResetPasswordUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : ResetPasswordUseCase {
    override fun resetPassword(resetPasswordRequest: ResetPasswordRequest) =
        flow {
            if (isConnected()) {
                val response = authRepository.resetPassword(resetPasswordRequest)
                if (response.success) {
                    emit(ResultData.Success(true))
                } else {
                    when (response.code) {
                        400,408 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.expired_password)))
                        404 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.user_not_found)))
                        in 500..599 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.internal_server_error)))
                        else -> emit(ResultData.Fail(message = MessageData.Resource(R.string.some_error_occurred)))
                    }
                }
            } else {
                emit(ResultData.Fail(message = MessageData.Resource(R.string.internet_not_connected)))
            }
        }.catch {
            emit(
                ResultData.Fail(
                    message = MessageData.Resource(
                        R.string.internal_server_error
                    )
                )
            )
        }.flowOn(Dispatchers.IO)
}

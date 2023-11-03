package safeme.uz.domain.usecase.impl

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import safeme.uz.R
import safeme.uz.data.model.MessageData
import safeme.uz.data.model.ResultData
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.ResetPinCodeUseCase
import safeme.uz.utils.isConnected
import javax.inject.Inject

class ResetPinCodeUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : ResetPinCodeUseCase {
    override fun resetPinCode() = flow {
        if (isConnected()) {
            val response = authRepository.resetPinCode()
            if (response.success) {
                response.body?.let {
                    emit(ResultData.Success(it))
                }
            } else {
                when (response.code) {
                    400 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.bad_request)))
                    701 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.not_confirmed)))
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
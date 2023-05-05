package safeme.uz.domain.usecase.impl

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import safeme.uz.R
import safeme.uz.data.model.MessageData
import safeme.uz.data.model.ResultData
import safeme.uz.data.repository.app.AppRepository
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.VerifyPinCodeUseCase
import safeme.uz.utils.isConnected
import javax.inject.Inject

class VerifyPinCodeCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : VerifyPinCodeUseCase {
    override fun invoke(verification_code: String): Flow<ResultData<Unit>> =
        flow<ResultData<Unit>> {
            if (isConnected()) {
                val response = authRepository.verifyPinCode(verification_code)
                if (response.success) {
                    emit(ResultData.Success(Unit))
                } else {
                    when (response.code) {
                        400 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.bad_request)))
                        404 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.user_not_found)))
                        409 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.not_confirmed)))
                        408 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.expired_verification_code)))
                        in 500..599 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.internal_server_error)))
                        else -> emit(ResultData.Fail(message = MessageData.Resource(R.string.some_error_occurred)))
                    }
                }
            } else {
                emit(ResultData.Fail(message = MessageData.Resource(R.string.internet_not_connected)))
            }
        }.catch {
            if (it is HttpException) {
                if (it.code() == 400) emit(ResultData.Fail(message = MessageData.Resource(R.string.bad_request)))
                else if (it.code() in 500..599) emit(
                    ResultData.Fail(
                        message = MessageData.Resource(
                            R.string.internal_server_error
                        )
                    )
                )
            } else emit(ResultData.Fail(message = MessageData.Resource(R.string.not_confirmed)))
        }.flowOn(Dispatchers.IO)
}
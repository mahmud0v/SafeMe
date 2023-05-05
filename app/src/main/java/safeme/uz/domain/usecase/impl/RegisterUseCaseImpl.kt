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
import safeme.uz.data.model.VerifyModel
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.RegisterUseCase
import safeme.uz.utils.isConnected
import javax.inject.Inject

class RegisterUseCaseImpl @Inject constructor(
    private val repository: AuthRepository,
) : RegisterUseCase {
    override fun invoke(registerRequest: VerifyModel) = flow {
        if (isConnected()) {
            val response = repository.register(registerRequest)
            Log.e("TAG", "response: ${response}", )
            if (response.success) {
                response.body?.let {
                    emit(ResultData.Success(registerRequest))
                }
            } else {
                when (response.code) {
                    409 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.already_exist)))
                    in 500..599 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.internal_server_error)))
                    else -> emit(ResultData.Fail(message = MessageData.Resource(R.string.some_error_occurred)))
                }
            }
        } else {
            emit(ResultData.Fail(message = MessageData.Resource(R.string.internet_not_connected)))
        }
    }.catch {
        Log.e("TAG", "RegisterUseCase: ${it.message}", )
        if (it is HttpException) {
            if (it.code() == 400) emit(ResultData.Fail(message = MessageData.Resource(R.string.bad_request)))
            else if (it.code() in 500..599) emit(ResultData.Fail(message = MessageData.Resource(R.string.internal_server_error)))
        } else emit(ResultData.Fail(message = MessageData.Resource(R.string.some_error_occurred)))
    }.flowOn(Dispatchers.IO)

}
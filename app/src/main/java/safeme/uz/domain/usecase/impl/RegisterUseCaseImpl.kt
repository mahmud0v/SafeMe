package safeme.uz.domain.usecase.impl

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import safeme.uz.R
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.data.model.MessageData
import safeme.uz.data.model.ResultData
import safeme.uz.data.model.VerifyModel
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.RegisterUseCase
import safeme.uz.utils.isConnected
import javax.inject.Inject

class RegisterUseCaseImpl @Inject constructor(
    private val repository: AuthRepository,
    private val appSharedPreference: AppSharedPreference
) : RegisterUseCase {
    override fun invoke(registerRequest: VerifyModel) = flow {
        if (isConnected()) {
            val response = repository.register(registerRequest)
            when(response.code()){
                in 200..209 -> {
                    emit(ResultData.Success(registerRequest))
                    appSharedPreference.sessionId = response.body()?.body?.session_id.toString()
                }
                409 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.already_exist)))
                in 500..599 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.internal_server_error)))
                else -> emit(ResultData.Fail(message = MessageData.Resource(R.string.some_error_occurred)))
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
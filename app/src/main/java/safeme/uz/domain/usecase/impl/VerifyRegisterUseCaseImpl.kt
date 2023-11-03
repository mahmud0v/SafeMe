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
import safeme.uz.data.remote.request.VerifyRegisterRequest
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.VerifyRegisterUseCase
import safeme.uz.utils.isConnected
import javax.inject.Inject

class VerifyRegisterUseCaseImpl @Inject constructor(
    private val repository: AuthRepository,
    private val appSharedPreference: AppSharedPreference
) : VerifyRegisterUseCase {
    override fun invoke(verifyRegisterRequest: VerifyRegisterRequest) = flow {
        if (isConnected()) {
            val response = repository.verifyRegister(verifyRegisterRequest)
            when(response.code()){
                in 200..209->{
                    response.body()?.let {
                        emit(ResultData.Success(it))
                    }

                    response.body()?.body?.refresh?.let {
                        appSharedPreference.refresh = it
                    }

                    response.body()?.body?.access?.let {
                        appSharedPreference.token = "Bearer $it"
                    }
                }

                400, 409 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.not_confirmed)))
                404 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.user_not_found)))
                703 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.sms_not_sent)))
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
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
import safeme.uz.data.remote.request.LoginRequest
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.LoginUseCase
import safeme.uz.utils.isConnected
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val repository: AuthRepository,
    private val sharedPreference: AppSharedPreference,
) : LoginUseCase {
    override fun invoke(loginRequest: LoginRequest) = flow {
        if (isConnected()) {
            val response = repository.login(loginRequest)
            when(response.code()){
                in 200..209 ->{
                    response.body()?.body?.let {
                        it.hasPin = sharedPreference.pinCode
                        emit(ResultData.Success(it))
                        it.refresh?.let {refresh->
                            sharedPreference.refresh = refresh
                        }
                        it.token?.let {token->
                            sharedPreference.token = "Bearer $token"
                        }
                    }
                }
                400 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.bad_request)))
                404 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.user_not_found)))
                409 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.wrong_phone_password)))
                in 500..599 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.internal_server_error)))
                else -> emit(ResultData.Fail(message = MessageData.Resource(R.string.some_error_occurred)))
            }
        } else {
            emit(ResultData.Fail(message = MessageData.Resource(R.string.internet_not_connected)))
        }
    }.catch {
        emit(ResultData.Fail(message = MessageData.Resource(R.string.internal_server_error)))
    }.flowOn(Dispatchers.IO)

}
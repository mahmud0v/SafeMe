package safeme.uz.domain.usecase.impl

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import safeme.uz.R
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.MessageData
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.response.RegisterResponse
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.ForgetPasswordUseCase
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.isConnected
import javax.inject.Inject

class ForgetPasswordUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val appSharedPreference: AppSharedPreference,
    private val application: Application
) : ForgetPasswordUseCase {
    override fun getVerificationCode(username: String?) =
        flow {
            if (isConnected()) {
                val response = authRepository.getVerificationCodeForPassword(username)
                when (response.code()) {
                    in 200..209 -> {
                        emit(ResultData.Success(true))
                        appSharedPreference.sessionId = response.body()?.body?.session_id.toString()
                    }

                    400 -> {
                        emit(ResultData.Fail(message = MessageData.Resource(R.string.bad_request)))
                    }

                    404 -> {
                        emit(ResultData.Fail(message = MessageData.Resource(R.string.user_not_found)))
                    }

                    in 500..599 -> {
                        emit(ResultData.Fail(message = MessageData.Resource(R.string.internal_server_error)))
                    }

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

    override fun sendSms(phone: String): Flow<RemoteApiResult<ApiResponse<RegisterResponse>>> {
        return flow {
            val response = authRepository.getVerificationCodeForPassword(phone)
            when (response.code()) {
                in 200..209 -> {
                    emit(RemoteApiResult.Success(response.body()))
                    appSharedPreference.sessionId = response.body()?.body?.session_id.toString()
                }

                400 -> emit(RemoteApiResult.Error(application.getString(R.string.bad_request)))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.user_not_found)))
                in 500..599 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }.catch {
            emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
        }.flowOn(Dispatchers.IO)
    }


}
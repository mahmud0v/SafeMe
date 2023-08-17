package safeme.uz.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import safeme.uz.R
import safeme.uz.data.model.MessageData
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.request.UserDataRequest
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.SendUserDataUseCase
import safeme.uz.utils.isConnected
import javax.inject.Inject

class SendUserDataUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : SendUserDataUseCase {
    override fun invoke(userDataRequest: UserDataRequest) = flow {
        if (isConnected()) {
            val response = authRepository.sendUserData(userDataRequest)
            if (response.success) {
                response.body?.let {
                    emit(ResultData.Success(it))
                }
            } else {
                when (response.code) {
                    400 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.bad_request)))
                    in 500..599 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.internal_server_error)))
                    else -> emit(ResultData.Fail(message = MessageData.Resource(R.string.some_error_occurred)))
                }
            }
        } else {
            emit(ResultData.Fail(message = MessageData.Resource(R.string.internet_not_connected)))
        }
//    }.catch {
//        if (it is HttpException) {
//            if (it.code() == 400) emit(ResultData.Fail(message = MessageData.Resource(R.string.bad_request)))
//            else if (it.code() in 500..599) emit(ResultData.Fail(message = MessageData.Resource(R.string.internal_server_error)))
//        } else emit(ResultData.Fail(message = MessageData.Resource(R.string.some_error_occurred)))
//    }.flowOn(Dispatchers.IO)
    }}
package safeme.uz.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import safeme.uz.R
import safeme.uz.data.model.MessageData
import safeme.uz.data.model.ResultData
import safeme.uz.data.repository.app.AppRepository
import safeme.uz.domain.LogOutUseCase
import safeme.uz.utils.isConnected
import javax.inject.Inject

class LogOutUseCaseImpl @Inject constructor(
    private val appRepository: AppRepository
) : LogOutUseCase {
    override fun invoke() = flow<ResultData<Boolean>> {
        if (isConnected()) {
            val response = appRepository.logOut()
            if (response.success) {
                response.body?.let {
                    appRepository.saveTokenToPreference()
                    emit(ResultData.Success(it))
                }
            } else {
                when (response.code) {
                    404 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.user_not_found)))
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
            else if (it.code() in 500..599) emit(ResultData.Fail(message = MessageData.Resource(R.string.internal_server_error)))
        } else emit(ResultData.Fail(message = MessageData.Resource(R.string.some_error_occurred)))
    }.flowOn(Dispatchers.IO)

}
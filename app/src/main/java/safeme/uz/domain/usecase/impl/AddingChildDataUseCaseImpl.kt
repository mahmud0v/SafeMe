package safeme.uz.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import safeme.uz.R
import safeme.uz.data.model.MessageData
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.request.AddingChildDataRequest
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.AddingChildDataUseCase
import safeme.uz.utils.isConnected
import javax.inject.Inject

class AddingChildDataUseCaseImpl @Inject constructor(
    private val repository: AuthRepository
) : AddingChildDataUseCase {
    override fun invoke(addingChildDataRequest: AddingChildDataRequest) = flow {
        if (isConnected()) {
            val response = repository.addingChildData(addingChildDataRequest)
            when (response.code()) {
                in 200..209 -> {
                    emit(ResultData.Success(response.body()?.body))
                }
                400 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.bad_request)))
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
package safeme.uz.domain.usecase.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.SosBody
import safeme.uz.data.remote.request.SosRequest
import safeme.uz.data.repository.appeal.AppealRepository
import safeme.uz.domain.usecase.SosNotifiedUseCase
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

class SosNotifiedUseCaseImpl @Inject constructor(
    private val appealRepository: AppealRepository
) : SosNotifiedUseCase {

    override fun sosNotified(sosRequest: SosRequest): Flow<RemoteApiResult<ApiResponse<SosBody>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = appealRepository.sosNotified(sosRequest)
            if (response.body()?.code in 200..209){
                emit(RemoteApiResult.Success(response.body()!!))
            }else {
                emit(RemoteApiResult.Error(response.body()?.message!!))
            }
        }
    }

}
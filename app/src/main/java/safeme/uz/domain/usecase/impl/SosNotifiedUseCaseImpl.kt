package safeme.uz.domain.usecase.impl

import android.app.Application
import android.content.res.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import safeme.uz.R
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.SosBody
import safeme.uz.data.remote.request.SosRequest
import safeme.uz.data.repository.appeal.AppealRepository
import safeme.uz.domain.usecase.SosNotifiedUseCase
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

class SosNotifiedUseCaseImpl @Inject constructor(
    private val appealRepository: AppealRepository,
    private val application: Application
) : SosNotifiedUseCase {

    override fun sosNotified(sosRequest: SosRequest): Flow<RemoteApiResult<ApiResponse<SosBody>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = appealRepository.sosNotified(sosRequest)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }

}
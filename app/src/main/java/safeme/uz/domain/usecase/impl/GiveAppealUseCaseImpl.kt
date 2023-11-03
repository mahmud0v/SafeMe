package safeme.uz.domain.usecase.impl

import android.app.Application
import android.content.res.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import safeme.uz.R
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.AppealRequest
import safeme.uz.data.remote.response.AppealResponse
import safeme.uz.data.repository.appeal.AppealRepository
import safeme.uz.domain.usecase.GiveAppealUseCase
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

class GiveAppealUseCaseImpl @Inject constructor(
    private val appealRepository: AppealRepository,
    private val application: Application
) : GiveAppealUseCase {

    override fun giveAppeal(appealRequest: AppealRequest): Flow<RemoteApiResult<ApiResponse<AppealResponse>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = appealRepository.giveAppeal(appealRequest)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.not_found)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }.catch {
            emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
        }.flowOn(Dispatchers.IO)
    }
}
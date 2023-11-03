package safeme.uz.domain.usecase.impl

import android.app.Application
import android.content.res.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import safeme.uz.R
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.MessageData
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.request.NeighborhoodRequest
import safeme.uz.data.remote.response.Address
import safeme.uz.data.remote.response.NeighborhoodInfo
import safeme.uz.data.repository.appeal.AppealRepository
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.GetMFYsByIdUseCase
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.isConnected
import javax.inject.Inject


class GetMFYsByIdUseCaseImpl @Inject constructor(
    private val repository: AuthRepository,
    private val appealRepository: AppealRepository,
    private val application: Application
) : GetMFYsByIdUseCase {
    override fun invoke(districtId: Int) = flow<ResultData<List<Address>>> {
        if (isConnected()) {
            val response = repository.getMFYById(districtId)
            if (response.success) {
                response.body?.let {
                    emit(ResultData.Success(it))
                }
            } else {
                when (response.code) {
                    404, 0 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.mfys_not_found)))
                    in 500..599 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.internal_server_error)))
                    else -> emit(ResultData.Fail(message = MessageData.Resource(R.string.some_error_occurred)))
                }
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

    override fun getNeighborhoodByDistrict(neighborhoodRequest: NeighborhoodRequest): Flow<RemoteApiResult<ApiResponse<ArrayList<NeighborhoodInfo>>>> {
        return flow {
            val response = appealRepository.getNeighborhoodByDistrict(neighborhoodRequest)
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
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
import safeme.uz.data.remote.response.Address
import safeme.uz.data.remote.response.AddressResponse
import safeme.uz.data.remote.response.RegionInfo
import safeme.uz.data.repository.appeal.AppealRepository
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.GetRegionsUseCase
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.isConnected
import javax.inject.Inject


class GetRegionsUseCaseImpl @Inject constructor(
    private val repository: AuthRepository,
    private val appealRepository: AppealRepository,
    private val application: Application
) : GetRegionsUseCase {
    override fun invoke() = flow {
        if (isConnected()) {
            val response = repository.getRegions()
            when(response.code()){
                in 200..209->{
                    response.body()?.body?.let {
                        emit(ResultData.Success(it))
                    }
                }
                404 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.regions_not_found)))
                in 500..599 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.internal_server_error)))
                else -> emit(ResultData.Fail(message = MessageData.Resource(R.string.some_error_occurred)))
            }

        } else {
            emit(ResultData.Fail(message = MessageData.Resource(R.string.internet_not_connected)))
        }
    }.catch {
        emit(ResultData.Fail(message = MessageData.Resource(R.string.some_error_occurred)))
    }.flowOn(Dispatchers.IO)



    override fun getRegions(): Flow<RemoteApiResult<ApiResponse<ArrayList<RegionInfo>>>> {
        return flow {
            val response = appealRepository.getRegions()
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
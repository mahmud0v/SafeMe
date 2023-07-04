package safeme.uz.domain.usecase.impl

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
import safeme.uz.data.remote.response.AddressResponse
import safeme.uz.data.remote.response.RegionInfo
import safeme.uz.data.repository.appeal.AppealRepository
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.GetRegionsUseCase
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.isConnected
import javax.inject.Inject


class GetRegionsUseCaseImpl @Inject constructor(
    private val repository: AuthRepository,
    private val appealRepository: AppealRepository
) : GetRegionsUseCase {
    override fun invoke() = flow<ResultData<AddressResponse>> {
        if (isConnected()) {
            val response = repository.getRegions()
            if (response.success) {
                response.body?.let {
                    emit(ResultData.Success(it))
                }
            } else {
                when (response.code) {
                    404, 0 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.regions_not_found)))
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

    override fun getRegions(): Flow<RemoteApiResult<ApiResponse<ArrayList<RegionInfo>>>> {
        return flow {
            val response = appealRepository.getRegions()
            val code = response.body()?.code
            if (code == 200) {
                emit(RemoteApiResult.Success(response.body()!!))
            } else {
                emit(RemoteApiResult.Error(response.body()?.message!!))
            }
        }
    }


}
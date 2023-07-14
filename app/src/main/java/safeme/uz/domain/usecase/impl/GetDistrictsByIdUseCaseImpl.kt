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
import safeme.uz.data.remote.request.DistrictByIdRequest
import safeme.uz.data.remote.response.DistrictInfo
import safeme.uz.data.repository.appeal.AppealRepository
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.GetDistrictsByIdUseCase
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.isConnected
import javax.inject.Inject


class GetDistrictsByIdUseCaseImpl @Inject constructor(
    private val repository: AuthRepository,
    private val appealRepository: AppealRepository,
    private val application: Application
) : GetDistrictsByIdUseCase {
    override fun invoke(regionId: Int) = flow {
        if (isConnected()) {
            val response = repository.getDistrictsById(regionId)
            if (response.success) {
                response.body?.let {
                    emit(ResultData.Success(it))
                }
            } else {
                when (response.code) {
                    404, 0 -> emit(ResultData.Fail(message = MessageData.Resource(R.string.districts_not_found)))
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


    override fun getDistrictsByRegion(districtByIdRequest: DistrictByIdRequest): Flow<RemoteApiResult<ApiResponse<ArrayList<DistrictInfo>>>> {
        return flow {
            val response = appealRepository.getDistrictsByRegion(districtByIdRequest)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }

}
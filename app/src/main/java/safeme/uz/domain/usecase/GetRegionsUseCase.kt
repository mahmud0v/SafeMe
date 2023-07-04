package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.response.AddressResponse
import safeme.uz.data.remote.response.RegionInfo
import safeme.uz.utils.RemoteApiResult

interface GetRegionsUseCase {
    operator fun invoke(): Flow<ResultData<AddressResponse>>

    fun getRegions(): Flow<RemoteApiResult<ApiResponse<ArrayList<RegionInfo>>>>

}
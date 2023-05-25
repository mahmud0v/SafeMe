package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.response.AddressResponse
import safeme.uz.data.remote.response.RegionInfo
import safeme.uz.utils.AnnouncementResult

interface GetRegionsUseCase {
    operator fun invoke(): Flow<ResultData<AddressResponse>>

    fun getRegions(): Flow<AnnouncementResult<ApiResponse<ArrayList<RegionInfo>>>>

}
package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.request.DistrictByIdRequest
import safeme.uz.data.remote.response.Address
import safeme.uz.data.remote.response.DistrictInfo
import safeme.uz.utils.AnnouncementResult

interface GetDistrictsByIdUseCase {
    operator fun invoke(regionId: Int): Flow<ResultData<List<Address>>>

    fun getDistrictsByRegion(districtByIdRequest: DistrictByIdRequest): Flow<AnnouncementResult<ApiResponse<ArrayList<DistrictInfo>>>>
}
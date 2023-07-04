package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.request.NeighborhoodRequest
import safeme.uz.data.remote.response.Address
import safeme.uz.data.remote.response.NeighborhoodInfo
import safeme.uz.utils.RemoteApiResult

interface GetMFYsByIdUseCase {
    operator fun invoke(districtId: Int): Flow<ResultData<List<Address>>>

    fun getNeighborhoodByDistrict(neighborhoodRequest: NeighborhoodRequest):Flow<RemoteApiResult<ApiResponse<ArrayList<NeighborhoodInfo>>>>


}
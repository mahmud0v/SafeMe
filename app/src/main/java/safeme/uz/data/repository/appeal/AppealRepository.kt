package safeme.uz.data.repository.appeal

import retrofit2.Response
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.DistrictByIdRequest
import safeme.uz.data.remote.request.InspectorMFYRequest
import safeme.uz.data.remote.request.NeighborhoodRequest
import safeme.uz.data.remote.response.DistrictInfo
import safeme.uz.data.remote.response.InspectorInfo
import safeme.uz.data.remote.response.NeighborhoodInfo
import safeme.uz.data.remote.response.RegionInfo

interface AppealRepository {

    suspend fun getRegions(): Response<ApiResponse<ArrayList<RegionInfo>>>

    suspend fun getDistrictsByRegion(districtByIdRequest: DistrictByIdRequest): Response<ApiResponse<ArrayList<DistrictInfo>>>

    suspend fun getNeighborhoodByDistrict(neighborhoodRequest: NeighborhoodRequest): Response<ApiResponse<ArrayList<NeighborhoodInfo>>>

    suspend fun getInspectorsByMFY(inspectorMFYRequest: InspectorMFYRequest): Response<ApiResponse<ArrayList<InspectorInfo>>>

}
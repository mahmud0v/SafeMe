package safeme.uz.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.SosBody
import safeme.uz.data.remote.request.DistrictByIdRequest
import safeme.uz.data.remote.request.InspectorMFYRequest
import safeme.uz.data.remote.request.NeighborhoodRequest
import safeme.uz.data.remote.request.SosRequest
import safeme.uz.data.remote.response.DistrictInfo
import safeme.uz.data.remote.response.InspectorInfo
import safeme.uz.data.remote.response.NeighborhoodInfo
import safeme.uz.data.remote.response.RegionInfo

interface AppealsApiService {

    @POST
    suspend fun getInspectorsData(
        @Url url: String,
        @Body inspectorMFYRequest: InspectorMFYRequest
    ): Response<ApiResponse<ArrayList<InspectorInfo>>>


    @POST
    suspend fun getRegions(
        @Url url: String
    ): Response<ApiResponse<ArrayList<RegionInfo>>>


    @POST
    suspend fun getDistrictsById(
        @Url url: String,
        @Body districtByIdRequest: DistrictByIdRequest
    ): Response<ApiResponse<ArrayList<DistrictInfo>>>


    @POST
    suspend fun getNeighborhoodByDistrict(
        @Url url: String,
        @Body neighborhoodRequest: NeighborhoodRequest
    ):Response<ApiResponse<ArrayList<NeighborhoodInfo>>>


    @POST
    suspend fun sosNotified(
        @Url url: String,
        @Body sosRequest: SosRequest
    ):Response<ApiResponse<SosBody>>



















}
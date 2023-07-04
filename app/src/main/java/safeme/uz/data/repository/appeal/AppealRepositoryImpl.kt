package safeme.uz.data.repository.appeal

import retrofit2.Response
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.SosBody
import safeme.uz.data.remote.api.AppealsApiService
import safeme.uz.data.remote.request.DistrictByIdRequest
import safeme.uz.data.remote.request.InspectorMFYRequest
import safeme.uz.data.remote.request.NeighborhoodRequest
import safeme.uz.data.remote.request.SosRequest
import safeme.uz.data.remote.response.DistrictInfo
import safeme.uz.data.remote.response.InspectorInfo
import safeme.uz.data.remote.response.NeighborhoodInfo
import safeme.uz.data.remote.response.RegionInfo
import javax.inject.Inject

class AppealRepositoryImpl @Inject constructor(
    private val appealsApiService: AppealsApiService,
    private val sharedPreference: AppSharedPreference
) : AppealRepository {


    override suspend fun getRegions(): Response<ApiResponse<ArrayList<RegionInfo>>> {
        return appealsApiService.getRegions(
            "${sharedPreference.locale}/api/v1.0/regions/"
        )
    }

    override suspend fun getDistrictsByRegion(districtByIdRequest: DistrictByIdRequest): Response<ApiResponse<ArrayList<DistrictInfo>>> {
        return appealsApiService.getDistrictsById(
            "${sharedPreference.locale}/api/v1.0/district/by_region",
            districtByIdRequest
        )
    }

    override suspend fun getNeighborhoodByDistrict(neighborhoodRequest: NeighborhoodRequest): Response<ApiResponse<ArrayList<NeighborhoodInfo>>> {
        return appealsApiService.getNeighborhoodByDistrict(
            "${sharedPreference.locale}/api/v1.0/mahalla/by_district",
            neighborhoodRequest
        )
    }

    override suspend fun getInspectorsByMFY(inspectorMFYRequest: InspectorMFYRequest): Response<ApiResponse<ArrayList<InspectorInfo>>> {
        return appealsApiService.getInspectorsData(
            "${sharedPreference.locale}/api/v1.0/police/",
            inspectorMFYRequest
        )
    }

    override suspend fun sosNotified(sosRequest: SosRequest): Response<ApiResponse<SosBody>> {
        return appealsApiService.sosNotified(
            "${sharedPreference.locale}/api/v1.0/sos/",
            sosRequest
        )
    }

}
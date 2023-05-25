package safeme.uz.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.response.RegionInfo

interface AppApiService {

    @GET
    suspend fun logout(@Url url: String): ApiResponse<Boolean>


}
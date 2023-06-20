package safeme.uz.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Url
import safeme.uz.data.model.ApiResponse

interface AppApiService {

    @GET
    suspend fun logout(@Url url: String): ApiResponse<Boolean>


}
package safeme.uz.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.request.AnnouncementCategoryRequest
import safeme.uz.data.remote.request.AnnouncementNewsRequest
import safeme.uz.data.remote.response.AnnouncementCategoryResponse

interface AnnouncementApiService {

    @POST("uz/api/v1.0/category/")
    suspend fun getAllCategories(
        @Body announcementCategoryRequest: AnnouncementCategoryRequest
    ): Response<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>


    @POST("uz/api/v1.0/news/category")
    suspend fun getAllNewsByCategory(
        @Body announcementNewsRequest: AnnouncementNewsRequest
    ): Response<AnnouncementCategoryResponse<ArrayList<NewsData>>>

    @GET("uz/api/v1.0/news/view/{id}")
    suspend fun getNewsById(
        @Path("id") id: Int
    ): Response<AnnouncementCategoryResponse<NewsData>>


}
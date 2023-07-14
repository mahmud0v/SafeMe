package safeme.uz.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.request.AnnouncementCategoryRequest
import safeme.uz.data.remote.request.AnnouncementNewsRequest
import safeme.uz.data.remote.request.PollAnswerRequest
import safeme.uz.data.remote.request.RecommendationRequest
import safeme.uz.data.remote.response.AgeCategoryInfo
import safeme.uz.data.remote.response.AgeCategoryResponse
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.remote.response.GameRecommendationResponse
import safeme.uz.data.remote.response.PollAnswerResponse
import safeme.uz.data.remote.response.PollDetailResponse
import safeme.uz.data.remote.response.PollResponseInfo
import safeme.uz.data.remote.response.RecommendationInfo
import safeme.uz.data.remote.response.RecommendationInfoResponse
import safeme.uz.data.remote.response.RecommendationResponse

interface AnnouncementApiService {

    @POST
    suspend fun getAllCategories(
        @Url url: String,
        @Body announcementCategoryRequest: AnnouncementCategoryRequest
    ): Response<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>


    @POST
    suspend fun getAllNewsByCategory(
        @Url url: String,
        @Body announcementNewsRequest: AnnouncementNewsRequest
    ): Response<AnnouncementCategoryResponse<ArrayList<NewsData>>>


    @GET
    suspend fun getNewsById(
        @Url url: String
    ): Response<AnnouncementCategoryResponse<NewsData>>


    @POST
    suspend fun getAgeCategory(
        @Url url: String
    ): Response<AgeCategoryResponse<AgeCategoryInfo>>


    @POST
    suspend fun getRecommendsByCategory(
        @Url url: String,
        @Body recommendationRequest: RecommendationRequest
    ): Response<AgeCategoryResponse<RecommendationInfo>>


    @POST
    suspend fun getRecommendsInfoByCategory(
        @Url url: String,
        @Body ageCategoryRequest: AgeCategoryRequest
    ): Response<RecommendationInfoResponse>


    @GET
    suspend fun getRecommendById(
        @Url url: String
    ): Response<RecommendationResponse>

    @POST
    suspend fun getGameRecommendationsByAge(
        @Url url: String,
        @Body ageCategoryRequest: AgeCategoryRequest
    ): Response<ApiResponse<ArrayList<GameRecommendationResponse>>>


    @POST
    suspend fun getGameRecommendationByCategory(
        @Url url: String,
        @Body recommendationRequest: RecommendationRequest
    ):Response<ApiResponse<ArrayList<GameRecommendationResponse>>>

    @GET
    suspend fun getGameById(
        @Url url: String,
    ):Response<ApiResponse<GameRecommendationResponse>>


    @POST
    suspend fun getPollByAgeCategory(
        @Url url:String,
        @Body ageCategoryRequest: AgeCategoryRequest
    ): Response<ApiResponse<ArrayList<PollResponseInfo>>>


    @GET
    suspend fun getPollById(
        @Url url: String
    ): Response<ApiResponse<PollDetailResponse>>

    @POST
    suspend fun givePollAnswer(
        @Url url:String,
        @Body body:PollAnswerRequest
    ): Response<ApiResponse<PollAnswerResponse>>





}
package safeme.uz.data.repository.announcement

import retrofit2.Response
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.request.AnnouncementCategoryRequest
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

interface AnnouncementRepository {

    suspend fun getAllCategories(announcementCategoryRequest: AnnouncementCategoryRequest): Response<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>

    suspend fun getAllNewsByCategory(categoryId: String): Response<AnnouncementCategoryResponse<ArrayList<NewsData>>>

    suspend fun getNewsById(id: Int): Response<AnnouncementCategoryResponse<NewsData>>

    suspend fun getAgeCategory(): Response<AgeCategoryResponse<AgeCategoryInfo>>

    suspend fun getRecommendationByCategory(recommendationRequest: RecommendationRequest): Response<AgeCategoryResponse<RecommendationInfo>>

    suspend fun getRecommendationInfoByCategory(ageCategoryRequest: AgeCategoryRequest): Response<RecommendationInfoResponse>

    suspend fun getRecommendationById(id: Int): Response<RecommendationResponse>

    suspend fun getGameRecommendationByAge(ageCategoryRequest: AgeCategoryRequest): Response<ApiResponse<ArrayList<GameRecommendationResponse>>>

    suspend fun getGameRecommendationByCategory(recommendationRequest: RecommendationRequest): Response<ApiResponse<ArrayList<GameRecommendationResponse>>>

    suspend fun getGameById(id:Int): Response<ApiResponse<GameRecommendationResponse>>

    suspend fun getPollByAgeCategory(ageCategoryRequest: AgeCategoryRequest):Response<ApiResponse<ArrayList<PollResponseInfo>>>

    suspend fun getPollById(id:Int):Response<ApiResponse<PollDetailResponse>>

    suspend fun givePollAnswer(pollAnswerRequest: PollAnswerRequest):Response<ApiResponse<PollAnswerResponse>>
}
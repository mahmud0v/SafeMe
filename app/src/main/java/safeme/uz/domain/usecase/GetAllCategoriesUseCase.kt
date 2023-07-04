package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.request.AnnouncementCategoryRequest
import safeme.uz.data.remote.request.RecommendationRequest
import safeme.uz.data.remote.response.AgeCategoryInfo
import safeme.uz.data.remote.response.AgeCategoryResponse
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.remote.response.GameRecommendationResponse
import safeme.uz.data.remote.response.RecommendationInfo
import safeme.uz.data.remote.response.RecommendationInfoResponse
import safeme.uz.data.remote.response.RecommendationResponse
import safeme.uz.utils.RemoteApiResult

interface GetAllCategoriesUseCase {

    fun getAllCategories(announcementCategoryRequest: AnnouncementCategoryRequest): Flow<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>>

    fun getAllNewsByCategory(categoryId: String): Flow<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<NewsData>>>>

    fun getNewsById(id: Int): Flow<RemoteApiResult<AnnouncementCategoryResponse<NewsData>>>

    fun getAgeCategory(): Flow<RemoteApiResult<AgeCategoryResponse<AgeCategoryInfo>>>

    fun getRecommendationByCategory(recommendationRequest: RecommendationRequest): Flow<RemoteApiResult<AgeCategoryResponse<RecommendationInfo>>>

    fun getRecommendationInfoByCategory(ageCategoryRequest: AgeCategoryRequest): Flow<RemoteApiResult<RecommendationInfoResponse>>

    fun getRecommendationById(id: Int): Flow<RemoteApiResult<RecommendationResponse>>

    fun getGameRecommendationByAge(ageCategoryRequest: AgeCategoryRequest): Flow<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>>

    fun getGameRecommendationByCategory(recommendationRequest: RecommendationRequest): Flow<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>>

    fun getGameById(id:Int):Flow<RemoteApiResult<ApiResponse<GameRecommendationResponse>>>

}
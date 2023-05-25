package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.request.AnnouncementCategoryRequest
import safeme.uz.data.remote.request.AnnouncementNewsRequest
import safeme.uz.data.remote.request.RecommendationRequest
import safeme.uz.data.remote.response.AgeCategoryInfo
import safeme.uz.data.remote.response.AgeCategoryResponse
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.remote.response.RecommendationInfo
import safeme.uz.data.remote.response.RecommendationInfoResponse
import safeme.uz.data.remote.response.RecommendationResponse
import safeme.uz.utils.AnnouncementResult

interface GetAllCategoriesUseCase {

    fun getAllCategories(announcementCategoryRequest: AnnouncementCategoryRequest): Flow<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>>

    fun getAllNewsByCategory(categoryId: String): Flow<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<NewsData>>>>

    fun getNewsById(id: Int): Flow<AnnouncementResult<AnnouncementCategoryResponse<NewsData>>>

    fun getAgeCategory(): Flow<AnnouncementResult<AgeCategoryResponse<AgeCategoryInfo>>>

    fun getRecommendationByCategory(recommendationRequest: RecommendationRequest): Flow<AnnouncementResult<AgeCategoryResponse<RecommendationInfo>>>

    fun getRecommendationInfoByCategory(ageCategoryRequest: AgeCategoryRequest): Flow<AnnouncementResult<RecommendationInfoResponse>>

    fun getRecommendationById(id: Int): Flow<AnnouncementResult<RecommendationResponse>>

}
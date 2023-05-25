package safeme.uz.data.repository.announcement

import retrofit2.Response
import retrofit2.http.Path
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.api.AnnouncementApiService
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
import javax.inject.Inject


class AnnouncementRepositoryImpl @Inject constructor(
    private val announcementApiService: AnnouncementApiService,
    private val sharedPreference: AppSharedPreference
) : AnnouncementRepository {


    override suspend fun getAllCategories(announcementCategoryRequest: AnnouncementCategoryRequest): Response<AnnouncementCategoryResponse<ArrayList<CategoriesData>>> {
        return announcementApiService.getAllCategories(
            "${sharedPreference.locale}/api/v1.0/category/",
            announcementCategoryRequest
        )

    }

    override suspend fun getAllNewsByCategory(categoryId: String): Response<AnnouncementCategoryResponse<ArrayList<NewsData>>> {
        return announcementApiService.getAllNewsByCategory(
            "${sharedPreference.locale}/api/v1.0/news/category",
            AnnouncementNewsRequest(categoryId)
        )
    }


    override suspend fun getNewsById(id: Int): Response<AnnouncementCategoryResponse<NewsData>> {
        return announcementApiService.getNewsById(
            "${sharedPreference.locale}/api/v1.0/news/view/$id",
        )
    }

    override suspend fun getAgeCategory(): Response<AgeCategoryResponse<AgeCategoryInfo>> {
        return announcementApiService.getAgeCategory(
            "${sharedPreference.locale}/api/v1.0/agecategory/"
        )
    }

    override suspend fun getRecommendationByCategory(recommendationRequest: RecommendationRequest): Response<AgeCategoryResponse<RecommendationInfo>> {
        return announcementApiService.getRecommendsByCategory(
            "${sharedPreference.locale}/api/v1.0/recommendation/category",
            recommendationRequest
        )
    }

    override suspend fun getRecommendationInfoByCategory(ageCategoryRequest: AgeCategoryRequest): Response<RecommendationInfoResponse> {
        return announcementApiService.getRecommendsInfoByCategory(
            "${sharedPreference.locale}/api/v1.0/recommendation/agecategory",
            ageCategoryRequest
        )
    }

    override suspend fun getRecommendationById(id: Int): Response<RecommendationResponse> {
        return announcementApiService.getRecommendById(
            "${sharedPreference.locale}/api/v1.0/recommendation/view/$id"
        )
    }


}

package safeme.uz.data.repository.announcement

import retrofit2.Response
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.api.AnnouncementApiService
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.request.AnnouncementCategoryRequest
import safeme.uz.data.remote.request.AnnouncementNewsRequest
import safeme.uz.data.remote.request.PollAnswerRequest
import safeme.uz.data.remote.request.AgeCatRequest
import safeme.uz.data.remote.request.GameBookmarkRequest
import safeme.uz.data.remote.request.RecommendationRequest
import safeme.uz.data.remote.response.AgeCategoryInfo
import safeme.uz.data.remote.response.AgeCategoryResponse
import safeme.uz.data.remote.response.AllBookmarkGame
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.remote.response.GameBookmarkResponse
import safeme.uz.data.remote.response.GameRecommendationResponse
import safeme.uz.data.remote.response.PollAnswerResponse
import safeme.uz.data.remote.response.PollDetailResponse
import safeme.uz.data.remote.response.PollResponseInfo
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

    override suspend fun getGameRecommendationByAge(ageCategoryRequest: AgeCategoryRequest): Response<ApiResponse<ArrayList<GameRecommendationResponse>>> {
        return announcementApiService.getGameRecommendationsByAge(
            "${sharedPreference.locale}/api/v1.0/games/age",
            ageCategoryRequest
        )
    }

    override suspend fun getGameRecommendationByCategory(recommendationRequest: RecommendationRequest): Response<ApiResponse<ArrayList<GameRecommendationResponse>>> {
        return announcementApiService.getGameRecommendationByCategory(
            "${sharedPreference.locale}/api/v1.0/games/category",
            recommendationRequest
        )
    }

    override suspend fun getGameById(id: Int): Response<ApiResponse<GameRecommendationResponse>> {
        return announcementApiService.getGameById(
            "${sharedPreference.locale}/api/v1.0/games/view/$id"
        )
    }

    override suspend fun getPollByAgeCategory(ageCategoryRequest: AgeCategoryRequest): Response<ApiResponse<ArrayList<PollResponseInfo>>> {
        return announcementApiService.getPollByAgeCategory(
            "${sharedPreference.locale}/api/v1.0/polling/all",
            ageCategoryRequest
        )
    }

    override suspend fun getPollById(id: Int): Response<ApiResponse<PollDetailResponse>> {
        return announcementApiService.getPollById(
            "${sharedPreference.locale}/api/v1.0/polling/view/$id"
        )
    }

    override suspend fun givePollAnswer(pollAnswerRequest: PollAnswerRequest): Response<ApiResponse<PollAnswerResponse>> {
        return announcementApiService.givePollAnswer(
            "${sharedPreference.locale}/api/v1.0/polling/answer",
            pollAnswerRequest
        )
    }

    override suspend fun getRecAgeCat(recAgeCatRequest: AgeCatRequest): Response<ApiResponse<ArrayList<RecommendationInfo>>> {
        return announcementApiService.getRecAgeCat(
            "${sharedPreference.locale}/api/v1.0/recommendation/agecat",
            recAgeCatRequest
        )
    }

    override suspend fun getGameAgeCat(gameAgeCatRequest: AgeCatRequest): Response<ApiResponse<ArrayList<GameRecommendationResponse>>> {
        return announcementApiService.getGameAgeCat(
            "${sharedPreference.locale}/api/v1.0/games/agecategory",
            gameAgeCatRequest
        )
    }

    override suspend fun gameItemBookmark(gameBookmarkRequest: GameBookmarkRequest): Response<ApiResponse<GameBookmarkResponse>> {
        return announcementApiService.gameItemBookmark(
            "${sharedPreference.locale}/api/v1.0/games/bookmark",
            gameBookmarkRequest
        )
    }

    override suspend fun gameItemDeleteBookmark(gameBookmarkRequest: GameBookmarkRequest): Response<ApiResponse<Nothing>> {
        return announcementApiService.gameItemDeleteBookmark(
            "${sharedPreference.locale}",
            gameBookmarkRequest
        )
    }

    override suspend fun allBookmarkGame(agecatgory:Int,category:Int): Response<ApiResponse<ArrayList<GameRecommendationResponse>>> {
        return announcementApiService.getAllBookmarkGame(
            "${sharedPreference.locale}/api/v1.0/games/bookmark",
            agecatgory,
            category
        )
    }

    override suspend fun allUnBookmarkGame(ageCatRequest: AgeCatRequest): Response<ApiResponse<ArrayList<GameRecommendationResponse>>> {
        return announcementApiService.getALlUnBookmarkedGame(
            "${sharedPreference.locale}/api/v1.0/games/unbookmark",
            ageCatRequest
        )
    }

    override suspend fun allBookmarkGameByAgeCategory(agecategory: Int): Response<ApiResponse<ArrayList<GameRecommendationResponse>>> {
        return announcementApiService.getAllBookmarkGameByAgeCategory(
            "${sharedPreference.locale}/api/v1.0/games/bookmark",
            agecategory
        )
    }

    override suspend fun allUnBookmarkGameByAgeCategory(ageCategoryRequest: AgeCategoryRequest): Response<ApiResponse<ArrayList<GameRecommendationResponse>>> {
        return announcementApiService.getAllUnBookmarkedByAgeCategory(
            "${sharedPreference.locale}/api/v1.0/games/unbookmark",
            ageCategoryRequest
        )
    }


}




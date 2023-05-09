package safeme.uz.data.repository.announcement

import retrofit2.Response
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.api.AnnouncementApiService
import safeme.uz.data.remote.request.AnnouncementCategoryRequest
import safeme.uz.data.remote.request.AnnouncementNewsRequest
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import javax.inject.Inject

class AnnouncementRepositoryImpl @Inject constructor(
    private val announcementApiService: AnnouncementApiService,
) : AnnouncementRepository {


    override suspend fun getAllCategories(): Response<AnnouncementCategoryResponse<ArrayList<CategoriesData>>> {
        return announcementApiService.getAllCategories(
            AnnouncementCategoryRequest("news")
        )

    }

    override suspend fun getAllNewsByCategory(categoryId: String): Response<AnnouncementCategoryResponse<ArrayList<NewsData>>> {
        return announcementApiService.getAllNewsByCategory(
            AnnouncementNewsRequest(categoryId)
        )
    }


    override suspend fun getNewsById(id: Int): Response<AnnouncementCategoryResponse<NewsData>> {
        return announcementApiService.getNewsById(id)
    }


}

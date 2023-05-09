package safeme.uz.data.repository.announcement

import retrofit2.Response
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.response.AnnouncementCategoryResponse

interface AnnouncementRepository {

    suspend fun getAllCategories() : Response<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>

    suspend fun getAllNewsByCategory(categoryId:String):Response<AnnouncementCategoryResponse<ArrayList<NewsData>>>

    suspend fun getNewsById(id:Int):Response<AnnouncementCategoryResponse<NewsData>>





}
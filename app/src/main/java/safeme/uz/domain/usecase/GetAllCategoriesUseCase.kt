package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.utils.AnnouncementResult

interface GetAllCategoriesUseCase {

     fun getAllCategories() : Flow<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>>

     fun getAllNewsByCategory(categoryId:String):Flow<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<NewsData>>>>

     fun getNewsById(id:Int):Flow<AnnouncementResult<AnnouncementCategoryResponse<NewsData>>>

}
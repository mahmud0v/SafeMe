package safeme.uz.domain.usecase.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.repository.announcement.AnnouncementRepository
import safeme.uz.domain.usecase.GetAllCategoriesUseCase
import safeme.uz.utils.AnnouncementResult
import javax.inject.Inject

class GetAllCategoriesUseCaseImpl @Inject constructor(
    private val announcementRepository: AnnouncementRepository
) : GetAllCategoriesUseCase {

    override fun getAllCategories(): Flow<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>> {
        return flow {
            val response = announcementRepository.getAllCategories()
            val code = response.body()?.code
            if (code == 200) {
                emit(AnnouncementResult.Success(response.body()!!))
            } else {
                emit(AnnouncementResult.Error(response.message()))
            }
        }
    }

    override fun getAllNewsByCategory(categoryId: String): Flow<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<NewsData>>>> {
        return flow {
            val response = announcementRepository.getAllNewsByCategory(categoryId)
            if (response.code() == 200){
                emit(AnnouncementResult.Success(response.body()!!))
            }else {
                emit(AnnouncementResult.Error(response.message()))
            }
        }
    }


    override fun getNewsById(id: Int): Flow<AnnouncementResult<AnnouncementCategoryResponse<NewsData>>> {
        return flow {
            val response = announcementRepository.getNewsById(id)
            if (response.code() == 200) {
                emit(AnnouncementResult.Success(response.body()!!))
            } else {
                emit(AnnouncementResult.Error(response.message()))
            }
        }
    }

}
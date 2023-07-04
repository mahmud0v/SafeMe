package safeme.uz.domain.usecase.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
import safeme.uz.data.repository.announcement.AnnouncementRepository
import safeme.uz.domain.usecase.GetAllCategoriesUseCase
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

class GetAllCategoriesUseCaseImpl @Inject constructor(
    private val announcementRepository: AnnouncementRepository
) : GetAllCategoriesUseCase {

    override fun getAllCategories(announcementCategoryRequest: AnnouncementCategoryRequest): Flow<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>> {
        return flow {
            val response = announcementRepository.getAllCategories(announcementCategoryRequest)
            val code = response.body()?.code
            if (code == 200) {
                emit(RemoteApiResult.Success(response.body()!!))
            } else {
                emit(RemoteApiResult.Error(response.body()?.message!!))
            }
        }
    }


    override fun getAllNewsByCategory(categoryId: String): Flow<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<NewsData>>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = announcementRepository.getAllNewsByCategory(categoryId)
            val code = response.body()?.code
            if (code == 200) {
                emit(RemoteApiResult.Success(response.body()!!))
            } else {
                emit(RemoteApiResult.Error(response.body()?.message!!))
            }
        }
    }


    override fun getNewsById(id: Int): Flow<RemoteApiResult<AnnouncementCategoryResponse<NewsData>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = announcementRepository.getNewsById(id)
            val code = response.body()?.code
            if (code == 200) {
                emit(RemoteApiResult.Success(response.body()!!))
            } else {
                emit(RemoteApiResult.Error(response.body()?.message!!))
            }
        }
    }

    override fun getAgeCategory(): Flow<RemoteApiResult<AgeCategoryResponse<AgeCategoryInfo>>> {
        return flow {
            val response = announcementRepository.getAgeCategory()
            val code = response.body()?.code
            if (code == 200) {
                emit(RemoteApiResult.Success(response.body()!!))
            } else {
                emit(RemoteApiResult.Error(response.body()?.message!!))
            }
        }
    }

    override fun getRecommendationByCategory(recommendationRequest: RecommendationRequest): Flow<RemoteApiResult<AgeCategoryResponse<RecommendationInfo>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = announcementRepository.getRecommendationByCategory(recommendationRequest)
            val code = response.body()?.code
            if (code == 200) {
                emit(RemoteApiResult.Success(response.body()!!))
            } else {
                emit(RemoteApiResult.Error(response.body()?.message!!))
            }
        }
    }

    override fun getRecommendationInfoByCategory(ageCategoryRequest: AgeCategoryRequest): Flow<RemoteApiResult<RecommendationInfoResponse>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response =
                announcementRepository.getRecommendationInfoByCategory(ageCategoryRequest)
            val code = response.body()?.code
            if (code == 200) {
                emit(RemoteApiResult.Success(response.body()!!))
            } else {
                emit(RemoteApiResult.Error(response.body()?.message!!))
            }
        }
    }

    override fun getRecommendationById(id: Int): Flow<RemoteApiResult<RecommendationResponse>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = announcementRepository.getRecommendationById(id)
            val code = response.body()?.code
            if (code == 200) {
                emit(RemoteApiResult.Success(response.body()!!))
            } else {
                emit(RemoteApiResult.Error(response.body()?.message!!))
            }
        }
    }

    override fun getGameRecommendationByAge(ageCategoryRequest: AgeCategoryRequest): Flow<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = announcementRepository.getGameRecommendationByAge(ageCategoryRequest)
            if (response.body()?.code == 200) {
                emit(RemoteApiResult.Success(response.body()!!))
            } else {
                emit(RemoteApiResult.Error(response.body()?.message!!))
            }
        }
    }

    override fun getGameRecommendationByCategory(recommendationRequest: RecommendationRequest): Flow<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response =
                announcementRepository.getGameRecommendationByCategory(recommendationRequest)
            if (response.body()?.code == 200) {
                emit(RemoteApiResult.Success(response.body()!!))
            } else {
                emit(RemoteApiResult.Error(response.body()?.message!!))
            }
        }
    }

    override fun getGameById(id: Int): Flow<RemoteApiResult<ApiResponse<GameRecommendationResponse>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = announcementRepository.getGameById(id)
            if (response.body()?.code == 200) {
                emit(RemoteApiResult.Success(response.body()!!))
            } else {
                emit(RemoteApiResult.Error(response.body()?.message!!))
            }
        }
    }
}

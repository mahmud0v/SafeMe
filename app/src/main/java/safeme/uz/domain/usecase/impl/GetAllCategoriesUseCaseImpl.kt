package safeme.uz.domain.usecase.impl
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.media.audiofx.DynamicsProcessing.Config
import android.view.ContextMenu
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import safeme.uz.R
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
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

class GetAllCategoriesUseCaseImpl @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val application: Application
) : GetAllCategoriesUseCase {

    override fun getAllCategories(announcementCategoryRequest: AnnouncementCategoryRequest): Flow<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>> {
        return flow {
            val response = announcementRepository.getAllCategories(announcementCategoryRequest)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }


    override fun getAllNewsByCategory(categoryId: String): Flow<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<NewsData>>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = announcementRepository.getAllNewsByCategory(categoryId)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }


    override fun getNewsById(id: Int): Flow<RemoteApiResult<AnnouncementCategoryResponse<NewsData>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = announcementRepository.getNewsById(id)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }

    override fun getAgeCategory(): Flow<RemoteApiResult<AgeCategoryResponse<AgeCategoryInfo>>> {
        return flow {
            val response = announcementRepository.getAgeCategory()
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }

    override fun getRecommendationByCategory(recommendationRequest: RecommendationRequest): Flow<RemoteApiResult<AgeCategoryResponse<RecommendationInfo>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = announcementRepository.getRecommendationByCategory(recommendationRequest)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }

    override fun getRecommendationInfoByCategory(ageCategoryRequest: AgeCategoryRequest): Flow<RemoteApiResult<RecommendationInfoResponse>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = announcementRepository.getRecommendationInfoByCategory(ageCategoryRequest)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }

    override fun getRecommendationById(id: Int): Flow<RemoteApiResult<RecommendationResponse>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = announcementRepository.getRecommendationById(id)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }

    override fun getGameRecommendationByAge(ageCategoryRequest: AgeCategoryRequest): Flow<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = announcementRepository.getGameRecommendationByAge(ageCategoryRequest)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }

    override fun getGameRecommendationByCategory(recommendationRequest: RecommendationRequest): Flow<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response =
                announcementRepository.getGameRecommendationByCategory(recommendationRequest)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }

        }
    }

    override fun getGameById(id: Int): Flow<RemoteApiResult<ApiResponse<GameRecommendationResponse>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = announcementRepository.getGameById(id)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.no_data)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }


}

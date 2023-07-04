package safeme.uz.presentation.viewmodel.recommendation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.request.AnnouncementCategoryRequest
import safeme.uz.data.remote.request.RecommendationRequest
import safeme.uz.data.remote.response.AgeCategoryResponse
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.remote.response.RecommendationInfo
import safeme.uz.data.remote.response.RecommendationInfoResponse
import safeme.uz.domain.usecase.GetAllCategoriesUseCase
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.Keys
import javax.inject.Inject

@HiltViewModel
class RecommendationPagerViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {
    private val recommendationByCategoryMutableLiveData =
        MutableLiveData<RemoteApiResult<AgeCategoryResponse<RecommendationInfo>>>()
    val recommendationByCategoryLiveData = recommendationByCategoryMutableLiveData

    private val recommendationInfoMutableLiveData =
        MutableLiveData<RemoteApiResult<RecommendationInfoResponse>>()
    val recommendationInfoLiveData: LiveData<RemoteApiResult<RecommendationInfoResponse>> =
        recommendationInfoMutableLiveData

    private val recommendationAllCategoriesMutableLiveData =
        MutableLiveData<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>>()
    val recommendationAllCategoriesLiveData = recommendationAllCategoriesMutableLiveData

    private val gameRecommendationMutableLiveData =
        MutableLiveData<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>>()
    val gameRecommendationLiveData: LiveData<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>> =
        gameRecommendationMutableLiveData

    init {
        getRecAllCategories(AnnouncementCategoryRequest(Keys.RECOMMENDATION))
        getRecAllCategories(AnnouncementCategoryRequest(Keys.GAME))

    }

    fun getRecommendationByCategory(recommendationRequest: RecommendationRequest) =
        viewModelScope.launch {
            getAllCategoriesUseCase.getRecommendationByCategory(recommendationRequest).collect {
                recommendationByCategoryMutableLiveData.value = it
            }
        }

    fun getRecommendationInfoByCategory(ageCategoryRequest: AgeCategoryRequest) =
        viewModelScope.launch {
            getAllCategoriesUseCase.getRecommendationInfoByCategory(ageCategoryRequest).collect {
                recommendationInfoMutableLiveData.value = it
            }
        }

    private fun getRecAllCategories(announcementCategoryRequest: AnnouncementCategoryRequest) =
        viewModelScope.launch {
            getAllCategoriesUseCase.getAllCategories(announcementCategoryRequest).collect {
                when (announcementCategoryRequest.type) {
                    Keys.RECOMMENDATION -> {
                        recommendationAllCategoriesMutableLiveData.value = it
                    }

                    Keys.GAME -> {
                        gameRecommendationMutableLiveData.value = it
                    }
                }
            }
        }

}



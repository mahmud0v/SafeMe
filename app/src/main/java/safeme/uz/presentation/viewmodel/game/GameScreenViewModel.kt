package safeme.uz.presentation.viewmodel.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.request.AnnouncementCategoryRequest
import safeme.uz.data.remote.request.RecommendationRequest
import safeme.uz.data.remote.response.AgeCategoryInfo
import safeme.uz.data.remote.response.AgeCategoryResponse
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.remote.response.GameRecommendationResponse
import safeme.uz.domain.usecase.GetAllCategoriesUseCase
import safeme.uz.utils.AnnouncementResult
import safeme.uz.utils.Keys
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {

    private val gameRecommendationMutableLiveData =
        MutableLiveData<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>>()
    val gameRecommendationLiveData: LiveData<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>> =
        gameRecommendationMutableLiveData

    private val ageCategoryMutableLiveData =
        MutableLiveData<AnnouncementResult<AgeCategoryResponse<AgeCategoryInfo>>>()
    val ageCategoryLiveData: LiveData<AnnouncementResult<AgeCategoryResponse<AgeCategoryInfo>>> =
        ageCategoryMutableLiveData

    private val gameRecByAgeMutableLiveData = MutableLiveData<AnnouncementResult<ApiResponse<ArrayList<GameRecommendationResponse>>>>()
    val gameRecByAgeLiveData:LiveData<AnnouncementResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> = gameRecByAgeMutableLiveData

    private val gameRecByCategoryMutableLiveData = MutableLiveData<AnnouncementResult<ApiResponse<ArrayList<GameRecommendationResponse>>>>()
    val gameRecByCategoryLiveData:LiveData<AnnouncementResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> = gameRecByCategoryMutableLiveData

    init {
        getAgeCategories()
        getGameCategories()

    }

    private fun getAgeCategories() = viewModelScope.launch {
        getAllCategoriesUseCase.getAgeCategory().collect {
            ageCategoryMutableLiveData.value = it
        }
    }

    private fun getGameCategories() = viewModelScope.launch {
        getAllCategoriesUseCase.getAllCategories(AnnouncementCategoryRequest(Keys.GAME)).collect {
            gameRecommendationMutableLiveData.value = it
        }
    }

    fun getGameRecommendationByAge(ageCategoryRequest: AgeCategoryRequest) = viewModelScope.launch {
        getAllCategoriesUseCase.getGameRecommendationByAge(ageCategoryRequest).collect{
             gameRecByAgeMutableLiveData.value = it
        }
    }

    fun getGameRecommendationByCategory(recommendationRequest: RecommendationRequest) = viewModelScope.launch {
        getAllCategoriesUseCase.getGameRecommendationByCategory(recommendationRequest).collect{
            gameRecByCategoryMutableLiveData.value = it
        }
    }


}
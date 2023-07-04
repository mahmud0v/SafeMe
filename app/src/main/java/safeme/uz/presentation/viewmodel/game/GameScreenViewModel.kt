package safeme.uz.presentation.viewmodel.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.Keys
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {

    private val gameRecommendationMutableLiveData =
        MutableLiveData<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>>()
    val gameRecommendationLiveData: LiveData<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>> =
        gameRecommendationMutableLiveData

    private val ageCategoryMutableLiveData =
        MutableLiveData<RemoteApiResult<AgeCategoryResponse<AgeCategoryInfo>>>()
    val ageCategoryLiveData: LiveData<RemoteApiResult<AgeCategoryResponse<AgeCategoryInfo>>> = ageCategoryMutableLiveData

    private val gameRecByAgeMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>>()
    val gameRecByAgeLiveData: LiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> =
        gameRecByAgeMutableLiveData

    private val gameRecByCategoryMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>>()
    val gameRecByCategoryLiveData: LiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> =
        gameRecByCategoryMutableLiveData

//    private val errorMutableLiveData = MutableLiveData<String?>()
//    val errorLiveData: LiveData<String?> = errorMutableLiveData

    init {
        getGameCategories()
        getAgeCategories()
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
        getAllCategoriesUseCase.getGameRecommendationByAge(ageCategoryRequest).collect {
            gameRecByAgeMutableLiveData.value = it
        }
    }

    fun getGameRecommendationByCategory(recommendationRequest: RecommendationRequest) =
        viewModelScope.launch {
            getAllCategoriesUseCase.getGameRecommendationByCategory(recommendationRequest).collect {
                gameRecByCategoryMutableLiveData.value = it
            }
        }


}
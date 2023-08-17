package safeme.uz.presentation.viewmodel.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.remote.request.AgeCatRequest
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.request.AnnouncementCategoryRequest
import safeme.uz.data.remote.request.GameBookmarkRequest
import safeme.uz.data.remote.request.RecommendationRequest
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.remote.response.GameBookmarkResponse
import safeme.uz.data.remote.response.GameRecommendationResponse
import safeme.uz.domain.usecase.GetAllCategoriesUseCase
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

@HiltViewModel
class GamePagerViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {

    private val gameRecommendationMutableLiveData =
        MutableLiveData<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>>()
    val gameRecommendationLiveData: LiveData<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>> =
        gameRecommendationMutableLiveData


    private val gameRecByAgeMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>>()
    val gameRecByAgeLiveData: LiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> =
        gameRecByAgeMutableLiveData

    private val gameRecByCategoryMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>>()
    val gameRecByCategoryLiveData: LiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> =
        gameRecByCategoryMutableLiveData

    private val gameAgeCateMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>>()
    val gameAgeCateLiveData: LiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> =
        gameAgeCateMutableLiveData

    private val gameItemBookmarkMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<GameBookmarkResponse>>>()
    val gameItemBookmarkLiveData: LiveData<RemoteApiResult<ApiResponse<GameBookmarkResponse>>> =
        gameItemBookmarkMutableLiveData


    private val gameItemDeleteBookmarkMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<Nothing>>>()
    val gameItemDeleteBookmarkLiveData: LiveData<RemoteApiResult<ApiResponse<Nothing>>> =
        gameItemDeleteBookmarkMutableLiveData


    private val allGameBookmarkMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>>()
    val allGameBookmarkLiveData: LiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> =
        allGameBookmarkMutableLiveData

    private val allGameUnBookmarkedMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>>()
    val allGameUnBookmarkedLiveData: LiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> =
        allGameUnBookmarkedMutableLiveData

    private val allGameUnBookmarkedByAgeCategoryMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>>()
    val allGameUnBookmarkedByAgeCategoryLiveData: LiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> =
        allGameUnBookmarkedByAgeCategoryMutableLiveData

    private val allGameBookmarkedByAgeCategoryMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>>()
    val allGameBookmarkedByAgeCategoryLiveData: LiveData<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> =
        allGameBookmarkedByAgeCategoryMutableLiveData

    init {
        getGameCategories()
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

    fun getGameAgeCat(gameAgeCatRequest: AgeCatRequest) = viewModelScope.launch {
        getAllCategoriesUseCase.getGameAgeCat(gameAgeCatRequest).collect {
            gameAgeCateMutableLiveData.value = it
        }
    }

    fun gameItemBookmark(gameBookmarkRequest: GameBookmarkRequest) = viewModelScope.launch {
        getAllCategoriesUseCase.gameItemBookmark(gameBookmarkRequest).collect {
            gameItemBookmarkMutableLiveData.value = it
        }
    }

    fun gameItemDeleteBookmark(gameBookmarkRequest: GameBookmarkRequest) = viewModelScope.launch {
        getAllCategoriesUseCase.gameItemDeleteBookmark(gameBookmarkRequest).collect {
            gameItemDeleteBookmarkMutableLiveData.value = it
        }
    }

    fun allGameBookmark(agecategory: Int, category: Int) = viewModelScope.launch {
        getAllCategoriesUseCase.allBookmarkGame(agecategory, category).collect {
            allGameBookmarkMutableLiveData.value = it
        }
    }

    fun allGameUnBookmark(ageCatRequest: AgeCatRequest) = viewModelScope.launch {
        getAllCategoriesUseCase.allUnBookmarkedGame(ageCatRequest).collect {
            allGameUnBookmarkedMutableLiveData.value = it
        }
    }

    fun allGameBookmarkByAgeCategory(agecategory: Int) = viewModelScope.launch {
        getAllCategoriesUseCase.allBookmarkGameByAgeCategory(agecategory).collect {
            allGameBookmarkedByAgeCategoryMutableLiveData.value = it
        }
    }

    fun allGameUnBookmarkByAgeCategory(ageCategoryRequest: AgeCategoryRequest) =
        viewModelScope.launch {
            getAllCategoriesUseCase.allUnBookmarkedGameByAgeCategory(ageCategoryRequest).collect {
                allGameUnBookmarkedByAgeCategoryMutableLiveData.value = it
            }
        }
}
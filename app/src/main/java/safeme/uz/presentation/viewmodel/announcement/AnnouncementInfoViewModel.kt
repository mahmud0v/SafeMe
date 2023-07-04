package safeme.uz.presentation.viewmodel.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.remote.response.GameRecommendationResponse
import safeme.uz.data.remote.response.RecommendationResponse
import safeme.uz.domain.usecase.GetAllCategoriesUseCase
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

@HiltViewModel
class AnnouncementInfoViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
) : ViewModel() {
    private val getNewsByIdMutableLiveData =
        MutableLiveData<RemoteApiResult<AnnouncementCategoryResponse<NewsData>>>()
    val getNewsByIdLiveData: LiveData<RemoteApiResult<AnnouncementCategoryResponse<NewsData>>> =
        getNewsByIdMutableLiveData

    private val getRecommendationMutableLiveData =
        MutableLiveData<RemoteApiResult<RecommendationResponse>>()
    val getRecommendationLiveData: LiveData<RemoteApiResult<RecommendationResponse>> =
        getRecommendationMutableLiveData

    private val getGameByIdMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<GameRecommendationResponse>>>()
    val getGameByIdLiveData: LiveData<RemoteApiResult<ApiResponse<GameRecommendationResponse>>> =
        getGameByIdMutableLiveData


    fun getNewsById(id: Int) = viewModelScope.launch {
        getAllCategoriesUseCase.getNewsById(id).collect {
            getNewsByIdMutableLiveData.value = it
        }
    }

    fun getRecommendationById(id: Int) = viewModelScope.launch {
        getAllCategoriesUseCase.getRecommendationById(id).collect {
            getRecommendationMutableLiveData.value = it
        }
    }

    fun getGameById(id: Int) = viewModelScope.launch {
        getAllCategoriesUseCase.getGameById(id).collect {
            getGameByIdMutableLiveData.value = it
        }
    }


}
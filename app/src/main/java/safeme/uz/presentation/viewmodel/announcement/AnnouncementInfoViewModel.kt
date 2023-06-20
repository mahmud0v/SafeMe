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
import safeme.uz.utils.AnnouncementResult
import javax.inject.Inject

@HiltViewModel
class AnnouncementInfoViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
) : ViewModel() {
    private val getNewsByIdMutableLiveData =
        MutableLiveData<AnnouncementResult<AnnouncementCategoryResponse<NewsData>>>()
    val getNewsByIdLiveData: LiveData<AnnouncementResult<AnnouncementCategoryResponse<NewsData>>> =
        getNewsByIdMutableLiveData

    private val getRecommendationMutableLiveData =
        MutableLiveData<AnnouncementResult<RecommendationResponse>>()
    val getRecommendationLiveData: LiveData<AnnouncementResult<RecommendationResponse>> =
        getRecommendationMutableLiveData

    private val getGameByIdMutableLiveData =
        MutableLiveData<AnnouncementResult<ApiResponse<GameRecommendationResponse>>>()
    val getGameByIdLiveData: LiveData<AnnouncementResult<ApiResponse<GameRecommendationResponse>>> =
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
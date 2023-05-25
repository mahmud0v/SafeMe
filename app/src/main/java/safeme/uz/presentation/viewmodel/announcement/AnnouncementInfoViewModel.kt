package safeme.uz.presentation.viewmodel.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.remote.response.RecommendationResponse
import safeme.uz.domain.usecase.GetAllCategoriesUseCase
import safeme.uz.utils.AnnouncementResult
import javax.inject.Inject
import kotlin.properties.Delegates

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


}
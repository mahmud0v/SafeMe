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
import safeme.uz.data.remote.request.AgeCatRequest
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.request.AnnouncementCategoryRequest
import safeme.uz.data.remote.request.GameBookmarkRequest
import safeme.uz.data.remote.request.RecommendationRequest
import safeme.uz.data.remote.response.AgeCategoryInfo
import safeme.uz.data.remote.response.AgeCategoryResponse
import safeme.uz.data.remote.response.AllBookmarkGame
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.remote.response.GameBookmarkResponse
import safeme.uz.data.remote.response.GameRecommendationResponse
import safeme.uz.domain.usecase.GetAllCategoriesUseCase
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {

    private val ageCategoryMutableLiveData =
        MutableLiveData<RemoteApiResult<AgeCategoryResponse<AgeCategoryInfo>>>()
    val ageCategoryLiveData: LiveData<RemoteApiResult<AgeCategoryResponse<AgeCategoryInfo>>> =
        ageCategoryMutableLiveData

    init {

        getAgeCategories()
    }

    private fun getAgeCategories() = viewModelScope.launch {
        getAllCategoriesUseCase.getAgeCategory().collect {
            ageCategoryMutableLiveData.value = it
        }
    }


}
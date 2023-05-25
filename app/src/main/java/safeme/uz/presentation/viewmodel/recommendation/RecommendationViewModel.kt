package safeme.uz.presentation.viewmodel.recommendation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import safeme.uz.data.remote.response.AgeCategoryInfo
import safeme.uz.data.remote.response.AgeCategoryResponse
import safeme.uz.domain.usecase.GetAllCategoriesUseCase
import safeme.uz.utils.AnnouncementResult
import javax.inject.Inject

@HiltViewModel
class RecommendationViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {
    private val getAgeCategoryMutableLiveData =
        MutableLiveData<AnnouncementResult<AgeCategoryResponse<AgeCategoryInfo>>>()
    val getAgeCategoryLiveData: LiveData<AnnouncementResult<AgeCategoryResponse<AgeCategoryInfo>>> =
        getAgeCategoryMutableLiveData


    init {
        getAgeCategory()
    }

    private fun getAgeCategory() = viewModelScope.launch {
        getAllCategoriesUseCase.getAgeCategory().collect {
            getAgeCategoryMutableLiveData.value = it
        }
    }



}
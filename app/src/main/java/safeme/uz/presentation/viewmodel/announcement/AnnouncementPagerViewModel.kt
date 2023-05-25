package safeme.uz.presentation.viewmodel.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.domain.usecase.GetAllCategoriesUseCase
import safeme.uz.utils.AnnouncementResult
import javax.inject.Inject

@HiltViewModel
class AnnouncementPagerViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
) : ViewModel() {

    private var getAllNewsMutableLiveData =
        MutableLiveData<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<NewsData>>>>()
    val getAllNewsLiveData: LiveData<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<NewsData>>>> =
        getAllNewsMutableLiveData


    fun getAllNewsByCategory(categoryId: String) = viewModelScope.launch {
        viewModelScope.launch {
            getAllCategoriesUseCase.getAllNewsByCategory(categoryId).collect {
                getAllNewsMutableLiveData.value = it
            }
        }
    }
}
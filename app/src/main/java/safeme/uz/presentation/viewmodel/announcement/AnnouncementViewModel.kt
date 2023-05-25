package safeme.uz.presentation.viewmodel.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.request.AnnouncementCategoryRequest
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.repository.announcement.AnnouncementRepositoryImpl
import safeme.uz.domain.usecase.ForgetPasswordUseCase
import safeme.uz.domain.usecase.GetAllCategoriesUseCase
import safeme.uz.domain.usecase.impl.GetAllCategoriesUseCaseImpl
import safeme.uz.utils.AnnouncementResult
import safeme.uz.utils.Keys
import javax.inject.Inject

@HiltViewModel
class AnnouncementViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
) : ViewModel() {
    private var getAllCategoriesMutableLiveData =
        MutableLiveData<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>>()
    val getAllCategoriesLiveData: LiveData<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>> =
        getAllCategoriesMutableLiveData


    init {
        getAllNewsCategories(AnnouncementCategoryRequest(Keys.NEWS))
    }

    private fun getAllNewsCategories(announcementCategoryRequest: AnnouncementCategoryRequest) =
        viewModelScope.launch {
            getAllCategoriesUseCase.getAllCategories(announcementCategoryRequest).collect {
                getAllCategoriesMutableLiveData.value = it
            }

        }


}
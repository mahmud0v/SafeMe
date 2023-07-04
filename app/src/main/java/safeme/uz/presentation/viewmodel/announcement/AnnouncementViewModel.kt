package safeme.uz.presentation.viewmodel.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.remote.request.AnnouncementCategoryRequest
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.domain.usecase.GetAllCategoriesUseCase
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.Keys
import javax.inject.Inject

@HiltViewModel
class AnnouncementViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
) : ViewModel() {
    private var getAllCategoriesMutableLiveData =
        MutableLiveData<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>>()
    val getAllCategoriesLiveData: LiveData<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>> =
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
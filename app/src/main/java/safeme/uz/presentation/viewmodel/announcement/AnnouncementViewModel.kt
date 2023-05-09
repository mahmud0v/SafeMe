package safeme.uz.presentation.viewmodel.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.repository.announcement.AnnouncementRepositoryImpl
import safeme.uz.domain.usecase.ForgetPasswordUseCase
import safeme.uz.domain.usecase.GetAllCategoriesUseCase
import safeme.uz.domain.usecase.impl.GetAllCategoriesUseCaseImpl
import safeme.uz.utils.AnnouncementResult
import javax.inject.Inject

@HiltViewModel
class AnnouncementViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {
    private var getAllCategoriesMutableLiveData =
        MutableLiveData<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>>()
    val getAllCategoriesLiveData: LiveData<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>> =
        getAllCategoriesMutableLiveData

    private var getAllNewsMutableLiveData =
        MutableLiveData<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<NewsData>>>>()
    val getAllNewsLiveData: LiveData<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<NewsData>>>> =
        getAllNewsMutableLiveData

    private var getNewsByIdMutableLiveData =
        MutableLiveData<AnnouncementResult<AnnouncementCategoryResponse<NewsData>>>()
    val getNewsByIdLiveData: LiveData<AnnouncementResult<AnnouncementCategoryResponse<NewsData>>> =
        getNewsByIdMutableLiveData

    fun getAllCategories() = viewModelScope.launch {
        getAllCategoriesUseCase.getAllCategories().collect {
            getAllCategoriesMutableLiveData.value = it
        }

    }

    fun getAllNewsByCategory(categoryId: String) = viewModelScope.launch {
        getAllCategoriesUseCase.getAllNewsByCategory(categoryId).collect {
            getAllNewsMutableLiveData.value = it
        }
    }

    fun getNewsById(id: Int) = viewModelScope.launch {
        getAllCategoriesUseCase.getNewsById(id).collect {
            getNewsByIdMutableLiveData.value = it
        }
    }


}
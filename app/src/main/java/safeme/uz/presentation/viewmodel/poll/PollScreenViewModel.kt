package safeme.uz.presentation.viewmodel.poll

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.response.AgeCategoryInfo
import safeme.uz.data.remote.response.AgeCategoryResponse
import safeme.uz.data.remote.response.PollResponseInfo
import safeme.uz.domain.usecase.GetAllCategoriesUseCase
import safeme.uz.domain.usecase.PollQuestionUseCase
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

@HiltViewModel
class PollScreenViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
) : ViewModel() {

    private val ageCategoryMutableLiveData =
        MutableLiveData<RemoteApiResult<AgeCategoryResponse<AgeCategoryInfo>>>()
    val ageCategoryLiveData: LiveData<RemoteApiResult<AgeCategoryResponse<AgeCategoryInfo>>> =
        ageCategoryMutableLiveData


    init {
        getAgeCategory()
    }

    private fun getAgeCategory() = viewModelScope.launch {
        getAllCategoriesUseCase.getAgeCategory().collect {
            ageCategoryMutableLiveData.value = it
        }
    }



}
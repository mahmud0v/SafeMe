package safeme.uz.presentation.viewmodel.poll


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.response.PollResponseInfo
import safeme.uz.domain.usecase.PollQuestionUseCase
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

@HiltViewModel
class PollPagerScreenViewModel @Inject constructor(
    private val pollQuestionUseCase: PollQuestionUseCase
): ViewModel(){

    private val pollQuestionMutableLiveData = MutableLiveData<RemoteApiResult<ApiResponse<ArrayList<PollResponseInfo>>>>()
    val pollMutableLiveData: LiveData<RemoteApiResult<ApiResponse<ArrayList<PollResponseInfo>>>> = pollQuestionMutableLiveData


    fun getPollQuestion(ageCategoryRequest: AgeCategoryRequest) = viewModelScope.launch {
        pollQuestionUseCase.getPollsByAgeCategory(ageCategoryRequest).collect{
            pollQuestionMutableLiveData.value = it
        }
    }


}
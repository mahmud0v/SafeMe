package safeme.uz.presentation.viewmodel.poll

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.PollAnswerRequest
import safeme.uz.data.remote.response.PollAnswerResponse
import safeme.uz.data.remote.response.PollDetailResponse
import safeme.uz.domain.usecase.GetPollByIdUseCase
import safeme.uz.domain.usecase.GivePollAnswerUseCase
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

@HiltViewModel
class PollDetailScreenViewModel @Inject constructor(
    private val getPollByIdUseCase: GetPollByIdUseCase,
    private val pollAnswerUseCase: GivePollAnswerUseCase
): ViewModel() {

    private val getPollByIdMutableLiveData = MutableLiveData<RemoteApiResult<ApiResponse<PollDetailResponse>>>()
    val getPollByIdLiveData:LiveData<RemoteApiResult<ApiResponse<PollDetailResponse>>> = getPollByIdMutableLiveData

    private val pollAnswerMutableLiveData = MutableLiveData<RemoteApiResult<ApiResponse<PollAnswerResponse>>>()
    val pollAnswerLiveData:LiveData<RemoteApiResult<ApiResponse<PollAnswerResponse>>> = pollAnswerMutableLiveData


    fun getPollById(id:Int) = viewModelScope.launch {
        getPollByIdUseCase.getPollById(id).collect{
            getPollByIdMutableLiveData.value = it
        }
    }

    fun giveAnswerPoll(pollAnswerRequest: PollAnswerRequest) = viewModelScope.launch {
        pollAnswerUseCase.givePollAnswer(pollAnswerRequest).collect{
            pollAnswerMutableLiveData.value = it
        }
    }




}
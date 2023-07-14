package safeme.uz.presentation.viewmodel.appeal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.AppealRequest
import safeme.uz.data.remote.response.AppealResponse
import safeme.uz.domain.usecase.GiveAppealUseCase
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

@HiltViewModel
class AppealScreenViewModel @Inject constructor(
    private val giveAppealUseCase: GiveAppealUseCase
) : ViewModel() {

    private val giveAppealMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<AppealResponse>>>()
    val giveAppealLiveData: LiveData<RemoteApiResult<ApiResponse<AppealResponse>>> =
        giveAppealMutableLiveData

    fun giveAppeal(appealRequest: AppealRequest) = viewModelScope.launch {
        giveAppealUseCase.giveAppeal(appealRequest).collect {
            giveAppealMutableLiveData.value = it
        }
    }



}
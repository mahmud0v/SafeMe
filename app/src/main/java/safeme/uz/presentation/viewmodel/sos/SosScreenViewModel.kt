package safeme.uz.presentation.viewmodel.sos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.SosBody
import safeme.uz.data.remote.request.SosRequest
import safeme.uz.domain.usecase.SosNotifiedUseCase
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

@HiltViewModel
class SosScreenViewModel @Inject constructor(
    private val sosNotifiedUseCase: SosNotifiedUseCase
) : ViewModel() {

    private val sosMutableLiveData = MutableLiveData<RemoteApiResult<ApiResponse<SosBody>>>()
    val sosLiveData: LiveData<RemoteApiResult<ApiResponse<SosBody>>> = sosMutableLiveData

    fun sosNotified(sosRequest: SosRequest) = viewModelScope.launch {
        sosNotifiedUseCase.sosNotified(sosRequest).collect {
            sosMutableLiveData.value = it
        }
    }

}
package safeme.uz.presentation.viewmodel.inspector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.InspectorMFYRequest
import safeme.uz.data.remote.response.InspectorInfo
import safeme.uz.data.remote.response.UserResponse
import safeme.uz.domain.usecase.GetInspectorDataUseCase
import safeme.uz.domain.usecase.ProfileUseCase
import safeme.uz.utils.AnnouncementResult
import javax.inject.Inject

@HiltViewModel
class InspectorScreenViewModel @Inject constructor(
    private val getInspectorDataUseCase: GetInspectorDataUseCase,
    private val profileUseCase: ProfileUseCase
) : ViewModel() {
    private val inspectorMutableLiveData =
        MutableLiveData<AnnouncementResult<ApiResponse<ArrayList<InspectorInfo>>>>()
    val inspectorLiveData: LiveData<AnnouncementResult<ApiResponse<ArrayList<InspectorInfo>>>> =
        inspectorMutableLiveData

    private val userMutableLiveData = MutableLiveData<AnnouncementResult<UserResponse>>()
    val userLiveData: LiveData<AnnouncementResult<UserResponse>> = userMutableLiveData

    fun getInspectorDataByMFY(inspectorMFYRequest: InspectorMFYRequest) = viewModelScope.launch {
        getInspectorDataUseCase.getInspectorDataByMFY(inspectorMFYRequest).collect {
            inspectorMutableLiveData.value = it
        }
    }

    fun getUserData() = viewModelScope.launch {
        profileUseCase.getUserInfo().collect {
            userMutableLiveData.value = it
        }
    }

}
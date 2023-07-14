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
import safeme.uz.domain.usecase.GetInspectorDataUseCase
import safeme.uz.domain.usecase.ProfileUseCase
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

@HiltViewModel
class InspectorScreenViewModel @Inject constructor(
    private val getInspectorDataUseCase: GetInspectorDataUseCase,
    private val profileUseCase: ProfileUseCase
) : ViewModel() {
    private val inspectorMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<ArrayList<InspectorInfo>>>>()
    val inspectorLiveData: LiveData<RemoteApiResult<ApiResponse<ArrayList<InspectorInfo>>>> =
        inspectorMutableLiveData



    fun getInspectorDataByMFY() = viewModelScope.launch {
        getInspectorDataUseCase.getInspectorDataByMFY().collect {
            inspectorMutableLiveData.value = it
        }
    }



}
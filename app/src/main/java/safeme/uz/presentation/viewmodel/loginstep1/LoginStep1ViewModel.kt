package safeme.uz.presentation.viewmodel.loginstep1

import androidx.lifecycle.LiveData
import safeme.uz.data.remote.request.AddingChildDataRequest
import safeme.uz.data.remote.response.AddingChildDataResponse

interface LoginStep1ViewModel {
    val errorLiveData: LiveData<Int>
    val progressLiveData: LiveData<Boolean>
    val messageLiveData: LiveData<String>
    val openLoginStep2ScreenLiveData: LiveData<AddingChildDataResponse>

    fun addingDataAboutChild(addingChildDataRequest: AddingChildDataRequest)
}
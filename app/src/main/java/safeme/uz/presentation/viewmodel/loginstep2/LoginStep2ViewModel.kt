package safeme.uz.presentation.viewmodel.loginstep2

import androidx.lifecycle.LiveData
import safeme.uz.data.remote.request.RegisterRequest

interface LoginStep2ViewModel {
    val errorLiveData: LiveData<Int>
    val progressLiveData: LiveData<Boolean>
    val messageLiveData: LiveData<String>
    val openVerifyScreenLiveData: LiveData<String>

    fun register(registerRequest: RegisterRequest)
}
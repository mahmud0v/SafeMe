package safeme.uz.presentation.viewmodel.login

import androidx.lifecycle.LiveData
import safeme.uz.data.remote.request.LoginRequest
import safeme.uz.data.remote.response.LoginResponse

interface LoginViewModel {
    val errorLiveData: LiveData<Int>
    val progressLiveData: LiveData<Boolean>
    val messageLiveData: LiveData<String>
    val openMainScreenLiveData: LiveData<LoginResponse>

    fun login(loginRequest: LoginRequest)
}
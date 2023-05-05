package safeme.uz.presentation.viewmodel.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import safeme.uz.data.model.*
import safeme.uz.data.remote.request.LoginRequest
import safeme.uz.data.remote.response.LoginResponse
import safeme.uz.domain.usecase.LoginUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel(), LoginViewModel {

    override val errorLiveData = MutableLiveData<Int>()
    override val messageLiveData = MutableLiveData<String>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val openMainScreenLiveData = MutableLiveData<LoginResponse>()

    override fun login(loginRequest: LoginRequest) {
        progressLiveData.value = true
        loginUseCase.invoke(loginRequest).onEach {
            progressLiveData.value = false

            it.onSuccess {
                val data = it.asSuccess.data
                openMainScreenLiveData.value = data
            }

            it.onResource {
                val error = it.asResource.resourceId
                errorLiveData.value = error
            }
            it.onText {
                val error = it.asText.message
                messageLiveData.value = error
            }
        }.launchIn(viewModelScope)
    }
}
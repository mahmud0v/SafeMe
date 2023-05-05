package safeme.uz.presentation.viewmodel.profileInfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import safeme.uz.data.model.*
import safeme.uz.data.remote.request.UserDataRequest
import safeme.uz.data.remote.response.UserDataResponse
import safeme.uz.domain.usecase.SendUserDataUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileInfoViewModelImpl @Inject constructor(
    private val sendUserDataUseCase: SendUserDataUseCase
) : ViewModel(), ProfileInfoViewModel {
    override val errorLiveData = MutableLiveData<Int>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val messageLiveData = MutableLiveData<String>()
    override val openLoginStep1ScreenLiveData = MutableLiveData<UserDataResponse>()

    override fun sendUserData(userDataRequest: UserDataRequest) {
        progressLiveData.value = true
        sendUserDataUseCase.invoke(userDataRequest).onEach {
            progressLiveData.value = false

            it.onSuccess {
                val data = it.asSuccess.data
                openLoginStep1ScreenLiveData.value = data
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
package safeme.uz.presentation.viewmodel.resetpassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import safeme.uz.data.model.*
import safeme.uz.data.remote.request.ResetPasswordRequest
import safeme.uz.domain.usecase.ResetPasswordUseCase
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModelImpl @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
) : ViewModel(), ResetPasswordViewModel {
    override val errorLiveData = MutableLiveData<Int>()
    override val messageLiveData = MutableLiveData<String>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val openLoginScreenLiveData = MutableLiveData<Unit>()

    override fun openLoginScreen(resetPasswordRequest: ResetPasswordRequest) {
        progressLiveData.value = true
        resetPasswordUseCase.resetPassword(resetPasswordRequest).onEach {
            progressLiveData.value = false
            it.onSuccess {
                val data = it.asSuccess.data
                if (data) openLoginScreenLiveData.value = Unit
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
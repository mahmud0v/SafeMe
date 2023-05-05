package safeme.uz.presentation.viewmodel.resetusername

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import safeme.uz.data.model.*
import safeme.uz.domain.usecase.ForgetPasswordUseCase
import javax.inject.Inject

@HiltViewModel
class ResetUserNameViewModelImpl @Inject constructor(
    private val forgetPasswordUseCase: ForgetPasswordUseCase
) : ViewModel(), ResetUserNameViewModel {
    override val errorLiveData = MutableLiveData<Int>()
    override val messageLiveData = MutableLiveData<String>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val openVerifyScreenLiveData = MutableLiveData<VerifyModel>()

    override fun openVerifyScreen(model: VerifyModel) {
        progressLiveData.value = true
        forgetPasswordUseCase.getVerificationCode(model.phoneNumber).onEach {
            progressLiveData.value = false
            it.onSuccess {
                val data = it.asSuccess.data
                if (data) openVerifyScreenLiveData.value = model
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
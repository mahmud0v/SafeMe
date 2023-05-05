package safeme.uz.presentation.viewmodel.pincode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import safeme.uz.data.model.*
import safeme.uz.data.remote.response.ResetPinCodeResponse
import safeme.uz.domain.usecase.PinUseCase
import safeme.uz.domain.usecase.ResetPinCodeUseCase
import javax.inject.Inject

@HiltViewModel
class PinCodeVIewModelImpl @Inject constructor(
    private val useCase: PinUseCase,
    private val resetPinUseCase: ResetPinCodeUseCase,
) : ViewModel(), PinCodeViewModel {
    override val errorLiveData = MutableLiveData<Int>()
    override val messageLiveData = MutableLiveData<String>()
    override val checkPinLiveData = MutableLiveData<String>()
    override val setPinLiveData = MutableLiveData<Int>()
    override val saveNewPinLiveData = MutableLiveData<Boolean>()
    override val getCurrentPinLiveData = MutableLiveData<String>()
    override val resetPinCodeLiveData = MutableLiveData<ResetPinCodeResponse>()

    override fun resetPinCode() {
        resetPinUseCase.resetPinCode().onEach {
            it.onSuccess {
                val data = it.asSuccess.data
                resetPinCodeLiveData.value = data
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

    override fun check(pin: String) {
        setPinLiveData.value = pin.length
        if (pin.length == 4) {
            checkPinLiveData.value = pin
        }
    }

    override fun saveNewPin(pin: String) {
        val saveNewPin = useCase.saveNewPin(pin)
        saveNewPinLiveData.value = saveNewPin
    }

    override fun getCurrentPin() {
        getCurrentPinLiveData.value = useCase.getCurrentPin()
    }
}
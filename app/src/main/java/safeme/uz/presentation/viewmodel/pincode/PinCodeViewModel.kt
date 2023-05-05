package safeme.uz.presentation.viewmodel.pincode

import androidx.lifecycle.LiveData
import safeme.uz.data.remote.response.ResetPinCodeResponse

interface PinCodeViewModel {
    val errorLiveData: LiveData<Int>
    val messageLiveData: LiveData<String>
    val setPinLiveData: LiveData<Int>
    val checkPinLiveData: LiveData<String>
    val saveNewPinLiveData: LiveData<Boolean>
    val getCurrentPinLiveData: LiveData<String>
    val resetPinCodeLiveData : LiveData<ResetPinCodeResponse>

    fun check(pin : String)
    fun saveNewPin(pin:String)
    fun getCurrentPin()
    fun resetPinCode()
}
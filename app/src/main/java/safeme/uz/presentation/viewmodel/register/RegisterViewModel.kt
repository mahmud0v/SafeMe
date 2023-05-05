package safeme.uz.presentation.viewmodel.register

import androidx.lifecycle.LiveData
import safeme.uz.data.model.VerifyModel

interface RegisterViewModel {
    val errorLiveData: LiveData<Int>
    val progressLiveData: LiveData<Boolean>
    val messageLiveData: LiveData<String>
    val openVerifyScreenLiveData: LiveData<VerifyModel>

    fun register(registerRequest: VerifyModel)
}
package safeme.uz.presentation.viewmodel.resetusername

import androidx.lifecycle.LiveData
import safeme.uz.data.model.VerifyModel

interface ResetUserNameViewModel {

    val errorLiveData: LiveData<Int>
    val messageLiveData: LiveData<String>
    val progressLiveData: LiveData<Boolean>
    val openVerifyScreenLiveData: LiveData<VerifyModel>

    fun openVerifyScreen(model : VerifyModel)
}
package safeme.uz.presentation.viewmodel.resetpassword

import androidx.lifecycle.LiveData
import safeme.uz.data.remote.request.ResetPasswordRequest

interface ResetPasswordViewModel {
    val errorLiveData: LiveData<Int>
    val messageLiveData: LiveData<String>
    val progressLiveData: LiveData<Boolean>
    val openLoginScreenLiveData: LiveData<Unit>

    fun openLoginScreen(resetPasswordRequest: ResetPasswordRequest)
}
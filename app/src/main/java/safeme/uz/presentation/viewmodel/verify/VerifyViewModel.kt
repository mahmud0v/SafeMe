package safeme.uz.presentation.viewmodel.verify

import androidx.lifecycle.LiveData
import safeme.uz.data.model.VerifyModel
import safeme.uz.data.remote.request.RegisterRequest
import safeme.uz.data.remote.request.VerifyRegisterRequest
import safeme.uz.data.remote.response.VerifyRegisterResponse
import safeme.uz.data.remote.response.VerifyResetPasswordResponse

interface VerifyViewModel {
    val errorLiveData: LiveData<Int>
    val messageLiveData: LiveData<String>
    val progressLiveData: LiveData<Boolean>
    val openProfileScreenLiveData: LiveData<VerifyRegisterResponse>
    val openResetPasswordScreenLiveData: LiveData<VerifyResetPasswordResponse>
    val openPinScreenLiveData: LiveData<Unit>
    val resendSmsCodeLiveData: LiveData<Boolean>
    val timeLiveData : LiveData<Long>
    val timeStatus : LiveData<Boolean>

    fun verifyCodeForPassword(verification_code: String)
    fun verifyCodeRegister(verifyRegisterRequest: VerifyRegisterRequest)
    fun verifyCodeForPin(verification_code: String)

    fun resendCodeForPassword(phoneNumber: String?)
    fun resendCodeForRegister(registerRequest: VerifyModel)
    fun resendCodeForPinCode()
}
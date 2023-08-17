package safeme.uz.presentation.viewmodel.verify

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import safeme.uz.data.model.ManageScreen
import safeme.uz.data.model.VerifyModel
import safeme.uz.data.model.asResource
import safeme.uz.data.model.asSuccess
import safeme.uz.data.model.asText
import safeme.uz.data.model.onResource
import safeme.uz.data.model.onSuccess
import safeme.uz.data.model.onText
import safeme.uz.data.remote.request.VerifyRegisterRequest
import safeme.uz.data.remote.response.VerifyRegisterResponse
import safeme.uz.data.remote.response.VerifyResetPasswordResponse
import safeme.uz.domain.usecase.ForgetPasswordUseCase
import safeme.uz.domain.usecase.RegisterUseCase
import safeme.uz.domain.usecase.ResetPinCodeUseCase
import safeme.uz.domain.usecase.VerifyPinCodeUseCase
import safeme.uz.domain.usecase.VerifyRegisterUseCase
import safeme.uz.domain.usecase.VerifyResetPasswordUseCase
import safeme.uz.utils.Keys
import javax.inject.Inject

@HiltViewModel
class VerifyViewModelImpl @Inject constructor(
    private val verifyRegisterUseCase: VerifyRegisterUseCase,
    private val verifyResetPasswordUseCase: VerifyResetPasswordUseCase,
    private val verifyPinCodeUseCase: VerifyPinCodeUseCase,
    private val registerUseCase: RegisterUseCase,
    private val forgetPasswordUseCase: ForgetPasswordUseCase,
    private val resetPinUseCase: ResetPinCodeUseCase,
) : ViewModel(), VerifyViewModel {
    override val errorLiveData = MutableLiveData<Int>()
    override val messageLiveData = MutableLiveData<String>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val openProfileScreenLiveData = MutableLiveData<VerifyRegisterResponse>()
    override val openResetPasswordScreenLiveData = MutableLiveData<VerifyResetPasswordResponse>()
    override val openPinScreenLiveData = MutableLiveData<Unit>()
    override val resendSmsCodeLiveData = MutableLiveData<Boolean>()

    override val timeLiveData = MutableLiveData<Long>()
    override val timeStatus = MutableLiveData<Boolean>()

    private val countDownTimer: CountDownTimer
    private val timerMillisInFuture = 2 * 60000L
    private val timerCountDownInterval = 1000L

    init {
        countDownTimer = object : CountDownTimer(timerMillisInFuture, timerCountDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLiveData.value = millisUntilFinished / timerCountDownInterval
            }

            override fun onFinish() {
                resendSmsCodeLiveData.value = true
                timeStatus.value = false
            }
        }
        countDownTimer.start()
    }

    override fun verifyCodeRegister(verifyRegisterRequest: VerifyRegisterRequest) {
        progressLiveData.value = true
        verifyRegisterUseCase.invoke(verifyRegisterRequest).onEach {
            progressLiveData.value = false
            it.onSuccess {
                val data = it.asSuccess.data
                openProfileScreenLiveData.value = data.body!!
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

    override fun verifyCodeForPassword(verification_code: String, manageScreen: ManageScreen?) {
        progressLiveData.value = true
        verifyResetPasswordUseCase.invoke(verification_code).onEach {
            progressLiveData.value = false
            it.onSuccess {
                if (manageScreen?.secondaryScreen == Keys.PROFILE_TO_EDIT) {
                    openResetPasswordScreenLiveData.value =
                        VerifyResetPasswordResponse(Keys.PROFILE_TO_EDIT)
                } else {
                    val data = it.asSuccess.data
                    openResetPasswordScreenLiveData.value = data!!
                }
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

    override fun verifyCodeForPin(verification_code: String) {
        progressLiveData.value = true
        verifyPinCodeUseCase.invoke(verification_code).onEach {
            progressLiveData.value = false
            it.onSuccess {
                openPinScreenLiveData.value = Unit
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

    override fun resendCodeForPassword(phoneNumber: String?) {
        progressLiveData.value = true
        forgetPasswordUseCase.getVerificationCode(phoneNumber).onEach {
            progressLiveData.value = false
            it.onSuccess {
                countDownTimer.cancel()
                countDownTimer.start()
                timeStatus.value = true
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

    override fun resendCodeForRegister(registerRequest: VerifyModel) {
        progressLiveData.value = true
        registerUseCase.invoke(registerRequest).onEach {
            progressLiveData.value = false
            it.onSuccess {
                countDownTimer.cancel()
                countDownTimer.start()
                timeStatus.value = true
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

    override fun resendCodeForPinCode() {
        progressLiveData.value = true
        resetPinUseCase.resetPinCode().onEach {
            progressLiveData.value = false
            it.onSuccess {
                countDownTimer.cancel()
                countDownTimer.start()
                timeStatus.value = true
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
package safeme.uz.presentation.viewmodel.profileInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.PasswordRecoverRequest
import safeme.uz.data.remote.request.RemindChangePasswordRequest
import safeme.uz.data.remote.request.ResetPasswordRequest
import safeme.uz.data.remote.request.VerifyRegisterRequest
import safeme.uz.data.remote.response.PasswordRecoverResponse
import safeme.uz.data.remote.response.PasswordUpdateBody
import safeme.uz.data.remote.response.RemindPasswordChangeBody
import safeme.uz.data.remote.response.UserResponse
import safeme.uz.domain.usecase.ProfileUseCase
import safeme.uz.utils.AnnouncementResult
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
) : ViewModel() {

    private val userInfoMutableLiveData = MutableLiveData<AnnouncementResult<UserResponse>>()
    val userInfoLiveData: LiveData<AnnouncementResult<UserResponse>> = userInfoMutableLiveData

    private val passwordRecoverMutableLiveData =
        MutableLiveData<AnnouncementResult<PasswordRecoverResponse>>()
    val passwordRecoverLiveData: LiveData<AnnouncementResult<PasswordRecoverResponse>> =
        passwordRecoverMutableLiveData

    private val passwordVerificationMutableLiveData =
        MutableLiveData<AnnouncementResult<PasswordRecoverResponse>>()
    val passwordVerificationLiveData: LiveData<AnnouncementResult<PasswordRecoverResponse>> =
        passwordVerificationMutableLiveData

    private val passwordUpdateMutableLiveData =
        MutableLiveData<AnnouncementResult<ApiResponse<PasswordUpdateBody>>>()
    val passwordUpdateLiveData: LiveData<AnnouncementResult<ApiResponse<PasswordUpdateBody>>> =
        passwordUpdateMutableLiveData

    private val remindChangePasswordMutableLiveData = MutableLiveData<AnnouncementResult<ApiResponse<RemindPasswordChangeBody>>>()
    val remindChangePasswordLiveData:LiveData<AnnouncementResult<ApiResponse<RemindPasswordChangeBody>>> = remindChangePasswordMutableLiveData



    fun getProfileInfo() = viewModelScope.launch {
        profileUseCase.getUserInfo().collect {
            userInfoMutableLiveData.value = it
        }

    }

    fun passwordRecover(passwordRecoverRequest: PasswordRecoverRequest) = viewModelScope.launch {
        profileUseCase.passwordRecover(passwordRecoverRequest).collect {
            passwordRecoverMutableLiveData.value = it
        }
    }

    fun passwordVerification(verifyRegisterRequest: VerifyRegisterRequest) = viewModelScope.launch {
        profileUseCase.passwordVerification(verifyRegisterRequest).collect {
            passwordVerificationMutableLiveData.value = it
        }
    }

    fun passwordUpdate(resetPasswordRequest: ResetPasswordRequest) = viewModelScope.launch {
        profileUseCase.passwordUpdate(resetPasswordRequest).collect {
            passwordUpdateMutableLiveData.value = it
        }
    }

    fun remindPasswordChange(remindChangePasswordRequest: RemindChangePasswordRequest) = viewModelScope.launch {
        profileUseCase.remindPasswordChange(remindChangePasswordRequest).collect{
            remindChangePasswordMutableLiveData.value = it
        }
    }


}
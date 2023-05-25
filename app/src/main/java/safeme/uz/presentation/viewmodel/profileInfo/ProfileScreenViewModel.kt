package safeme.uz.presentation.viewmodel.profileInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import safeme.uz.data.remote.response.UserResponse
import safeme.uz.domain.usecase.ProfileUseCase
import safeme.uz.domain.usecase.impl.ProfileUseCaseImpl
import safeme.uz.utils.AnnouncementResult
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
) : ViewModel() {
    private val userInfoMutableLiveData = MutableLiveData<AnnouncementResult<UserResponse>>()
    val userInfoLiveData: LiveData<AnnouncementResult<UserResponse>> = userInfoMutableLiveData

    fun getProfileInfo() = viewModelScope.launch {
        profileUseCase.getUserInfo().collect {
            userInfoMutableLiveData.value = it
        }

    }
}
package safeme.uz.presentation.viewmodel.profileInfo

import androidx.lifecycle.LiveData
import safeme.uz.data.remote.request.UserDataRequest
import safeme.uz.data.remote.response.UserDataResponse

interface ProfileInfoViewModel {
    val errorLiveData: LiveData<Int>
    val progressLiveData: LiveData<Boolean>
    val messageLiveData: LiveData<String>
    val openLoginStep1ScreenLiveData: LiveData<UserDataResponse>

    fun sendUserData(userDataRequest: UserDataRequest)
}
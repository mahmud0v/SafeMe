package safeme.uz.presentation.viewmodel.splash

import androidx.lifecycle.LiveData

interface SplashViewModel {
    val openNextScreenLiveData: LiveData<String>
}
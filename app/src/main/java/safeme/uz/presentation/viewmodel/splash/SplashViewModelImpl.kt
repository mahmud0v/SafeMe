package safeme.uz.presentation.viewmodel.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import safeme.uz.domain.usecase.GetTokenUseCase
import safeme.uz.domain.usecase.PinUseCase
import safeme.uz.presentation.viewmodel.register.RegisterViewModel
import safeme.uz.utils.Keys
import javax.inject.Inject

@HiltViewModel
class SplashViewModelImpl @Inject constructor(
    private val pinUseCase: PinUseCase,
    private val tokenUseCase: GetTokenUseCase
) : ViewModel(), SplashViewModel {
    override val openNextScreenLiveData = MutableLiveData<String>()

    init {
        if (tokenUseCase.invoke()) {
            viewModelScope.launch(Dispatchers.IO) {
                delay(2000)
                openNextScreenLiveData.postValue(Keys.OPEN_LOGIN)
            }
        } else if (pinUseCase.hasPinCode()) {
            viewModelScope.launch(Dispatchers.IO) {
                delay(2000)
                openNextScreenLiveData.postValue(Keys.PIN_OPEN)
            }
        }
        else {
            viewModelScope.launch(Dispatchers.IO) {
                delay(2000)
                openNextScreenLiveData.postValue(Keys.PIN_CREATE_AFTER_LOGIN)
            }
        }
    }
}
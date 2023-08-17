package safeme.uz.presentation.viewmodel.splash

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import safeme.uz.R
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.domain.usecase.GetTokenUseCase
import safeme.uz.domain.usecase.PinUseCase
import safeme.uz.domain.usecase.TokenUpdateUseCase
import safeme.uz.presentation.viewmodel.register.RegisterViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

@HiltViewModel
class SplashViewModelImpl @Inject constructor(
    private val pinUseCase: PinUseCase,
    private val tokenUseCase: GetTokenUseCase,
    private val tokenUpdateUseCase: TokenUpdateUseCase,
    private val appSharedPreference: AppSharedPreference,
    private val application: Application
) : ViewModel(), SplashViewModel {
    override val openNextScreenLiveData = MutableLiveData<String>()

    init {

        if (appSharedPreference.languageSaved) {
            runBlocking {
                viewModelScope.launch {
                    tokenUpdateUseCase.accessTokenUpdate().collect {
                        when (it) {
                            is RemoteApiResult.Success -> {
                                it.data?.body?.token?.let {
                                    appSharedPreference.token = it
                                }
                                it.data?.body?.refresh?.let {
                                    appSharedPreference.refresh = it
                                }
                            }

                            is RemoteApiResult.Error -> {
                                if (it.message == application.getString(R.string.unauthorized)) {
                                    openNextScreenLiveData.postValue(Keys.OPEN_LOGIN)
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }


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
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    delay(2000)
                    openNextScreenLiveData.postValue(Keys.PIN_CREATE_AFTER_LOGIN)
                }
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                delay(2000)
                openNextScreenLiveData.postValue(Keys.LANG_OPEN)
            }
        }
    }

}
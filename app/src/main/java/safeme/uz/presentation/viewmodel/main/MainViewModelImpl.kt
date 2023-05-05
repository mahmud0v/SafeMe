package safeme.uz.presentation.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import safeme.uz.data.model.*
import safeme.uz.domain.LogOutUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModelImpl @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
) : ViewModel(), MainViewModel {
    override val errorLiveData = MutableLiveData<Int>()
    override val messageLiveData = MutableLiveData<String>()
    override val logOutLiveData = MutableLiveData<Unit>()

    override fun logOut() {
        logOutUseCase.invoke().onEach {
            it.onSuccess {
                logOutLiveData.value = Unit
            }

            it.onText {
                val text = it.asText.message
                messageLiveData.value = text
            }

            it.onResource {
                val text = it.asResource.resourceId
                errorLiveData.value = text
            }
        }.launchIn(viewModelScope)
    }
}
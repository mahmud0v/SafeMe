package safeme.uz.presentation.viewmodel.loginstep1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import safeme.uz.data.model.*
import safeme.uz.data.remote.request.AddingChildDataRequest
import safeme.uz.data.remote.response.AddingChildDataResponse
import safeme.uz.domain.usecase.AddingChildDataUseCase
import javax.inject.Inject

@HiltViewModel
class LoginStep1ViewModelImpl @Inject constructor(
    private val addingChildDataUseCase: AddingChildDataUseCase
) : ViewModel(), LoginStep1ViewModel {
    override val errorLiveData = MutableLiveData<Int>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val messageLiveData = MutableLiveData<String>()
    override val openLoginStep2ScreenLiveData = MutableLiveData<AddingChildDataResponse>()

    override fun addingDataAboutChild(addingChildDataRequest: AddingChildDataRequest) {
        progressLiveData.value = true
        addingChildDataUseCase.invoke(addingChildDataRequest).onEach {
            progressLiveData.value = false

            it.onSuccess {
                val data = it.asSuccess.data
                openLoginStep2ScreenLiveData.value = data
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
package safeme.uz.presentation.viewmodel.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import safeme.uz.data.model.*
import safeme.uz.domain.usecase.RegisterUseCase
import javax.inject.Inject

@HiltViewModel
class RegisterViewModelImpl @Inject constructor(
    private val registerUseCase: RegisterUseCase,
) : ViewModel(), RegisterViewModel {
    override val errorLiveData = MutableLiveData<Int>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val messageLiveData = MutableLiveData<String>()
    override val openVerifyScreenLiveData = MutableLiveData<VerifyModel>()

    override fun register(registerRequest: VerifyModel) {
        progressLiveData.value = true
        registerUseCase.invoke(registerRequest).onEach {
            progressLiveData.value = false

            it.onSuccess {
                val data = it.asSuccess.data
                openVerifyScreenLiveData.value = data
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
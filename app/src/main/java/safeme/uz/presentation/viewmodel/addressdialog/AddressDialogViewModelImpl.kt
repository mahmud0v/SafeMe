package safeme.uz.presentation.viewmodel.addressdialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import safeme.uz.data.model.*
import safeme.uz.data.remote.response.Address
import safeme.uz.data.remote.response.AddressResponse
import safeme.uz.domain.usecase.*
import javax.inject.Inject

@HiltViewModel
class AddressDialogViewModelImpl @Inject constructor(
    private val regionListUseCase: GetRegionsUseCase,
    private val districtsUseCase: GetDistrictsUseCase,
    private val mfYsUseCase: GetMFYsUseCase,
    private val getDistrictsByIdUseCase: GetDistrictsByIdUseCase,
    private val getMFYsUseCase: GetMFYsByIdUseCase,
) : ViewModel(), AddressDialogViewModel {
    override val errorLiveData = MutableLiveData<Int>()
    override val progressBarLiveData = MutableLiveData<Boolean>()
    override val messageLiveData = MutableLiveData<String>()
    override val getRegionsLiveData = MutableLiveData<AddressResponse>()
    override val getDistrictsLiveData = MutableLiveData<AddressResponse>()
    override val getMFYBysLiveData = MutableLiveData<AddressResponse>()
    override val getDistrictsByIdLiveData = MutableLiveData<List<Address>>()
    override val getMFYsByIdLiveData = MutableLiveData<List<Address>>()

    override fun getRegions() {
        progressBarLiveData.value = true
        regionListUseCase.invoke().onEach {
            progressBarLiveData.value = false
            it.onSuccess {
                val data = it.asSuccess.data
                getRegionsLiveData.value = data
            }
            it.onText {
                messageLiveData.value = it.asText.message
            }
            it.onResource {
                errorLiveData.value = it.asResource.resourceId
            }
        }.launchIn(viewModelScope)
    }

    override fun getDistricts() {
        progressBarLiveData.value = true
        districtsUseCase.invoke().onEach {
            progressBarLiveData.value = false
            it.onSuccess {
                val data = it.asSuccess.data
                getDistrictsLiveData.value = data
            }
            it.onText {
                messageLiveData.value = it.asText.message
            }
            it.onResource {
                errorLiveData.value = it.asResource.resourceId
            }
        }.launchIn(viewModelScope)
    }

    override fun getMFYs() {
        progressBarLiveData.value = true
        mfYsUseCase.invoke().onEach {
            progressBarLiveData.value = false
            it.onSuccess {
                val data = it.asSuccess.data
                getMFYBysLiveData.value = data
            }
            it.onText {
                messageLiveData.value = it.asText.message
            }
            it.onResource {
                errorLiveData.value = it.asResource.resourceId
            }
        }.launchIn(viewModelScope)
    }

    override fun getDistrictsById(regionId: Int) {
        progressBarLiveData.value = true
        getDistrictsByIdUseCase.invoke(regionId).onEach {
            progressBarLiveData.value = false
            it.onSuccess {
                val data = it.asSuccess.data
                getDistrictsByIdLiveData.value = data
            }
            it.onText {
                messageLiveData.value = it.asText.message
            }
            it.onResource {
                errorLiveData.value = it.asResource.resourceId
            }
        }.launchIn(viewModelScope)
    }

    override fun getMFYsById(districtId: Int) {
        progressBarLiveData.value = true
        getMFYsUseCase.invoke(districtId).onEach {
            progressBarLiveData.value = false
            it.onSuccess {
                val data = it.asSuccess.data
                getMFYsByIdLiveData.value = data
            }
            it.onText {
                messageLiveData.value = it.asText.message
            }
            it.onResource {
                errorLiveData.value = it.asResource.resourceId
            }
        }.launchIn(viewModelScope)
    }

}
package safeme.uz.presentation.viewmodel.addressdialog

import androidx.lifecycle.LiveData
import safeme.uz.data.remote.response.Address
import safeme.uz.data.remote.response.AddressResponse

interface AddressDialogViewModel {
    val progressBarLiveData: LiveData<Boolean>
    val errorLiveData: LiveData<Int>
    val messageLiveData: LiveData<String>
    val getRegionsLiveData: LiveData<AddressResponse>
    val getDistrictsLiveData: LiveData<AddressResponse>
    val getMFYBysLiveData: LiveData<AddressResponse>
    val getDistrictsByIdLiveData: LiveData<List<Address>>
    val getMFYsByIdLiveData: LiveData<List<Address>>

    fun getRegions()
    fun getDistricts()
    fun getMFYs()
    fun getDistrictsById(regionId: Int)
    fun getMFYsById(districtId: Int)
}
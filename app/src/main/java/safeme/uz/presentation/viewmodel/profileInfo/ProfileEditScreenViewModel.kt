package safeme.uz.presentation.viewmodel.profileInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.DistrictByIdRequest
import safeme.uz.data.remote.request.NeighborhoodRequest
import safeme.uz.data.remote.request.UserUpdateRequest
import safeme.uz.data.remote.response.DistrictInfo
import safeme.uz.data.remote.response.NeighborhoodInfo
import safeme.uz.data.remote.response.RegionInfo
import safeme.uz.data.remote.response.UserUpdateResponse
import safeme.uz.domain.usecase.GetDistrictsByIdUseCase
import safeme.uz.domain.usecase.GetMFYsByIdUseCase
import safeme.uz.domain.usecase.GetRegionsUseCase
import safeme.uz.domain.usecase.UserUpdateUseCase
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

@HiltViewModel
class ProfileEditScreenViewModel @Inject constructor(
    private val getRegionsUseCase: GetRegionsUseCase,
    private val districtsByIdUseCase: GetDistrictsByIdUseCase,
    private val getMFYsByIdUseCase: GetMFYsByIdUseCase,
    private val userUpdateUseCase: UserUpdateUseCase
) : ViewModel() {
    private val regionsDataMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<ArrayList<RegionInfo>>>>()
    val regionsDataLiveData: LiveData<RemoteApiResult<ApiResponse<ArrayList<RegionInfo>>>> =
        regionsDataMutableLiveData

    private val districtByRegionMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<ArrayList<DistrictInfo>>>>()
    val districtByRegionLiveData: LiveData<RemoteApiResult<ApiResponse<ArrayList<DistrictInfo>>>> =
        districtByRegionMutableLiveData

    private val getMFYByDistrictMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<ArrayList<NeighborhoodInfo>>>>()
    val getMFYByDistrictLiveData: LiveData<RemoteApiResult<ApiResponse<ArrayList<NeighborhoodInfo>>>> =
        getMFYByDistrictMutableLiveData

    private val userUpdateMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<UserUpdateResponse>>>()
    val userUpdateLiveData: LiveData<RemoteApiResult<ApiResponse<UserUpdateResponse>>> =
        userUpdateMutableLiveData


    fun getRegions() = viewModelScope.launch {
        getRegionsUseCase.getRegions().collect {
            regionsDataMutableLiveData.value = it
        }
    }

    fun getDistrictsById(districtByIdRequest: DistrictByIdRequest) = viewModelScope.launch {
        districtsByIdUseCase.getDistrictsByRegion(districtByIdRequest).collect {
            districtByRegionMutableLiveData.value = it
        }
    }

    fun getMFYByDistrict(neighborhoodRequest: NeighborhoodRequest) = viewModelScope.launch {
        getMFYsByIdUseCase.getNeighborhoodByDistrict(neighborhoodRequest).collect {
            getMFYByDistrictMutableLiveData.value = it
        }
    }

    fun userUpdate(requestBody: MultipartBody) = viewModelScope.launch {
      userUpdateUseCase.userUpdate(requestBody).collect{
          userUpdateMutableLiveData.value = it
      }
    }


}
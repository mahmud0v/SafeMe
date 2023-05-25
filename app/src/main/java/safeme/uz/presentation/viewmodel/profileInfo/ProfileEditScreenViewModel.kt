package safeme.uz.presentation.viewmodel.profileInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
import safeme.uz.utils.AnnouncementResult
import javax.inject.Inject

@HiltViewModel
class ProfileEditScreenViewModel @Inject constructor(
    private val getRegionsUseCase: GetRegionsUseCase,
    private val districtsByIdUseCase: GetDistrictsByIdUseCase,
    private val getMFYsByIdUseCase: GetMFYsByIdUseCase,
    private val userUpdateUseCase: UserUpdateUseCase
) : ViewModel() {
    private val regionsDataMutableLiveData =
        MutableLiveData<AnnouncementResult<ApiResponse<ArrayList<RegionInfo>>>>()
    val regionsDataLiveData: LiveData<AnnouncementResult<ApiResponse<ArrayList<RegionInfo>>>> =
        regionsDataMutableLiveData

    private val districtByRegionMutableLiveData =
        MutableLiveData<AnnouncementResult<ApiResponse<ArrayList<DistrictInfo>>>>()
    val districtByRegionLiveData: LiveData<AnnouncementResult<ApiResponse<ArrayList<DistrictInfo>>>> =
        districtByRegionMutableLiveData

    private val getMFYByDistrictMutableLiveData =
        MutableLiveData<AnnouncementResult<ApiResponse<ArrayList<NeighborhoodInfo>>>>()
    val getMFYByDistrictLiveData: LiveData<AnnouncementResult<ApiResponse<ArrayList<NeighborhoodInfo>>>> =
        getMFYByDistrictMutableLiveData

    private val userUpdateMutableLiveData =
        MutableLiveData<AnnouncementResult<ApiResponse<UserUpdateResponse>>>()
    val userUpdateLiveData: LiveData<AnnouncementResult<ApiResponse<UserUpdateResponse>>> =
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

    fun userUpdate(userUpdateRequest: UserUpdateRequest) = viewModelScope.launch {
      userUpdateUseCase.userUpdate(userUpdateRequest).collect{
          userUpdateMutableLiveData.value = it
      }
    }


}
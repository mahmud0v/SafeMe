package safeme.uz.presentation.viewmodel.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.response.GameRecommendationResponse
import safeme.uz.domain.usecase.GetAllCategoriesUseCase
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

@HiltViewModel
class GameInfoScreenViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
) : ViewModel() {

    private val getGameByIdMutableLiveData =
        MutableLiveData<RemoteApiResult<ApiResponse<GameRecommendationResponse>>>()
    val getGameByIdLiveData: LiveData<RemoteApiResult<ApiResponse<GameRecommendationResponse>>> =
        getGameByIdMutableLiveData

    fun getGameById(id: Int) = viewModelScope.launch {
        getAllCategoriesUseCase.getGameById(id).collect {
            getGameByIdMutableLiveData.value = it
        }
    }

}
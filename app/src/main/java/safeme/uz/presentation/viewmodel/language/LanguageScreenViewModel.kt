package safeme.uz.presentation.viewmodel.language

import androidx.lifecycle.LiveData
import safeme.uz.data.remote.request.RefreshTokenRequest

interface LanguageScreenViewModel {

    val openNextScreenLiveData: LiveData<String>


}
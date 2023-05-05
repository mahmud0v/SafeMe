package safeme.uz.presentation.viewmodel.main

import androidx.lifecycle.LiveData

interface MainViewModel {
    val errorLiveData: LiveData<Int>
    val messageLiveData: LiveData<String>
    val logOutLiveData: LiveData<Unit>

    fun logOut()
}
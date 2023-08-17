package safeme.uz.presentation.viewmodel.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RemindListenerViewModel @Inject constructor(
) : ViewModel() {

    private val remindListenerMutableLiveData = MutableLiveData<Boolean>()
    val remindListenerLiveData: LiveData<Boolean> = remindListenerMutableLiveData

    private val drawerLabelMutableLiveData = MutableLiveData<Boolean>()
    val drawerLabelLiveData: LiveData<Boolean> = drawerLabelMutableLiveData


    fun remindInFragment(result: Boolean) {
        remindListenerMutableLiveData.value = result
    }

    fun labelRecommendation() {
        drawerLabelMutableLiveData.value = true
    }



}
package safeme.uz.presentation.ui.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.databinding.ScreenAboutUsBinding
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel

@AndroidEntryPoint
class AboutUsScreen : Fragment(R.layout.screen_about_us) {
    private val binding: ScreenAboutUsBinding by viewBinding()
    private val remindViewModel:RemindListenerViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        moveToProfile()
        drawerClickEvent()
    }

    private fun moveToProfile() {
        val action = AboutUsScreenDirections.actionAboutUsToProfileScreen()
        binding.ivProfile.setOnClickListener {
            findNavController().navigate(action)
        }
    }

    private fun drawerClickEvent(){
        binding.ivMenu.setOnClickListener {
            remindViewModel.remindInFragment(true)
        }
    }




}
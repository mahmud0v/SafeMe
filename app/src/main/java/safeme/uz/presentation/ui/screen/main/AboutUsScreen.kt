package safeme.uz.presentation.ui.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ManageScreen
import safeme.uz.databinding.ScreenAboutUsBinding
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.backPressDispatcher

@AndroidEntryPoint
class AboutUsScreen : Fragment(R.layout.screen_about_us) {
    private val binding: ScreenAboutUsBinding by viewBinding()
    private val remindViewModel:RemindListenerViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        moveToProfile()
        drawerClickEvent()
        backPressDispatcher()
    }

    private fun moveToProfile() {
        binding.ivProfile.setOnClickListener {
            val manageScreen = ManageScreen(Keys.ABOUT_SCREEN, Keys.PROFILE_TO_EDIT)
            val bundle = Bundle().apply {
                putSerializable(Keys.BUNDLE_KEY, manageScreen)
            }
            findNavController().navigate(R.id.action_about_us_to_profileScreen,bundle)
        }
    }

    private fun drawerClickEvent(){
        binding.ivMenu.setOnClickListener {
            remindViewModel.remindInFragment(true)
        }
    }




}
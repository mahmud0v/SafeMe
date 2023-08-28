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
import safeme.uz.databinding.SettingsScreenBinding
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.backPressDispatcher

@AndroidEntryPoint
class SettingsScreen : Fragment(R.layout.settings_screen) {
    private val binding: SettingsScreenBinding by viewBinding()
    private val remindViewModel: RemindListenerViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        moveToProfile()
        drawerClickEvent()
        moveToSOS()
        btnClick()
        backPressDispatcher()

    }

    private fun btnClick() {
        binding.langLayout.setOnClickListener {
           findNavController().navigate(R.id.action_settingsScreen_to_changeLangScreen)
        }

        binding.pinLayout.setOnClickListener {
            val bundle  = Bundle().apply {
                putString(Keys.PIN_BUNDLE_KEY,Keys.PIN_EDIT)
            }
            findNavController().navigate(R.id.pinCodeScreen,bundle)
        }

    }




    private fun moveToProfile() {
        binding.ivProfile.setOnClickListener {
            val manageScreen = ManageScreen(Keys.SETTINGS_SCREEN, Keys.PROFILE_TO_EDIT)
            val bundle = Bundle().apply {
                putSerializable(Keys.BUNDLE_KEY, manageScreen)
            }
            findNavController().navigate(R.id.profileScreen, bundle)
        }
    }

    private fun drawerClickEvent() {
        binding.ivMenu.setOnClickListener {
            remindViewModel.remindInFragment(true)
        }
    }

    private fun moveToSOS() {
        binding.ivSOS.setOnClickListener {
            val action = SettingsScreenDirections.actionSettingsScreenToSosScreen()
            findNavController().navigate(action)
        }

    }
}
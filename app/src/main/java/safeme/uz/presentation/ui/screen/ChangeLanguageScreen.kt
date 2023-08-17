package safeme.uz.presentation.ui.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.data.model.ManageScreen
import safeme.uz.databinding.ChangeLangScreenBinding
import safeme.uz.presentation.ui.screen.main.AboutUsScreenDirections
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.LocalHelper
import safeme.uz.utils.backPressDispatcher

@AndroidEntryPoint
class ChangeLanguageScreen : Fragment(R.layout.change_lang_screen) {
    private val binding: ChangeLangScreenBinding by viewBinding()
    private val appSharedPreference by lazy { AppSharedPreference(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        changeLanguage()
        backEvent()

    }

    private fun changeLanguage() {
        binding.uzbekLayout.setOnClickListener {
            appSharedPreference.locale = "uz"
            appSharedPreference.languageSaved = true
            LocalHelper.changeLanguage("uz-rUz",requireContext())
            findNavController().navigate(R.id.recommendations)
        }
        binding.uzbekKirilLayout.setOnClickListener {
            appSharedPreference.locale = "sr"
            appSharedPreference.languageSaved = true
            LocalHelper.changeLanguage("uz",requireContext())
            findNavController().navigate(R.id.recommendations)

        }
        binding.englishLayout.setOnClickListener {
            appSharedPreference.locale = "en"
            appSharedPreference.languageSaved = true
            LocalHelper.changeLanguage("en",requireContext())
            findNavController().navigate(R.id.recommendations)

        }
        binding.russianLayout.setOnClickListener {
            appSharedPreference.locale = "ru"
            appSharedPreference.languageSaved = true
            LocalHelper.changeLanguage("ru",requireContext())
            findNavController().navigate(R.id.recommendations)

        }
    }

    private fun backEvent(){
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }



}
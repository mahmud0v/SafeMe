package safeme.uz.presentation.ui.screen

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.databinding.SelectLangScreenBinding
import safeme.uz.presentation.viewmodel.language.LanguageScreenViewModel
import safeme.uz.presentation.viewmodel.language.LanguageScreenViewModelImpl
import safeme.uz.utils.Keys
import safeme.uz.utils.LocalHelper

@AndroidEntryPoint
class LanguageScreen : Fragment(R.layout.select_lang_screen){
    private val binding:SelectLangScreenBinding by viewBinding()
    private val viewModel:LanguageScreenViewModel by viewModels<LanguageScreenViewModelImpl>()
    private val appSharedPreference by lazy { AppSharedPreference(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       selectLanguage()
    }

    private fun selectLanguage(){
        binding.uzbekLayout.setOnClickListener {
            appSharedPreference.locale = "uz"
            appSharedPreference.languageSaved = true
            initObservers()
            LocalHelper.changeLanguage("uz-rUz",requireContext())

        }

        binding.uzbekKirilLayout.setOnClickListener {
            appSharedPreference.locale = "sr"
            appSharedPreference.languageSaved = true
            initObservers()
            LocalHelper.changeLanguage("uz",requireContext())
        }

        binding.russianLayout.setOnClickListener {
            appSharedPreference.locale = "ru"
            appSharedPreference.languageSaved = true
            initObservers()
            LocalHelper.changeLanguage("ru",requireContext())
        }

        binding.englishLayout.setOnClickListener {
            appSharedPreference.locale = "en"
            appSharedPreference.languageSaved = true
            initObservers()
            LocalHelper.changeLanguage("en",requireContext())
        }

    }


    private fun initObservers() = with(viewModel) {
        openNextScreenLiveData.observe(viewLifecycleOwner, openNextScreenObserver)
    }

    private val openNextScreenObserver = Observer<String> {
        when(it){

            Keys.OPEN_LOGIN ->  findNavController().navigate(
                R.id.action_languageScreen_to_loginScreen
            )

            Keys.PIN_OPEN -> findNavController().navigate(
                R.id.action_languageScreen_to_pinCodeScreen,
                bundleOf(Keys.PIN_BUNDLE_KEY to Keys.PIN_OPEN)
            )

            Keys.PIN_CREATE_AFTER_LOGIN -> findNavController().navigate(
                R.id.action_languageScreen_to_pinCodeScreen,
                bundleOf(Keys.PIN_BUNDLE_KEY to Keys.PIN_CREATE_AFTER_LOGIN)
            )


        }

    }
}
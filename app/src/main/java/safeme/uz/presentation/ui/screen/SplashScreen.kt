package safeme.uz.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.databinding.ScreenSplashBinding
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.viewmodel.splash.SplashViewModel
import safeme.uz.presentation.viewmodel.splash.SplashViewModelImpl
import safeme.uz.utils.Keys
import safeme.uz.utils.LocalHelper
import safeme.uz.utils.hideKeyboard
import safeme.uz.utils.isConnected

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.screen_splash) {
    private val binding:ScreenSplashBinding by viewBinding()
    private val viewModel: SplashViewModel by viewModels<SplashViewModelImpl>()
    private val appSharedPreference by lazy { AppSharedPreference(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isConnected()) {
            initObservers()
        } else {
           Snackbar.make(binding.imageId,getString(R.string.internet_not_connected),Snackbar.LENGTH_SHORT).show()
        }
        hideKeyboard()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initObservers() = with(viewModel) {
        openNextScreenLiveData.observe(this@SplashScreen, openNextScreenObserver)
    }



    private val openNextScreenObserver = Observer<String> {
        Log.d("PPP", it)

        when (it) {

            Keys.OPEN_LOGIN -> findNavController().navigate(
                R.id.action_splashScreen_to_loginScreen
            )

            Keys.PIN_OPEN -> findNavController().navigate(
                R.id.action_splashScreen_to_pinCodeScreen,
                bundleOf(Keys.PIN_BUNDLE_KEY to Keys.PIN_OPEN)
            )

            Keys.PIN_CREATE_AFTER_LOGIN -> findNavController().navigate(
                R.id.action_splashScreen_to_pinCodeScreen,
                bundleOf(Keys.PIN_BUNDLE_KEY to Keys.PIN_CREATE_AFTER_LOGIN)
            )

            Keys.LANG_OPEN -> findNavController().navigate(
                R.id.action_splashScreen_to_languageScreen
            )


        }

    }
}
package safeme.uz.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.presentation.viewmodel.splash.SplashViewModel
import safeme.uz.presentation.viewmodel.splash.SplashViewModelImpl
import safeme.uz.utils.Keys
import safeme.uz.utils.hideKeyboard

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.screen_splash) {
    private val viewModel: SplashViewModel by viewModels<SplashViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        hideKeyboard()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initObservers() = with(viewModel) {
        openNextScreenLiveData.observe(this@SplashScreen, openNextScreenObserver)
    }

    private val openNextScreenObserver = Observer<String> {
        if (it == Keys.OPEN_LOGIN) findNavController().navigate(
            R.id.action_splashScreen_to_loginScreen
        )

        if (it == Keys.PIN_OPEN) findNavController().navigate(
            R.id.action_splashScreen_to_pinCodeScreen,
            bundleOf(Keys.PIN_BUNDLE_KEY to Keys.PIN_OPEN)
        )

        if (it == Keys.PIN_CREATE_AFTER_LOGIN) findNavController().navigate(
            R.id.action_splashScreen_to_pinCodeScreen,
            bundleOf(Keys.PIN_BUNDLE_KEY to Keys.PIN_CREATE_AFTER_LOGIN)
        )
    }
}
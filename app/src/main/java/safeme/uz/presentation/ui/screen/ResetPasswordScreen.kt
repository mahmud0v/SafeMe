package safeme.uz.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ManageScreen
import safeme.uz.data.remote.request.ResetPasswordRequest
import safeme.uz.databinding.ScreenResetPasswordBinding
import safeme.uz.presentation.viewmodel.resetpassword.ResetPasswordViewModel
import safeme.uz.presentation.viewmodel.resetpassword.ResetPasswordViewModelImpl
import safeme.uz.utils.Keys
import safeme.uz.utils.snackBar
import safeme.uz.utils.snackMessage

@AndroidEntryPoint
class ResetPasswordScreen : Fragment(R.layout.screen_reset_password) {

    private val binding by viewBinding(ScreenResetPasswordBinding::bind)
    private val viewModel: ResetPasswordViewModel by viewModels<ResetPasswordViewModelImpl>()
    private var checkPassword: Boolean = false
    private var checkConfirmPassword: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        initObservers()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initObservers() = with(viewModel) {
        errorLiveData.observe(this@ResetPasswordScreen, errorObserver)
        messageLiveData.observe(this@ResetPasswordScreen, messageObserver)
        progressLiveData.observe(viewLifecycleOwner, progressObserver)
        openLoginScreenLiveData.observe(this@ResetPasswordScreen, openLoginScreenObserver)
    }

    private val openLoginScreenObserver = Observer<Unit> {
        val navOption = NavOptions.Builder().setPopUpTo(R.id.resetPasswordScreen, true)
            .setPopUpTo(R.id.verifyScreen, true).setPopUpTo(R.id.resetUsernameScreen, true)
            .setPopUpTo(R.id.loginScreen, true).build()
        val manageScreen:ManageScreen? = arguments?.getSerializable(Keys.BUNDLE_KEY) as ManageScreen
        if (manageScreen?.secondaryScreen == Keys.PROFILE_TO_EDIT) {
            val bundle = Bundle().apply {
                putSerializable(Keys.BUNDLE_KEY,manageScreen)
            }
            findNavController().navigate(R.id.action_resetPasswordScreen_to_profileScreen,bundle)
        } else {
            findNavController().navigate(R.id.loginScreen, null, navOption)
        }

    }

    private val progressObserver = Observer<Boolean> {
        binding.progress.isVisible = it
    }

    private val messageObserver = Observer<String> {
        binding.progress.snackBar(it)
    }

    private val errorObserver = Observer<Int> {
        binding.etPasswordLayout.error = getString(it)
        binding.progress.snackBar(getString(it))
    }

    private fun initViews() = with(binding) {
        etPassword.addTextChangedListener {
            checkPassword()
            etPasswordLayout.isErrorEnabled = false
        }
        etConfirmPassword.addTextChangedListener {
            checkConfirmPassword()
            etConfirmPasswordLayout.isErrorEnabled = false
        }

        resetPassword.setOnClickListener {
            if (checkPassword && checkConfirmPassword) {
                viewModel.openLoginScreen(
                    ResetPasswordRequest(
                        etPassword.text.toString(), etConfirmPassword.text.toString()
                    )
                )
            } else {
                checkPassword()
                checkConfirmPassword()
            }
        }
    }

    private fun checkPassword() = with(binding) {
        checkPassword = false
        val password = etPassword.text.toString()
        if (password.isEmpty()) {
            etPasswordLayout.error = getString(R.string.please_enter_password)
        } else if (password.length in 1..5) {
            etPasswordLayout.error = getString(R.string.password_length_must_be_6)
        } else if (password.contains(" ")) {
            etPasswordLayout.error = getString(R.string.password_contains_invalid_characters)
        } else if (etPassword.text.length >= 6) {
            checkPassword = true
        }
    }

    private fun checkConfirmPassword() = with(binding) {
        checkConfirmPassword = false
        if (etConfirmPassword.text.isEmpty()) {
            etConfirmPasswordLayout.error = getString(R.string.please_enter_password)
        } else if (etConfirmPassword.text.toString().contains(" ")) {
            etConfirmPasswordLayout.error = getString(R.string.password_contains_invalid_characters)
        } else if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
            etConfirmPasswordLayout.error = getString(R.string.passwords_not_match)
        } else {
            checkConfirmPassword = true
        }
    }
}
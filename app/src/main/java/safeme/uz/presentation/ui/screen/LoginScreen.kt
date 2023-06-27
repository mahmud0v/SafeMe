package safeme.uz.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.remote.request.LoginRequest
import safeme.uz.data.remote.response.LoginResponse
import safeme.uz.databinding.ScreenLoginBinding
import safeme.uz.presentation.viewmodel.login.LoginViewModel
import safeme.uz.presentation.viewmodel.login.LoginViewModelImpl
import safeme.uz.utils.Keys
import safeme.uz.utils.Keys.LOGIN_TO_EDIT
import safeme.uz.utils.hideKeyboard
import safeme.uz.utils.snackMessage

@AndroidEntryPoint
class LoginScreen : Fragment(R.layout.screen_login), View.OnClickListener {

    private val binding by viewBinding(ScreenLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels<LoginViewModelImpl>()

    private var loginValid: Boolean = false
    private var passwordValid: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initObservers() = with(viewModel) {
        errorLiveData.observe(this@LoginScreen, errorObserver)
        messageLiveData.observe(this@LoginScreen, messageObserver)
        progressLiveData.observe(viewLifecycleOwner, progressObserver)
        openMainScreenLiveData.observe(this@LoginScreen, openMainScreenObserver)
    }

    private val openMainScreenObserver = Observer<LoginResponse> {
        if (it.hasPin.isEmpty()) {
            findNavController().navigate(
                R.id.action_loginScreen_to_pinCodeScreen,
                bundleOf(Keys.PIN_BUNDLE_KEY to Keys.PIN_CREATE_AFTER_LOGIN)
            )
        } else {
            findNavController().navigate(
                R.id.action_loginScreen_to_pinCodeScreen,
                bundleOf(Keys.PIN_BUNDLE_KEY to Keys.PIN_OPEN)
            )
        }
    }

    private val errorObserver = Observer<Int> {
        binding.etPhoneNumberLayout.error = getString(it)
    }

    private val messageObserver = Observer<String> {
        hideKeyboard()
        snackMessage(it)
    }

    private val progressObserver = Observer<Boolean> {
        binding.progress.isVisible = it
    }

    private fun initViews() = with(binding) {
        registerButton.setOnClickListener(this@LoginScreen)
        loginButton.setOnClickListener(this@LoginScreen)
        forgetPasswordButton.setOnClickListener(this@LoginScreen)

        etPhoneNumber.addTextChangedListener {
            etPhoneNumberLayout.isErrorEnabled = false
        }
        etPassword.addTextChangedListener {
            etPasswordLayout.isErrorEnabled = false
        }
    }

    override fun onClick(v: View?): Unit = with(binding) {
        when (v) {
            forgetPasswordButton -> {
                findNavController().navigate(
                    LoginScreenDirections.actionLoginScreenToResetUsernameScreen()
                )
            }
            loginButton -> {
                check()
                if (loginValid && passwordValid) viewModel.login(
                    LoginRequest(
                        "998${etPhoneNumber.rawText}", etPassword.text.toString()
                    )
                )
                hideKeyboard()
            }
            registerButton -> {
                findNavController().navigate(R.id.action_loginScreen_to_registrationScreen)
            }
        }
    }

    private fun check() = with(binding) {
        if (etPhoneNumber.rawText.length < 9) {
            etPhoneNumberLayout.error = getString(R.string.please_enter_phone)
            loginValid = false
        } else loginValid = true

        passwordValid = false
        val password = etPassword.text.toString()
        if (password.isEmpty()) {
            etPasswordLayout.error = getString(R.string.please_enter_password)
        } else if (password.length in 1..5) {
            etPasswordLayout.error = getString(R.string.password_length_must_be_6)
        } else if (password.contains(" ")) {
            etPasswordLayout.error = getString(R.string.password_contains_invalid_characters)
        } else passwordValid = true
    }
}
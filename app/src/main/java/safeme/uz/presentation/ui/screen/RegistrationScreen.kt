package safeme.uz.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.VerifyModel
import safeme.uz.databinding.ScreenRegistrationBinding
import safeme.uz.presentation.viewmodel.register.RegisterViewModel
import safeme.uz.presentation.viewmodel.register.RegisterViewModelImpl
import safeme.uz.utils.VerifyType
import safeme.uz.utils.hideKeyboard
import safeme.uz.utils.snackBar
import safeme.uz.utils.snackMessage

@AndroidEntryPoint
class RegistrationScreen : Fragment(R.layout.screen_registration), OnClickListener {
    private val binding by viewBinding(ScreenRegistrationBinding::bind)
    private val viewModel: RegisterViewModel by viewModels<RegisterViewModelImpl>()

    private var checkPassword: Boolean = false
    private var checkPhoneNumber: Boolean = false
    private var checkConfirmPassword: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initObservers() = with(viewModel) {
        errorLiveData.observe(this@RegistrationScreen, errorObserver)
        messageLiveData.observe(this@RegistrationScreen, messageObserver)
        progressLiveData.observe(viewLifecycleOwner, progressObserver)
        openVerifyScreenLiveData.observe(this@RegistrationScreen, openVerifyScreenObserver)
    }

    private val openVerifyScreenObserver = Observer<VerifyModel> {
        findNavController().navigate(
            RegistrationScreenDirections.actionRegistrationScreenToVerifyScreen(
                it
            )
        )

    }

    private val progressObserver = Observer<Boolean> {
        binding.progress.isVisible = it
    }

    private val messageObserver = Observer<String> {
        hideKeyboard()
        binding.progress.snackBar(it)
    }

    private val errorObserver = Observer<Int> {
        binding.etPhoneNumberLayout.error = getString(it)
    }


    private fun initViews() = with(binding) {
        registerButton.setOnClickListener(this@RegistrationScreen)
        allReadyMemberButton.setOnClickListener(this@RegistrationScreen)

        etPhoneNumber.addTextChangedListener {
            etPhoneNumberLayout.isErrorEnabled = false
        }

        etPassword.addTextChangedListener {
            etPasswordLayout.isErrorEnabled = false
        }
        etConfirmPassword.addTextChangedListener {
            etConfirmPasswordLayout.isErrorEnabled = false
        }
    }

    override fun onClick(v: View?): Unit = with(binding) {
        when (v) {
            registerButton -> {
                checkPassword()
                checkConfirmPassword()
                checkPhoneNumber()
                if (checkPassword && checkPhoneNumber && checkConfirmPassword) {
                    viewModel.register(
                        VerifyModel(
                            phoneNumber = "998${etPhoneNumber.rawText}",
                            title = getString(R.string.confirmation_code),
                            type = VerifyType.VERIFY_REGISTER.ordinal,
                            password = etPassword.text.toString()
                        )
                    )
                }
                hideKeyboard()
            }
            allReadyMemberButton -> {
                findNavController().popBackStack()
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
        } else if (etPassword.text.length >= 5) {
            checkPassword = true
        }
    }

    private fun checkConfirmPassword() = with(binding) {
        checkConfirmPassword = false
        if (etConfirmPassword.text.isEmpty()) {
            etConfirmPasswordLayout.error = getString(R.string.please_enter_confirm_password)
        } else if (etConfirmPassword.text.toString().contains(" ")) {
            etConfirmPasswordLayout.error =
                getString(R.string.confirm_password_contains_invalid_characters)
        } else if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
            etConfirmPasswordLayout.error = getString(R.string.passwords_not_match)
        } else {
            checkConfirmPassword = true
        }
    }

    private fun checkPhoneNumber() = with(binding) {
        if (etPhoneNumber.rawText.length < 9) {
            etPhoneNumberLayout.error = getString(R.string.please_enter_phone)
            checkPhoneNumber = false
        } else checkPhoneNumber = true
    }

}
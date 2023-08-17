package safeme.uz.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ManageScreen
import safeme.uz.data.model.VerifyModel
import safeme.uz.data.remote.request.VerifyRegisterRequest
import safeme.uz.data.remote.response.VerifyRegisterResponse
import safeme.uz.data.remote.response.VerifyResetPasswordResponse
import safeme.uz.databinding.ScreenVerifyBinding
import safeme.uz.presentation.viewmodel.verify.VerifyViewModel
import safeme.uz.presentation.viewmodel.verify.VerifyViewModelImpl
import safeme.uz.utils.*

@AndroidEntryPoint
class VerifyScreen : Fragment(R.layout.screen_verify) {
    private val binding by viewBinding(ScreenVerifyBinding::bind)
    private val viewModel: VerifyViewModel by viewModels<VerifyViewModelImpl>()
    private val navArgs: VerifyScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
    }

    @SuppressLint("FragmentLiveDataObserve", "SetTextI18n")
    private fun initObservers() = with(viewModel) {
        errorLiveData.observe(this@VerifyScreen, errorObserver)
        messageLiveData.observe(this@VerifyScreen, messageObserver)
        progressLiveData.observe(viewLifecycleOwner, progressObserver)
        openProfileScreenLiveData.observe(this@VerifyScreen, openProfileScreenObserver)
        openResetPasswordScreenLiveData.observe(this@VerifyScreen, openResetPasswordScreenObserver)
        openPinScreenLiveData.observe(this@VerifyScreen, openPinScreenObserver)
        resendSmsCodeLiveData.observe(this@VerifyScreen, resendSmsCodeObserver)

        timeLiveData.observe(viewLifecycleOwner) {
            val minut = it / 60
            val second = it - (minut * 60)
            val minutString = if (minut < 10) "0$minut" else minut.toString()
            val secondString = if (second < 10) "0$second" else second.toString()
            binding.time.text = "$minutString:$secondString"
        }
        timeStatus.observe(viewLifecycleOwner) {
            if (it) {
                binding.resendCodeButton.gone()
                binding.time.visible()
                binding.button.enable()
            } else binding.time.invisible()
        }
    }

    private val errorObserver = Observer<Int> {
        binding.etVerificationCodeLayout.error = getString(it)
    }

    private val messageObserver = Observer<String> {
        binding.button.snackBar(it)
    }

    private val progressObserver = Observer<Boolean> {
        binding.progress.isVisible = it
    }


    private val openResetPasswordScreenObserver = Observer<VerifyResetPasswordResponse> {
        val bundle = Bundle().apply {
            putSerializable(Keys.BUNDLE_KEY,navArgs.model.manageScreen)
        }
        if (it.response == Keys.PROFILE_TO_EDIT) {
            findNavController().navigate(R.id.action_verifyScreen_to_resetPasswordScreen, bundle)
        } else {
            findNavController().navigate(R.id.action_verifyScreen_to_resetPasswordScreen,bundle)

        }
    }

    private val openProfileScreenObserver = Observer<VerifyRegisterResponse> {
        findNavController().navigate(R.id.action_verifyScreen_to_profileInfoScreen)

    }

    private val openPinScreenObserver = Observer<Unit> {
        findNavController().navigate(
            R.id.action_verifyScreen_to_pinCodeScreen,
            bundleOf(Keys.PIN_BUNDLE_KEY to Keys.PIN_CREATE_AFTER_LOGIN)
        )

    }

    private val resendSmsCodeObserver = Observer<Boolean> {
        binding.resendCodeButton.visible()
        binding.button.disable()
    }

    private fun initViews() = with(binding) {
        verifyTitle.text = navArgs.model.title
        tvDesc.text = getString(
            R.string.enter_confirmation_code_for_phone_number, "+${navArgs.model.phoneNumber.maskPhoneText()?:""}"
        )

        etVerificationCode.addTextChangedListener {
            etVerificationCodeLayout.isErrorEnabled = false
            it?.let {
                if (it.length == 4) {
                    button.enable()
                    hideKeyboard()
                } else button.disable()
            }
        }

        button.setOnClickListener {
            when (navArgs.model.type) {
                VerifyType.VERIFY_PASSWORD.ordinal -> {
                    val manageScreen = navArgs.model.manageScreen as ManageScreen
                    if (manageScreen.secondaryScreen == Keys.PROFILE_TO_EDIT) {
                        viewModel.verifyCodeForPassword(
                            etVerificationCode.text.toString(),
                            manageScreen
                        )
                    } else {
                        viewModel.verifyCodeForPassword(etVerificationCode.text.toString(), null)
                    }
                }

                VerifyType.VERIFY_REGISTER.ordinal -> {
                    viewModel.verifyCodeRegister(
                        VerifyRegisterRequest(
                            etVerificationCode.text.toString()
                        )
                    )
                }

                VerifyType.VERIFY_PINCODE.ordinal -> {
                    viewModel.verifyCodeForPin(etVerificationCode.text.toString())
                }
            }
            hideKeyboard()
        }

        resendCodeButton.setOnClickListener {
            etVerificationCode.text?.clear()

            when (navArgs.model.type) {
                VerifyType.VERIFY_PASSWORD.ordinal -> {
                    viewModel.resendCodeForPassword(navArgs.model.phoneNumber)
                }

                VerifyType.VERIFY_REGISTER.ordinal -> {
                    val phone = navArgs.model.phoneNumber
                    val password = navArgs.model.password
                    val registerRequest = VerifyModel(phoneNumber = phone, password = password)
                    viewModel.resendCodeForRegister(registerRequest)
                }

                VerifyType.VERIFY_PINCODE.ordinal -> {
                    viewModel.resendCodeForPinCode()
                }
            }
        }
    }


}
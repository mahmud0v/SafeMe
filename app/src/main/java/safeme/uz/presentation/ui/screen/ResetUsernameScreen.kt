package safeme.uz.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ManageScreen
import safeme.uz.data.model.VerifyModel
import safeme.uz.databinding.ScreenResetUsernameBinding
import safeme.uz.presentation.viewmodel.resetusername.ResetUserNameViewModel
import safeme.uz.presentation.viewmodel.resetusername.ResetUserNameViewModelImpl
import safeme.uz.utils.Keys
import safeme.uz.utils.VerifyType
import safeme.uz.utils.hideKeyboard
import safeme.uz.utils.showKeyboard
import safeme.uz.utils.snackBar

@AndroidEntryPoint
class ResetUsernameScreen : Fragment(R.layout.screen_reset_username) {

    private val binding by viewBinding(ScreenResetUsernameBinding::bind)
    private val viewModel: ResetUserNameViewModel by viewModels<ResetUserNameViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initObservers() = with(viewModel) {
        errorLiveData.observe(this@ResetUsernameScreen, errorObserver)
        messageLiveData.observe(this@ResetUsernameScreen, messageObserver)
        progressLiveData.observe(viewLifecycleOwner, progressObserver)
        openVerifyScreenLiveData.observe(this@ResetUsernameScreen, openVerifyScreenObserver)
    }

    private val openVerifyScreenObserver = Observer<VerifyModel> {
        val manageScreen = arguments?.getSerializable(Keys.BUNDLE_KEY) as ManageScreen
        findNavController().navigate(ResetUsernameScreenDirections.actionResetUsernameScreenToVerifyScreen(
            VerifyModel(it.phoneNumber,it.title,it.type,it.password,manageScreen)
        ))
    }

    private val progressObserver = Observer<Boolean> {
        binding.progress.isVisible = it
    }

    private val messageObserver = Observer<String> {
        hideKeyboard()
        binding.button.snackBar(it)
    }

    private val errorObserver = Observer<Int> {
        binding.etPhoneNumberLayout.error = getString(it)
    }

    private fun initViews() = with(binding) {
        val manageScreen = arguments?.getSerializable(Keys.BUNDLE_KEY) as ManageScreen
        manageScreen.phoneNumber?.let {
            etPhoneNumber.setText(it)
        }
        etPhoneNumber.addTextChangedListener {
            if (etPhoneNumber.rawText.length == 9) {
                hideKeyboard()
            } else {
                showKeyboard()
            }
            etPhoneNumberLayout.isErrorEnabled = false
        }

        button.setOnClickListener {
            if (etPhoneNumber.rawText.length >= 9) viewModel.openVerifyScreen(
                VerifyModel(
                    "998${etPhoneNumber.rawText}",
                    getString(R.string.reset_password),
                    VerifyType.VERIFY_PASSWORD.ordinal
                )
            )
            else etPhoneNumberLayout.error = getString(R.string.please_enter_phone)
        }
    }
}
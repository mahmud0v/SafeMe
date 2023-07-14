package safeme.uz.presentation.ui.screen


import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.RemindChangePasswordRequest
import safeme.uz.data.remote.response.RemindPasswordChangeBody
import safeme.uz.databinding.ScreenPasswordRecoverBinding
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.viewmodel.profileInfo.ProfileScreenViewModel
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.Keys
import safeme.uz.utils.disable
import safeme.uz.utils.enable
import safeme.uz.utils.isConnected
import safeme.uz.utils.snackBar
import safeme.uz.utils.snackMessage


@AndroidEntryPoint
class PasswordRecoverScreen : Fragment(R.layout.screen_password_recover) {
    private val binding: ScreenPasswordRecoverBinding by viewBinding()
    private val viewModel: ProfileScreenViewModel by viewModels()
    private var oldPasswordStateResult = false
    private var newPasswordStateResult = false
    private var reNewPasswordStateResult = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        forgotPassword()
        remindedPasswordChange()
        buttonClickEvent()
        backClickEvent()
    }


    private fun remindedPasswordChange() {
        binding.oldPasswordEdit.addTextChangedListener {
            oldPasswordStateResult = it.toString().length >= 6
            buttonEnabledState()

        }

        binding.newPasswordEdit.addTextChangedListener {
            newPasswordStateResult = it.toString().length >= 6
            buttonEnabledState()

        }

        binding.reNewPasswordEdit.addTextChangedListener {
            binding.reNewPasswordLayout.isErrorEnabled = false
            reNewPasswordStateResult = it.toString().length >= 6
            buttonEnabledState()

        }

    }

    private fun forgotPassword() {
        binding.allReadyMemberButton.setOnClickListener {
            val manageScreen = arguments?.getSerializable(Keys.BUNDLE_KEY)
            val bundle = Bundle().apply {
                putSerializable(Keys.BUNDLE_KEY,manageScreen)
            }
            findNavController().navigate(R.id.resetUsernameScreen, bundle)
        }
    }

    private fun buttonEnabledState() {
        if (oldPasswordStateResult && newPasswordStateResult && reNewPasswordStateResult) {
            binding.button.enable()
        } else {
            binding.button.disable()
        }
    }

    private fun buttonClickEvent() {
        binding.button.setOnClickListener {
            if (binding.newPasswordEdit.text.toString() != binding.oldPasswordEdit.text.toString() &&
                binding.reNewPasswordEdit.text.toString() != binding.oldPasswordEdit.text.toString()
            ) {
                if (isConnected()){
                    viewModel.remindPasswordChange(
                        RemindChangePasswordRequest(
                            binding.oldPasswordEdit.text.toString(),
                            binding.newPasswordEdit.text.toString(),
                            binding.reNewPasswordEdit.text.toString()
                        )
                    )
                    viewModel.remindChangePasswordLiveData.observe(
                        viewLifecycleOwner,
                        remindChangePasswordObserver
                    )
                }else {
                    val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
                    messageDialog.show(requireActivity().supportFragmentManager,Keys.DIALOG)
                }

            } else {
                binding.reNewPasswordLayout.error = getString(R.string.not_same_new_old_password)
            }
        }
    }

    private val remindChangePasswordObserver =
        Observer<RemoteApiResult<ApiResponse<RemindPasswordChangeBody>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    binding.progress.hide()
                    val messageDialog = MessageDialog(getString(R.string.successfully_changed_passoword))
                    messageDialog.show(requireActivity().supportFragmentManager,Keys.DIALOG)

                }

                is RemoteApiResult.Loading -> {
                    binding.progress.show()
                }

                is RemoteApiResult.Error -> {
                    binding.progress.hide()
                    val messageDialog = MessageDialog(it.message)
                    messageDialog.show(requireActivity().supportFragmentManager,Keys.DIALOG)
                }
            }
        }

    private fun backClickEvent() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}



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
import safeme.uz.presentation.viewmodel.profileInfo.ProfileScreenViewModel
import safeme.uz.utils.AnnouncementResult
import safeme.uz.utils.disable
import safeme.uz.utils.enable
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
            reNewPasswordStateResult = it.toString().length >= 6
            buttonEnabledState()

        }

    }

    private fun forgotPassword() {
        binding.allReadyMemberButton.setOnClickListener {
            findNavController().navigate(R.id.resetUsernameScreen)
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
            if (binding.newPasswordEdit.text.toString() != binding.oldPasswordEdit.text.toString()&&
                    binding.reNewPasswordEdit.text.toString()!= binding.oldPasswordEdit.text.toString()) {
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
            } else {
                snackMessage(getString(R.string.not_same_new_old_password))
            }
        }
    }

    private val remindChangePasswordObserver =
        Observer<AnnouncementResult<ApiResponse<RemindPasswordChangeBody>>> {
            when (it) {
                is AnnouncementResult.Success -> {
                    snackMessage(getString(R.string.successfully_changed_passoword))
                    binding.progress.hide()
                }

                is AnnouncementResult.Loading -> {
                    binding.progress.show()
                }

                is AnnouncementResult.Error -> {
                    snackMessage(it.message!!)
                    binding.progress.hide()
                }
            }
        }

    private fun backClickEvent() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}



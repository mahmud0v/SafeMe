package safeme.uz.presentation.ui.screen.main

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.databinding.ScreenEditProfileBinding

@AndroidEntryPoint
class EditProfileScreen : Fragment(R.layout.screen_edit_profile), OnClickListener {

    private val binding by viewBinding(ScreenEditProfileBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding) {
        ivBack.setOnClickListener(this@EditProfileScreen)
        etDateOfBirth.setOnClickListener(this@EditProfileScreen)
        etWho.setOnClickListener(this@EditProfileScreen)
        btnSave.setOnClickListener(this@EditProfileScreen)
    }

    override fun onClick(view: View): Unit = with(binding) {
        when (view) {
            ivBack -> {
                findNavController().popBackStack()
            }
            etDateOfBirth -> {

            }
            etWho -> {

            }
            btnSave -> {

            }
        }
    }


}
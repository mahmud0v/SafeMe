package safeme.uz.presentation.ui.screen.main

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.databinding.ScreenProfileBinding

@AndroidEntryPoint
class ProfileScreen : Fragment(R.layout.screen_profile), OnClickListener {

    private val binding by viewBinding(ScreenProfileBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding) {
        ivBack.setOnClickListener(this@ProfileScreen)
        btnEditProfile.setOnClickListener(this@ProfileScreen)
        btnEditPassword.setOnClickListener(this@ProfileScreen)
    }

    override fun onClick(view: View): Unit = with(binding) {
        when (view) {
            ivBack -> {
                findNavController().popBackStack()
            }
            btnEditProfile -> {
                findNavController().navigate(R.id.action_profileScreen_to_editProfileScreen)
            }
            btnEditPassword -> {
                findNavController().navigate(R.id.action_profileScreen_to_pinCodeScreen)
            }
        }
    }
}
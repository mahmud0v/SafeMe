package safeme.uz.presentation.ui.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.databinding.ScreenLoginStep2Binding

@AndroidEntryPoint
class LoginStep2Screen : Fragment(R.layout.screen_login_step2) {
    private val binding by viewBinding(ScreenLoginStep2Binding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

    }

    private fun initViews() = with(binding) {

        button.setOnClickListener {
            findNavController().navigate(safeme.uz.R.id.action_loginStep2Screen_to_pinCodeScreen)
        }

    }
}
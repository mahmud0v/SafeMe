package safeme.uz.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.databinding.ScreenExpandedProfileImageBinding


@AndroidEntryPoint
class ExpandedProfileImageScreen : Fragment(R.layout.screen_expanded_profile_image) {
    private val binding by viewBinding(ScreenExpandedProfileImageBinding::bind)
    private val navArgs: ExpandedProfileImageScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() = with(binding) {
        navArgs.model.photoUri?.let { imgUrl ->
            binding.ivProfile.setImageURI(imgUrl)
        }
        tvName.text = "${navArgs.model.first_name ?: ""} ${navArgs.model.last_name ?: ""}"

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
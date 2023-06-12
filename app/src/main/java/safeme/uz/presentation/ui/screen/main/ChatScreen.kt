package safeme.uz.presentation.ui.screen.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.databinding.ScreenChatBinding

@AndroidEntryPoint
class ChatScreen : Fragment(R.layout.screen_chat) {

    private val binding by viewBinding(ScreenChatBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btn.setOnClickListener {
        }

        initViews()
        initLoads()
        initObservers()
    }

    private fun initObservers() {

    }

    private fun initLoads() {


    }

    private fun initViews() = with(binding) {

    }
}
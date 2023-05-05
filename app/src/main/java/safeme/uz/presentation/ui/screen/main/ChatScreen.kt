package safeme.uz.presentation.ui.screen.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.databinding.ScreenChatBinding
import safeme.uz.databinding.ScreenInspectorBinding

@AndroidEntryPoint
class ChatScreen : Fragment(R.layout.screen_chat) {

    private val binding by viewBinding(ScreenChatBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
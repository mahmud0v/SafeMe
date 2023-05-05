package safeme.uz.presentation.ui.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.databinding.ScreenInspectorBinding

@AndroidEntryPoint
class InspectorScreen : Fragment(R.layout.screen_inspector) {

    private val binding by viewBinding(ScreenInspectorBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLoads()
        initViews()
        initObservers()
    }

    private fun initLoads() {


    }

    private fun initObservers() {

    }

    private fun initViews() = with(binding) {

    }
}
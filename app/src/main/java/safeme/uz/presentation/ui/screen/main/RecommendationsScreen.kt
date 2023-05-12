package safeme.uz.presentation.ui.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.databinding.ScreenRecommendationBinding
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel

@AndroidEntryPoint
class RecommendationsScreen : Fragment(R.layout.screen_recommendation) {
    private val binding: ScreenRecommendationBinding by viewBinding()
    private val remindViewModel: RemindListenerViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.ivMenu.setOnClickListener {
            remindViewModel.remindInFragment(true)
        }

    }


}
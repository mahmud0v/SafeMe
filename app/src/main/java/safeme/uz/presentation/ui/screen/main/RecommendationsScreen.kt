package safeme.uz.presentation.ui.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ManageScreen
import safeme.uz.data.remote.response.AgeCategoryInfo
import safeme.uz.data.remote.response.AgeCategoryResponse
import safeme.uz.databinding.ScreenRecommendationBinding
import safeme.uz.presentation.ui.adapter.RecommendationViewPagerAdapter
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel
import safeme.uz.presentation.viewmodel.recommendation.RecommendationViewModel
import safeme.uz.utils.AnnouncementResult
import safeme.uz.utils.Keys
import safeme.uz.utils.Keys.PROFILE_TO_EDIT
import safeme.uz.utils.Keys.RECOMMENDATION_SCREEN
import safeme.uz.utils.backPressDispatcher
import safeme.uz.utils.snackMessage

@AndroidEntryPoint
class RecommendationsScreen : Fragment(R.layout.screen_recommendation) {
    private val binding: ScreenRecommendationBinding by viewBinding()
    private val viewModel: RecommendationViewModel by viewModels()
    private val remindViewModel: RemindListenerViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loadData()
        eventBackListener()
        moveToProfile()
        backPressDispatcher()

    }

    private fun loadData() {
        viewModel.getAgeCategoryLiveData.observe(viewLifecycleOwner, ageCategoryObserver)
    }


    private val ageCategoryObserver =
        Observer<AnnouncementResult<AgeCategoryResponse<AgeCategoryInfo>>> {
            when (it) {
                is AnnouncementResult.Success -> initView(it.data?.body)
                is AnnouncementResult.Error -> snackMessage(it.data?.message!!)
                else -> {}
            }
        }

    private fun initView(ageCategoryList: ArrayList<AgeCategoryInfo>?) {
        if (ageCategoryList != null) {
            val adapter = RecommendationViewPagerAdapter(this, ageCategoryList)
            binding.viewPager2.adapter = adapter
            TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
                tab.text = ageCategoryList[position].title
            }.attach()
        }
    }

    private fun moveToProfile() {
        requireActivity().window.setBackgroundDrawableResource(R.drawable.bg_main)
        val manageScreen = ManageScreen(RECOMMENDATION_SCREEN, PROFILE_TO_EDIT)
        val bundle = Bundle().apply {
            putSerializable(Keys.BUNDLE_KEY, manageScreen)
        }
        binding.ivProfile.setOnClickListener {
            findNavController().navigate(R.id.action_recommendations_to_profileScreen, bundle)
        }
    }

    private fun eventBackListener() {
        binding.ivMenu.setOnClickListener {
            remindViewModel.remindInFragment(true)
        }
    }


}
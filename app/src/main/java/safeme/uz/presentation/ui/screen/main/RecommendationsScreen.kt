package safeme.uz.presentation.ui.screen.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.databinding.ScreenRecommendationsBinding

@AndroidEntryPoint
class RecommendationsScreen : Fragment(R.layout.screen_recommendations) {

    private val binding by viewBinding(ScreenRecommendationsBinding::bind)

    private var tabSelectedId: TabLayout.Tab? = null

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

    @SuppressLint("StringFormatMatches")
    private fun initViews() = with(binding) {
        tabLayout.getTabAt(0)?.text = getString(R.string.years_old, 5, 9)
        tabLayout.getTabAt(1)?.text = getString(R.string.years_old, 10, 15)
        tabLayout.getTabAt(2)?.text = getString(R.string.years_old, 16, 18)

        if (tabSelectedId != null) {
            tabLayout.selectTab(tabSelectedId)
        }
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabSelectedId = tab

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

}
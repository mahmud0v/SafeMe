package safeme.uz.presentation.ui.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.databinding.ScreenAnnouncementBinding

@AndroidEntryPoint
class AnnouncementScreen : Fragment(R.layout.screen_announcement) {

    private val binding by viewBinding(ScreenAnnouncementBinding::bind)

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

    private fun initViews() = with(binding) {
        if (tabSelectedId != null) {
            tabLayout.selectTab(tabSelectedId)
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
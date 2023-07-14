package safeme.uz.presentation.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import safeme.uz.data.remote.response.AgeCategoryInfo
import safeme.uz.presentation.ui.screen.PollPagerScreen
import safeme.uz.utils.Keys

class PollViewPagerAdapter(
    private val fm: Fragment,
    private val ageList: ArrayList<AgeCategoryInfo>
) : FragmentStateAdapter(fm) {

    override fun getItemCount() = ageList.size

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle().apply {
            putInt(Keys.POLL_PAGER, ageList[position].id)
        }
        val fragment = PollPagerScreen()
        fragment.arguments = bundle
        return fragment
    }
}
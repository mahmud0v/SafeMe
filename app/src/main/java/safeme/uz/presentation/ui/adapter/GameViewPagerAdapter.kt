package safeme.uz.presentation.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import safeme.uz.data.remote.response.AgeCategoryInfo
import safeme.uz.presentation.ui.screen.GamerPagerScreen

class GameViewPagerAdapter(
    fm: Fragment,
    private val ageList: ArrayList<AgeCategoryInfo>
) : FragmentStateAdapter(fm) {

    override fun getItemCount() = ageList.size

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle().apply {
            putInt("key", ageList[position].id)
        }
        val fragment = GamerPagerScreen()
        fragment.arguments = bundle
        return fragment
    }

}
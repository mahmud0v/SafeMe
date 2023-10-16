package safeme.uz.presentation.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import safeme.uz.data.model.CategoriesData
import safeme.uz.presentation.ui.screen.AnnouncementPagerScreen

class AnnouncementViewPagerAdapter(fm: Fragment, private val categoryList:ArrayList<CategoriesData>) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int = categoryList.size

    override fun createFragment(position: Int): Fragment {
        val fragment = AnnouncementPagerScreen()
        val bundle = Bundle().apply {
            putInt("key",categoryList[position].id!!)
        }
        fragment.arguments = bundle
        return fragment
    }
}
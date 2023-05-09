package safeme.uz.presentation.ui.screen.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.databinding.ScreenAnnouncementBinding
import safeme.uz.presentation.ui.adapter.ViewPagerAdapter
import safeme.uz.presentation.viewmodel.announcement.AnnouncementViewModel
import safeme.uz.utils.AnnouncementResult
import safeme.uz.utils.snackMessage

@AndroidEntryPoint
class AnnouncementScreen : Fragment(R.layout.screen_announcement) {

    private val binding: ScreenAnnouncementBinding by viewBinding()
    private val viewModel: AnnouncementViewModel by viewModels()
    private var categoryList = ArrayList<CategoriesData>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initLoadData()

    }



    private fun initLoadData() {
        viewModel.apply {
            getAllCategories()
            getAllCategoriesLiveData.observe(viewLifecycleOwner, categoryObserver)

        }

    }


    private val categoryObserver =
        Observer<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>> {
            when (it) {
                is AnnouncementResult.Success -> initViews(it.data?.body!!)
                is AnnouncementResult.Error -> snackMessage(it.message!!)
                else -> {}
            }
        }




    private fun initViews(listCategory: ArrayList<CategoriesData>) {
        val adapter = ViewPagerAdapter(this,listCategory)
        binding.viewPager2.adapter = adapter
        categoryList = listCategory
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = listCategory[position].title
        }.attach()

    }



}
package safeme.uz.presentation.ui.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.databinding.ScreenAnnouncementBinding
import safeme.uz.presentation.ui.adapter.ViewPagerAdapter
import safeme.uz.presentation.viewmodel.announcement.AnnouncementViewModel
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel
import safeme.uz.utils.AnnouncementResult
import safeme.uz.utils.Keys
import safeme.uz.utils.isConnected
import safeme.uz.utils.snackMessage

@AndroidEntryPoint
class AnnouncementScreen : Fragment(R.layout.screen_announcement) {

    private val binding: ScreenAnnouncementBinding by viewBinding()
    private val viewModel: AnnouncementViewModel by viewModels()
    private val remindViewModel: RemindListenerViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initLoadData()
        manageDrawerLayout()


    }


    private fun initLoadData() {
        if (isConnected()) {
            viewModel.apply {
                getAllCategories()
                getAllCategoriesLiveData.observe(viewLifecycleOwner, categoryObserver)
            }
        } else {
            snackMessage(Keys.INTERNET_FAIL)
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
            val adapter = ViewPagerAdapter(this, listCategory)
            binding.viewPager2.adapter = adapter
            TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
                tab.text = listCategory[position].title
            }.attach()

        }

        private fun manageDrawerLayout() {
            binding.ivMenu.setOnClickListener {
                remindViewModel.remindInFragment(true)
            }
        }


    }
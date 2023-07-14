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
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.ManageScreen
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.databinding.ScreenAnnouncementBinding
import safeme.uz.presentation.ui.adapter.AnnouncementViewPagerAdapter
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.viewmodel.announcement.AnnouncementViewModel
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.Keys
import safeme.uz.utils.backPressDispatcher
import safeme.uz.utils.isConnected

@AndroidEntryPoint
class AnnouncementScreen : Fragment(R.layout.screen_announcement) {

    private val binding: ScreenAnnouncementBinding by viewBinding()
    private val viewModel: AnnouncementViewModel by viewModels()
    private val remindViewModel: RemindListenerViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initLoadData()
        manageDrawerLayout()
        moveToProfile()
        moveToSOS()
        backPressDispatcher()

    }


    private fun initLoadData() {
        if (isConnected()) {
            viewModel.apply {
                getAllCategoriesLiveData.observe(viewLifecycleOwner, categoryObserver)
            }
        } else {
            val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
            messageDialog.show(requireActivity().supportFragmentManager,Keys.DIALOG)
        }
    }


    private val categoryObserver =
        Observer<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>> {
            when (it) {
                is RemoteApiResult.Success -> initViews(it.data?.body)
                is RemoteApiResult.Error -> {
                    val messageDialog = MessageDialog(it.message!!)
                    messageDialog.show(requireActivity().supportFragmentManager,Keys.DIALOG)
                }
                else -> {}
            }
        }


    private fun initViews(listCategory: ArrayList<CategoriesData>?) {
        listCategory?.let {
            val adapter = AnnouncementViewPagerAdapter(this, listCategory)
            binding.viewPager2.adapter = adapter
            TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
                tab.text = listCategory[position].title
            }.attach()
        }

    }

    private fun manageDrawerLayout() {
        binding.ivMenu.setOnClickListener {
            remindViewModel.remindInFragment(true)
        }
    }

    private fun moveToProfile() {
        val manageScreen = ManageScreen(Keys.ANNOUNCEMENT_SCREEN, Keys.PROFILE_TO_EDIT)
        val bundle = Bundle().apply {
            putSerializable(Keys.BUNDLE_KEY, manageScreen)
        }
        binding.ivProfile.setOnClickListener {
            findNavController().navigate(R.id.action_announcements_to_profileScreen, bundle)
        }
    }

    private fun moveToSOS() {
        binding.ivSOS.setOnClickListener {
            val action = AnnouncementScreenDirections.actionAnnouncementsToSosScreen()
            findNavController().navigate(action)
        }

    }


}
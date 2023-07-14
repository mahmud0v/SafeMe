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
import safeme.uz.databinding.ScreenPollBinding
import safeme.uz.presentation.ui.adapter.PollViewPagerAdapter
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel
import safeme.uz.presentation.viewmodel.poll.PollScreenViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.isConnected

@AndroidEntryPoint
class PollScreen : Fragment(R.layout.screen_poll) {
    private val binding: ScreenPollBinding by viewBinding()
    private val viewModel: PollScreenViewModel by viewModels()
    private val remindedListener: RemindListenerViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loadDataTabLayout()
        backListenerEvent()
        moveToProfile()
        moveToSos()

    }

    private fun initViewPager(data: ArrayList<AgeCategoryInfo>?) {
        data?.let {
            val adapter = PollViewPagerAdapter(this, it)
            binding.viewPager2.adapter = adapter
            TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
                tab.text = it[position].title
            }.attach()
        }

    }

    private fun loadDataTabLayout() {
        if (isConnected()) {
            viewModel.ageCategoryLiveData.observe(viewLifecycleOwner, ageCategoryObserver)
        } else {
            val messageDialog = MessageDialog(getString(R.string.no_data))
            messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
        }
    }

    private val ageCategoryObserver =
        Observer<RemoteApiResult<AgeCategoryResponse<AgeCategoryInfo>>> {
            when (it) {

                is RemoteApiResult.Success -> {
                    initViewPager(it.data?.body)
                }

                is RemoteApiResult.Error -> {
                    val messageDialog = MessageDialog(it.message)
                    messageDialog.show(requireActivity().supportFragmentManager,Keys.DIALOG)
                }

                else -> {}
            }
        }

    private fun backListenerEvent() {
        binding.ivMenu.setOnClickListener {
            remindedListener.remindInFragment(true)
        }
    }

    private fun moveToProfile() {

        binding.ivProfile.setOnClickListener {
            val manageScreen = ManageScreen(Keys.POLL_PAGER, Keys.PROFILE_TO_EDIT)
            val bundle = Bundle().apply {
                putSerializable(Keys.BUNDLE_KEY, manageScreen)
            }
            findNavController().navigate(R.id.action_questionnaire_to_profileScreen, bundle)
        }

    }

    private fun moveToSos() {
        binding.ivSOS.setOnClickListener {
            findNavController().navigate(R.id.action_questionnaire_to_sosScreen)
        }
    }


}
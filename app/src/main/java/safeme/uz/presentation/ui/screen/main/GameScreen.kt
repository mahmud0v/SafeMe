package safeme.uz.presentation.ui.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ManageScreen
import safeme.uz.data.remote.response.AgeCategoryInfo
import safeme.uz.data.remote.response.AgeCategoryResponse
import safeme.uz.databinding.ScreenGameBinding
import safeme.uz.presentation.ui.adapter.GameViewPagerAdapter
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel
import safeme.uz.presentation.viewmodel.game.GameScreenViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.backPressDispatcher
import safeme.uz.utils.gone
import safeme.uz.utils.isConnected
import safeme.uz.utils.visible

@AndroidEntryPoint
class GameScreen : Fragment(R.layout.screen_game) {
    private val binding: ScreenGameBinding by viewBinding()
    private val backRemindedViewModel: RemindListenerViewModel by activityViewModels()
    private val viewModel: GameScreenViewModel by viewModels()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadAllData()
        moveToProfile()
        moveToSOS()
        backListenerEvent()
        backPressDispatcher()

    }

    private fun loadAllData() {
        if (isConnected()) {
            loadAgeCategory()
        } else {
            val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
            messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
        }
    }

    private fun loadAgeCategory() {
        viewModel.ageCategoryLiveData.observe(viewLifecycleOwner, ageCategoryObserver)
    }



    private fun initTabLayout(ageCategoryList: ArrayList<AgeCategoryInfo>?) {
        binding.viewPager2.visible()
        binding.placeHolder.gone()
        if (ageCategoryList != null) {
            val adapter = GameViewPagerAdapter(this, ageCategoryList)
            binding.viewPager2.adapter = adapter
            TabLayoutMediator(binding.tabLayout, binding.viewPager2 ) { tab, position ->
                tab.text = ageCategoryList[position].title
            }.attach()

        }
    }


    private val ageCategoryObserver =
        Observer<RemoteApiResult<AgeCategoryResponse<AgeCategoryInfo>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    initTabLayout(it.data?.body)
                }

                is RemoteApiResult.Error -> {
                    binding.placeHolder.visible()
                    binding.viewPager2.gone()
                    if (it.message!=getString(R.string.not_found)){
                        val messageDialog = MessageDialog(getString(R.string.some_error_occurred))
                        messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                    }
                }

                else -> {}
            }
        }

    private fun backListenerEvent() {
        binding.ivMenu.setOnClickListener {
            backRemindedViewModel.remindInFragment(true)
        }
    }

    private fun moveToProfile() {
        binding.ivProfile.setOnClickListener {
            val manageScreen = ManageScreen(Keys.GAME_SCREEN, Keys.PROFILE_TO_EDIT)
            val bundle = Bundle().apply {
                putSerializable(Keys.BUNDLE_KEY, manageScreen)
            }
            findNavController().navigate(R.id.action_game_to_profileScreen, bundle)
        }
    }

    private fun moveToSOS() {
        binding.ivSOS.setOnClickListener {
            val action = GameScreenDirections.actionGameToSosScreen()
            findNavController().navigate(action)
        }

    }

}

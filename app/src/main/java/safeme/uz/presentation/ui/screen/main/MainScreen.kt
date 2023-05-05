package safeme.uz.presentation.ui.screen.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.databinding.ScreenMainBinding
import safeme.uz.presentation.viewmodel.main.MainViewModel
import safeme.uz.presentation.viewmodel.main.MainViewModelImpl
import safeme.uz.utils.hideKeyboard
import safeme.uz.utils.snackMessage

@AndroidEntryPoint
class MainScreen : Fragment(R.layout.screen_main), OnClickListener {
    private val binding by viewBinding(ScreenMainBinding::bind)
    private val viewModel: MainViewModel by viewModels<MainViewModelImpl>()

    private var currentFragment: Fragment = RecommendationsScreen()
    private var selectedMenuItem: MenuItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initObservers() = with(viewModel) {
        errorLiveData.observe(this@MainScreen, errorObserver)
        messageLiveData.observe(this@MainScreen, messageObserver)
        logOutLiveData.observe(this@MainScreen, logOutObserver)
    }

    private val messageObserver = Observer<String> {
        hideKeyboard()
        snackMessage(it)
    }

    private val errorObserver = Observer<Int> {

    }

    private val logOutObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_global_loginScreen)
    }

    private fun initViews() = with(binding) {
        updateFragment()

        ivMenu.setOnClickListener(this@MainScreen)
        ivSOS.setOnClickListener(this@MainScreen)
        ivProfile.setOnClickListener(this@MainScreen)

        navigationView.itemIconTintList = null

        navigationView.setNavigationItemSelectedListener {
            setMenuScreen(it)
            true
        }
    }

    private fun setEnableToMenuItems(selectedMenuItem: MenuItem?) {
        selectedMenuItem?.let {
            val size = binding.navigationView.menu.size()
            for (i in 0 until size) {
                binding.navigationView.menu.getItem(i).isEnabled =
                    it.title != binding.navigationView.menu.getItem(i).title
            }
        }
    }

    override fun onClick(view: View): Unit = with(binding) {
        when (view) {
            ivMenu -> {
                setEnableToMenuItems(selectedMenuItem)
                drawerLayout.openDrawer(GravityCompat.START)
            }
            ivSOS -> {
                findNavController().navigate(R.id.action_mainScreen_to_sosScreen)
            }
            ivProfile -> {
                findNavController().navigate(R.id.action_mainScreen_to_profileScreen)
            }
        }
    }


    private fun setMenuScreen(item: MenuItem) {
        childFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        when (item.itemId) {
            R.id.recommendations -> {
                if (currentFragment !is RecommendationsScreen) currentFragment =
                    (RecommendationsScreen())
                updateFragment()
            }
            R.id.announcements -> {
                if (currentFragment !is AnnouncementScreen) currentFragment = (AnnouncementScreen())
                updateFragment()
            }
            R.id.chat -> {
                if (currentFragment !is ChatScreen) currentFragment = (ChatScreen())
                updateFragment()
            }
            R.id.consultant -> {

            }
            R.id.questionnaire -> {

            }
            R.id.prevention_inspector -> {
                if (currentFragment !is InspectorScreen) currentFragment = (InspectorScreen())
                updateFragment()
            }
            R.id.appeals -> {
                if (currentFragment !is AppealsScreen) currentFragment = (AppealsScreen())
                updateFragment()
            }
            R.id.about_us -> {
                if (currentFragment !is AboutUsScreen) currentFragment = (AboutUsScreen())
                updateFragment()
            }
            R.id.logout -> {
                viewModel.logOut()
            }
        }
        item.isEnabled = false
        selectedMenuItem = item
    }

    private fun updateFragment() {
        binding.drawerLayout.closeDrawer(GravityCompat.START, true)
        childFragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.containerMain, currentFragment, "CONTAINER").commitAllowingStateLoss()
    }

}

package safeme.uz

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.databinding.ActivityMainBinding
import safeme.uz.presentation.ui.adapter.DrawerItemRecyclerAdapter
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel
import safeme.uz.utils.LocalHelper
import safeme.uz.utils.Util

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding()
    private var appUpdateManager: AppUpdateManager? = null
    private val remindListenerViewModel: RemindListenerViewModel by viewModels()
    private lateinit var navHostFragment: NavHostFragment
    private val appSharedPreference by lazy { AppSharedPreference(this) }
    private val MY_REQUEST_CODE = 1001


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        when (navHostFragment.navController.currentDestination?.id) {
            R.id.splashScreen -> binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            R.id.recommendations -> {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED)
            }
        }
        remindListenerViewModel.remindListenerLiveData.observe(this, Observer {
            if (it) {
                binding.drawerLayout.open()
            }
        })
        binding.includeHeader.drHeaderLayout.setOnClickListener {}
        val adapter = DrawerItemRecyclerAdapter()
        adapter.differ.submitList(Util.getInfoDrawerItems())
        binding.navigationRv.adapter = adapter
        binding.navigationRv.layoutManager = LinearLayoutManager(this)
        adapter.onItemClick = {
            when (it.title) {
                R.string.recommendations -> {
                    if (navHostFragment.navController.currentDestination?.id == R.id.recommendations) {
                        binding.drawerLayout.close()
                    } else {
                        findNavController(binding.navHost.id).navigate(R.id.recommendations)
                        binding.drawerLayout.close()
                    }
                }

                R.string.announcements -> {
                    if (navHostFragment.navController.currentDestination?.id == R.id.announcements) {
                        binding.drawerLayout.close()
                    } else {
                        findNavController(binding.navHost.id).navigate(R.id.announcements)
                        binding.drawerLayout.close()

                    }
                }

                R.string.games -> {
                    if (navHostFragment.navController.currentDestination?.id == R.id.game) {
                        binding.drawerLayout.close()
                    } else {
                        findNavController(binding.navHost.id).navigate(R.id.game)
                        binding.drawerLayout.close()

                    }
                }

                R.string.questionnaire -> {
                    if (navHostFragment.navController.currentDestination?.id == R.id.questionnaire) {
                        binding.drawerLayout.close()
                    } else {
                        findNavController(binding.navHost.id).navigate(R.id.questionnaire)
                        binding.drawerLayout.close()

                    }
                }

                R.string.inspectors -> {
                    if (navHostFragment.navController.currentDestination?.id == R.id.prevention_inspector) {
                        binding.drawerLayout.close()
                    } else {
                        findNavController(binding.navHost.id).navigate(R.id.prevention_inspector)
                        binding.drawerLayout.close()

                    }
                }

                R.string.appeals -> {
                    if (navHostFragment.navController.currentDestination?.id == R.id.appeals) {
                        binding.drawerLayout.close()
                    } else {
                        findNavController(binding.navHost.id).navigate(R.id.appeals)
                        binding.drawerLayout.close()

                    }
                }

                R.string.settings_drawer -> {
                    if (navHostFragment.navController.currentDestination?.id == R.id.settingsScreen) {
                        binding.drawerLayout.close()
                    } else {
                        findNavController(binding.navHost.id).navigate(R.id.settingsScreen)
                        binding.drawerLayout.close()

                    }
                }

                R.string.about_us -> {
                    if (navHostFragment.navController.currentDestination?.id == R.id.about_us) {
                        binding.drawerLayout.close()
                    } else {
                        findNavController(binding.navHost.id).navigate(R.id.about_us)
                        binding.drawerLayout.close()

                    }
                }
            }
        }
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        remindListenerViewModel.drawerLabelLiveData.observe(this, Observer {
            adapter.labelRecommendation()
        })

        checkUpdate()
        checkLanguage()


    }


    private fun checkUpdate() {
        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                startUpdateFlow(appUpdateInfo)
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdateFlow(appUpdateInfo)
            }
        }
    }

    private fun startUpdateFlow(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager?.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.FLEXIBLE,
                this,
                MY_REQUEST_CODE
            )
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

    private fun checkLanguage() {
        when (appSharedPreference.locale) {
            "uz" -> LocalHelper.changeLanguage("uz-rUz", this)
            "en" -> LocalHelper.changeLanguage("en", this)
            "ru" -> LocalHelper.changeLanguage("ru", this)
            "sr" -> LocalHelper.changeLanguage("uz", this)
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            when (resultCode) {
                RESULT_CANCELED -> {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.update_canceled),
                        Toast.LENGTH_LONG
                    ).show()
                }

                RESULT_OK -> {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.update_success),
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.update_failed),
                        Toast.LENGTH_LONG
                    ).show()
                    checkUpdate()
                }
            }
        }
    }


}
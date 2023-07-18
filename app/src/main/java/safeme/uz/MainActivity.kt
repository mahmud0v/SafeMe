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
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.databinding.ActivityMainBinding
import safeme.uz.presentation.ui.adapter.DrawerItemRecyclerAdapter
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel
import safeme.uz.utils.Util

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding()
    private var appUpdateManager: AppUpdateManager? = null
    private val remindListenerViewModel: RemindListenerViewModel by viewModels()
    private lateinit var navHostFragment:NavHostFragment
    private val MY_REQUEST_CODE = 1001
    private var intentValue: String? = null


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
            if (it){
                binding.drawerLayout.open()
            }
        })
        val adapter = DrawerItemRecyclerAdapter()
        adapter.differ.submitList(Util.getInfoDrawerItems())
        binding.navigationRv.adapter = adapter
        binding.navigationRv.layoutManager = LinearLayoutManager(this)
        adapter.onItemClick = {
            when(it.title){
                R.string.recommendations -> findNavController(binding.navHost.id).navigate(R.id.recommendations)
                R.string.announcements -> findNavController(binding.navHost.id).navigate(R.id.announcements)
                R.string.games -> findNavController(binding.navHost.id).navigate(R.id.game)
                R.string.questionnaire -> findNavController(binding.navHost.id).navigate(R.id.questionnaire)
                R.string.inspectors -> findNavController(binding.navHost.id).navigate(R.id.prevention_inspector)
                R.string.appeals -> findNavController(binding.navHost.id).navigate(R.id.appeals)
                R.string.about_us -> findNavController(binding.navHost.id).navigate(R.id.about_us)
            }
            binding.drawerLayout.close()
        }
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        checkUpdate()


    }


    private fun checkUpdate() {
        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
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
                AppUpdateType.IMMEDIATE,
                this,
                MY_REQUEST_CODE
            )
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
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
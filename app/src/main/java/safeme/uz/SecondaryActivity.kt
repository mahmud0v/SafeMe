package safeme.uz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.databinding.ActivitySecondaryBinding
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel


@AndroidEntryPoint
class SecondaryActivity : AppCompatActivity() {
    private val binding: ActivitySecondaryBinding by viewBinding()
    private val remindViewModel: RemindListenerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        binding.navigationView.setupWithNavController(navHostFragment.navController)

        remindViewModel.remindListenerLiveData.observe(this, Observer {
            if (it && !binding.drawerLayout.isOpen){
                binding.drawerLayout.open()
            }else {
                binding.drawerLayout.close()
            }
        })


    }




}



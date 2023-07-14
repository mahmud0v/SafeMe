package safeme.uz.presentation.ui.screen.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.SosBody
import safeme.uz.data.remote.request.SosRequest
import safeme.uz.databinding.ScreenSosBinding
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.viewmodel.sos.SosScreenViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.isConnected

@AndroidEntryPoint
class SosScreen : Fragment(R.layout.screen_sos) {
    private val binding: ScreenSosBinding by viewBinding()
    private val viewModel: SosScreenViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkLocationPermission()
        sosNotified()
        backEvent()
    }

    private fun sosNotified() {

        binding.button1.setOnClickListener {
            giveCurrentLocation(getString(R.string.suspicious))
        }

        binding.button2.setOnClickListener {
            giveCurrentLocation(getString(R.string.dangerous_area))
        }

        binding.button3.setOnClickListener {
            giveCurrentLocation(getString(R.string.danger))
        }

    }

    private val sosObserver = Observer<RemoteApiResult<ApiResponse<SosBody>>> {
        when (it) {
            is RemoteApiResult.Success -> {
                binding.progress.hide()
                val messageDialog = MessageDialog(getString(R.string.message_notified))
                messageDialog.show(requireActivity().supportFragmentManager,Keys.DIALOG)
            }

            is RemoteApiResult.Error -> {
                binding.progress.hide()
                val messageDialog = MessageDialog(it.message)
                messageDialog.show(requireActivity().supportFragmentManager,Keys.DIALOG)
            }

            is RemoteApiResult.Loading -> {
                binding.progress.show()
            }
        }
    }


    private fun checkLocationPermission() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
            } else {
            }
        }

        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())


    }

    private fun giveCurrentLocation(type: String) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
        }

        fusedLocationClient.lastLocation.addOnSuccessListener {

            it?.let {
                if (isConnected()){
                    viewModel.sosNotified(
                        SosRequest(
                            it.latitude.toString(),
                            it.longitude.toString(),
                            type
                        )
                    )
                    viewModel.sosLiveData.observe(viewLifecycleOwner, sosObserver)
                } else {
                    val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
                    messageDialog.show(requireActivity().supportFragmentManager,Keys.DIALOG)
                }
            }
        }
    }

    private fun backEvent(){
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }






}
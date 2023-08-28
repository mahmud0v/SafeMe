package safeme.uz.presentation.ui.screen.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
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
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.SosBody
import safeme.uz.data.remote.request.SosRequest
import safeme.uz.databinding.ScreenSosBinding
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.ui.dialog.ProminentDisclosureDialog
import safeme.uz.presentation.viewmodel.sos.SosScreenViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.gone
import safeme.uz.utils.isConnected

@AndroidEntryPoint
class SosScreen : Fragment(R.layout.screen_sos) {
    private val binding: ScreenSosBinding by viewBinding()
    private val viewModel: SosScreenViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val sharedPreference by lazy { AppSharedPreference(requireContext()) }
    private val REQUEST_CODE = 100


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showProminentDisclosure()
        sosNotified()
        backEvent()
    }

    private fun makeLocationRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_CODE
        )
    }


    private fun showProminentDisclosure() {
        if (!sharedPreference.prominentDisclosureResult) {
            val prominentDialog = ProminentDisclosureDialog()
            prominentDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
            prominentDialog.allowBtnClick = {
                makeLocationRequest()
            }
            prominentDialog.isCancelable = false
        }
    }

    private fun sosNotified() {

        binding.button1.setOnClickListener {
            if (sharedPreference.prominentDisclosureResult) {
                giveCurrentLocation(getString(R.string.suspicious))
            } else {
                showProminentDisclosure()
            }

        }

        binding.button2.setOnClickListener {
            if (sharedPreference.prominentDisclosureResult) {
                giveCurrentLocation(getString(R.string.dangerous_area))
            } else {
                showProminentDisclosure()
            }
        }

        binding.button3.setOnClickListener {
            if (sharedPreference.prominentDisclosureResult) {
                giveCurrentLocation(getString(R.string.danger))
            } else {
                showProminentDisclosure()
            }
        }

    }

    private val sosObserver = Observer<RemoteApiResult<ApiResponse<SosBody>>> {
        when (it) {
            is RemoteApiResult.Success -> {
                binding.progress.hide()
                val messageDialog = MessageDialog(getString(R.string.message_notified))
                messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
            }

            is RemoteApiResult.Error -> {
                binding.progress.hide()
                val messageDialog = MessageDialog(it.message)
                messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
            }

            is RemoteApiResult.Loading -> {
                binding.progress.show()
            }
        }
    }



    private fun giveCurrentLocation(type: String) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (checkGps()) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    it?.let {
                        if (isConnected()) {
                            viewModel.sosNotified(
                                SosRequest(
                                    it.latitude.toString(),
                                    it.longitude.toString(),
                                    type
                                )
                            )
                            viewModel.sosLiveData.observe(viewLifecycleOwner, sosObserver)
                        } else {
                            binding.progress.gone()
                            val messageDialog =
                                MessageDialog(getString(R.string.internet_not_connected))
                            messageDialog.show(
                                requireActivity().supportFragmentManager,
                                Keys.DIALOG
                            )
                        }
                    }
                }
            } else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

            }
        } else {
            makeLocationRequest()
        }


    }


    private fun checkGps(): Boolean {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    }

    private fun backEvent() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}
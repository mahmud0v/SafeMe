package safeme.uz.presentation.ui.screen.main

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.ManageScreen
import safeme.uz.data.remote.request.AppealRequest
import safeme.uz.data.remote.response.AppealResponse
import safeme.uz.databinding.ScreenAppealsBinding
import safeme.uz.presentation.ui.adapter.SpinnerAdapter
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel
import safeme.uz.presentation.viewmodel.appeal.AppealScreenViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.Util
import safeme.uz.utils.backPressDispatcher
import safeme.uz.utils.disable
import safeme.uz.utils.enable
import safeme.uz.utils.isConnected

@AndroidEntryPoint
class AppealsScreen : Fragment(R.layout.screen_appeals) {

    private val binding: ScreenAppealsBinding by viewBinding()
    private val remindListenerViewModel: RemindListenerViewModel by activityViewModels()
    private val viewModel: AppealScreenViewModel by viewModels()
    private var appealTypeResult = false
    private var appealTitleResult = false
    private var appealDescription = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        sendAppeal()
        checkViewState()
        remindedListenerEvent()
        moveToSos()
        moveToProfile()
        backPressDispatcher()
    }


    private fun checkViewState() {
        val typeList = ArrayList<String>()
        typeList.add(getString(R.string.select_name))
        typeList.addAll(Util.getAppealTypes())
        val spinnerAdapter = SpinnerAdapter(requireContext(), typeList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.appealTypeSpinner.adapter = spinnerAdapter
        binding.appealTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    appealTypeResult = true
                    checkInputView()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        binding.appealTitleEditText.addTextChangedListener {
            appealTitleResult = it.toString().isNotBlank()
            checkInputView()
        }

        binding.appealTextEditText.addTextChangedListener {
            appealDescription = it.toString().isNotBlank()
            checkInputView()
        }

    }

    private fun checkInputView() {
        if (appealTypeResult && appealTitleResult && appealDescription) {
            binding.button.enable()
        } else {
            binding.button.disable()
        }
    }


    private fun remindedListenerEvent() {
        binding.ivMenu.setOnClickListener {
            remindListenerViewModel.remindInFragment(true)
        }
    }

    private fun sendAppeal() {
        binding.button.setOnClickListener {
            val appealRequest = AppealRequest(
                binding.appealTypeSpinner.selectedItem.toString(),
                binding.appealTitleEditText.text.toString(),
                binding.appealTextEditText.text.toString()
            )
            if (isConnected()){
                viewModel.giveAppeal(appealRequest)
                viewModel.giveAppealLiveData.observe(viewLifecycleOwner, appealObserver)
            }else {
                val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
                messageDialog.show(requireActivity().supportFragmentManager,Keys.DIALOG)

            }

        }
    }

    private val appealObserver = Observer<RemoteApiResult<ApiResponse<AppealResponse>>> {
        when (it) {
            is RemoteApiResult.Success -> {
                binding.progress.hide()
                val messageDialog = MessageDialog(getString(R.string.receive_appeal))
                messageDialog.show(requireActivity().supportFragmentManager,Keys.DIALOG)
            }

            is RemoteApiResult.Loading -> {
                binding.progress.show()
            }

            is RemoteApiResult.Error -> {
                binding.progress.hide()
                val messageDialog = MessageDialog(it.message!!)
                messageDialog.show(requireActivity().supportFragmentManager,Keys.DIALOG)            }
        }
    }

    private fun moveToSos() {
        binding.ivSOS.setOnClickListener {
            val action = AppealsScreenDirections.actionAppealsToSosScreen()
            findNavController().navigate(action)
        }
    }

    private fun moveToProfile() {
        binding.ivProfile.setOnClickListener {
            val manageScreen = ManageScreen(Keys.APPEAL_SCREEN, Keys.PROFILE_TO_EDIT)
            val bundle = Bundle().apply {
                putSerializable(Keys.BUNDLE_KEY, manageScreen)
            }
            findNavController().navigate(R.id.action_appeals_to_profileScreen,bundle)
        }

    }


}
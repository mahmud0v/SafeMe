package safeme.uz.presentation.ui.screen.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.ManageScreen
import safeme.uz.data.remote.response.InspectorInfo
import safeme.uz.databinding.ScreenInspectorBinding
import safeme.uz.presentation.ui.adapter.InspectorRecyclerAdapter
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel
import safeme.uz.presentation.viewmodel.inspector.InspectorScreenViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.MarginGameItemDecoration
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.backPressDispatcher
import safeme.uz.utils.gone
import safeme.uz.utils.isConnected
import safeme.uz.utils.visible

@AndroidEntryPoint
class InspectorScreen : Fragment(R.layout.screen_inspector) {
    private val binding: ScreenInspectorBinding by viewBinding()
    private val backRemindedViewModel: RemindListenerViewModel by activityViewModels()
    private val viewModel: InspectorScreenViewModel by viewModels()
    private lateinit var inspectorRecyclerAdapter: InspectorRecyclerAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        backListenerEvent()
        loadInspectorData()
        initRecyclerView()
        callInspector()
        moveToProfile()
        moveToSOS()
        backPressDispatcher()
    }


    private fun initRecyclerView() {
        inspectorRecyclerAdapter = InspectorRecyclerAdapter()
        binding.rvInspector.adapter = inspectorRecyclerAdapter
        binding.rvInspector.layoutManager = LinearLayoutManager(requireContext())
        binding.rvInspector.addItemDecoration(MarginGameItemDecoration())
    }

    private fun loadInspectorData() {
        if (isConnected()) {
            viewModel.getInspectorDataByMFY()
            viewModel.inspectorLiveData.observe(viewLifecycleOwner, inspectorObserver)
        } else {
            binding.progress.gone()
            val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
            messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
        }
    }


    private val inspectorObserver =
        Observer<RemoteApiResult<ApiResponse<ArrayList<InspectorInfo>>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    attachInspectorData(it.data?.body)
                }

                is RemoteApiResult.Error -> {
                    binding.progress.hide()
                    binding.placeHolder.visible()
                    if (it.message != getString(R.string.not_found)) {
                        val messageDialog = MessageDialog(getString(R.string.some_error_occurred))
                        messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                    }
                }

                is RemoteApiResult.Loading -> {
                    binding.progress.visible()
                    binding.placeHolder.gone()
                }
            }
        }

    private fun attachInspectorData(inspectorList: ArrayList<InspectorInfo>?) {
        binding.progress.gone()
        binding.placeHolder.gone()
        inspectorRecyclerAdapter.differ.submitList(inspectorList)
    }


    private fun backListenerEvent() {
        binding.ivMenu.setOnClickListener {
            backRemindedViewModel.remindInFragment(true)
        }
    }

    private fun callInspector() {
        inspectorRecyclerAdapter.onItemClick = { data ->
            val intent = Uri.parse("${getString(R.string.phone)}+${data.phone}").let { number ->
                Intent(Intent.ACTION_DIAL, number)
            }
            startActivity(intent)
        }
    }


    private fun moveToProfile() {
        binding.ivProfile.setOnClickListener {
            val manageScreen = ManageScreen(Keys.INSPECTOR_SCREEN, Keys.PROFILE_TO_EDIT)
            val bundle = Bundle().apply {
                putSerializable(Keys.BUNDLE_KEY, manageScreen)
            }
            findNavController().navigate(R.id.action_prevention_inspector_to_profileScreen, bundle)
        }
    }

    private fun moveToSOS() {
        binding.ivSOS.setOnClickListener {
            val action = InspectorScreenDirections.actionPreventionInspectorToSosScreen()
            findNavController().navigate(action)
        }
    }

}
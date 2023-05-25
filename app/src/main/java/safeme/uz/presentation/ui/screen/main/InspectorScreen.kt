package safeme.uz.presentation.ui.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.InspectorMFYRequest
import safeme.uz.data.remote.response.InspectorInfo
import safeme.uz.data.remote.response.UserResponse
import safeme.uz.databinding.ScreenInspectorBinding
import safeme.uz.presentation.ui.adapter.InspectorRecyclerAdapter
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel
import safeme.uz.presentation.viewmodel.inspector.InspectorScreenViewModel
import safeme.uz.utils.AnnouncementResult
import safeme.uz.utils.gone
import safeme.uz.utils.snackMessage
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
    }


    private fun initRecyclerView() {
        inspectorRecyclerAdapter = InspectorRecyclerAdapter()
        binding.rvInspector.adapter = inspectorRecyclerAdapter
        binding.rvInspector.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadInspectorData() {
        viewModel.getUserData()
        viewModel.userLiveData.observe(viewLifecycleOwner, userDataObserver)
    }

    private val userDataObserver = Observer<AnnouncementResult<UserResponse>> {
        when (it) {
            is AnnouncementResult.Success -> initInspectorView(it.data?.body?.id)
            is AnnouncementResult.Error -> snackMessage(it.data?.message!!)
            else -> {}
        }
    }

    private fun initInspectorView(id: Int?) {
        id?.let {
            viewModel.getInspectorDataByMFY(InspectorMFYRequest(it))
            viewModel.inspectorLiveData.observe(viewLifecycleOwner, inspectorObserver)
        }
    }

    private val inspectorObserver =
        Observer<AnnouncementResult<ApiResponse<ArrayList<InspectorInfo>>>> {
            when (it) {
                is AnnouncementResult.Success -> attachInspectorData(it.data?.body)
                is AnnouncementResult.Error -> snackMessage(it.data?.message!!)
                is AnnouncementResult.Loading -> binding.progress.visible()
            }
        }

    private fun attachInspectorData(inspectorList: ArrayList<InspectorInfo>?) {
        binding.progress.gone()
        inspectorRecyclerAdapter.differ.submitList(inspectorList)
    }


    private fun backListenerEvent() {
        binding.ivMenu.setOnClickListener {
            backRemindedViewModel.remindInFragment(true)
        }
    }
}
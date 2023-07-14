package safeme.uz.presentation.ui.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.response.PollResponseInfo
import safeme.uz.databinding.ScreenPollPagerBinding
import safeme.uz.presentation.ui.adapter.PollPagerRecyclerAdapter
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.ui.screen.main.PollScreenDirections
import safeme.uz.presentation.viewmodel.poll.PollPagerScreenViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.gone
import safeme.uz.utils.isConnected
import safeme.uz.utils.visible

@AndroidEntryPoint
class PollPagerScreen : Fragment(R.layout.screen_poll_pager) {
    private val binding: ScreenPollPagerBinding by viewBinding()
    private val viewModel: PollPagerScreenViewModel by viewModels()
    private val pollRecyclerAdapter by lazy { PollPagerRecyclerAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler()
        loadPollRecyclerData()
        pollQuestionClickEvent()

    }

    private fun initRecycler() {
        binding.pollRv.adapter = pollRecyclerAdapter
        binding.pollRv.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadPollRecyclerData() {
        val ageCategoryId = requireArguments().getInt(Keys.POLL_PAGER)
        if (isConnected()) {
            viewModel.getPollQuestion(AgeCategoryRequest(ageCategoryId))
            viewModel.pollMutableLiveData.observe(viewLifecycleOwner, pollObserver)
        } else {
            val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
            messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
        }

    }

    private val pollObserver = Observer<RemoteApiResult<ApiResponse<ArrayList<PollResponseInfo>>>> {

        when (it) {
            is RemoteApiResult.Success -> {
                binding.progress.hide()
                binding.placeHolder.gone()
                it.data?.let { data ->
                    pollRecyclerAdapter.differ.submitList(data.body)
                }

            }

            is RemoteApiResult.Error -> {
                pollRecyclerAdapter.differ.submitList(emptyList())
                binding.progress.hide()
                if (it.message == getString(R.string.no_data)) {
                    binding.placeHolder.visible()
                } else {
                    binding.placeHolder.gone()
                    val messageDialog = MessageDialog(it.message)
                    messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                }
            }

            is RemoteApiResult.Loading -> {
                binding.progress.show()
                binding.placeHolder.gone()
            }
        }

    }

    private fun pollQuestionClickEvent() {

        pollRecyclerAdapter.onItemClick = {
            it.id?.let {
                val action = PollScreenDirections.actionQuestionnaireToPollDetailScreen(it)
                findNavController().navigate(action)
            }
        }
    }


}
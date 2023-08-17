package safeme.uz.presentation.ui.screen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.DestinationArguments
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.databinding.ScreenAnnouncementPagerBinding
import safeme.uz.presentation.ui.adapter.NewsRecyclerAdapter
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.ui.screen.main.AnnouncementScreenDirections
import safeme.uz.presentation.viewmodel.announcement.AnnouncementPagerViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.MarginAnnouncementItemDecoration
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.gone
import safeme.uz.utils.isConnected
import safeme.uz.utils.visible

@AndroidEntryPoint
class AnnouncementPagerScreen : Fragment(R.layout.screen_announcement_pager) {
    private val binding: ScreenAnnouncementPagerBinding by viewBinding()
    private val viewModel: AnnouncementPagerViewModel by viewModels()
    private val newsAdapter: NewsRecyclerAdapter by lazy { NewsRecyclerAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loadData()
        itemCLickEvent()

    }


    private fun loadData() {
        val argument = requireArguments().getInt(Keys.KEY_VALUE)
        if (isConnected()) {
            viewModel.apply {
                getAllNewsByCategory(argument.toString())
                getAllNewsLiveData.observe(viewLifecycleOwner, newsObserver)
            }
        } else {
            binding.progress.gone()
            val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
            messageDialog.show(requireActivity().supportFragmentManager,Keys.DIALOG)
        }
    }


    private val newsObserver =
        Observer<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<NewsData>>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    initRecyclerView(it.data?.body)
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
                    binding.placeHolder.gone()
                    binding.progress.visible()
                }

            }
        }


    private fun initRecyclerView(newsList: ArrayList<NewsData>?) {
        binding.progress.hide()
        binding.placeHolder.gone()
        newsAdapter.differ.submitList(newsList)
        binding.announcementsRv.adapter = newsAdapter
        binding.announcementsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.announcementsRv.addItemDecoration(MarginAnnouncementItemDecoration())
    }

    private fun itemCLickEvent() {
        newsAdapter.onItemClick = {
            findNavController().navigate(
                AnnouncementScreenDirections.actionAnnouncementScreenToAnnouncementInfoScreen(
                    DestinationArguments(it.id, Keys.ANNOUNCEMENTS)
                )
            )
        }
    }

}
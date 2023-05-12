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
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.databinding.ScreenAnnouncementPagerBinding
import safeme.uz.presentation.ui.adapter.NewsRecyclerAdapter
import safeme.uz.presentation.ui.screen.main.AnnouncementScreenDirections
import safeme.uz.presentation.viewmodel.announcement.AnnouncementViewModel
import safeme.uz.utils.AnnouncementResult
import safeme.uz.utils.Keys
import safeme.uz.utils.gone
import safeme.uz.utils.isConnected
import safeme.uz.utils.snackMessage
import safeme.uz.utils.visible
import java.security.Key

@AndroidEntryPoint
class AnnouncementPagerScreen : Fragment(R.layout.screen_announcement_pager) {
    private val binding: ScreenAnnouncementPagerBinding by viewBinding()
    private val viewModel: AnnouncementViewModel by viewModels()
    private val newsAdapter: NewsRecyclerAdapter by lazy { NewsRecyclerAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loadData()
        itemCLickEvent()

    }


    private fun loadData() {
        val argument = requireArguments().getInt("key")
        if (isConnected()) {
            viewModel.apply {
                getAllNewsByCategory(argument.toString())
                getAllNewsLiveData.observe(viewLifecycleOwner, newsObserver)
            }
        } else {
            snackMessage(Keys.INTERNET_FAIL)
        }
    }


    private val newsObserver =
        Observer<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<NewsData>>>> {
            when (it) {
                is AnnouncementResult.Success -> initRecyclerView(it.data?.body!!)
                is AnnouncementResult.Error -> snackMessage(it.message!!)
                is AnnouncementResult.Loading -> binding.progress.visible()
                else -> {}
            }
        }


    private fun initRecyclerView(newsList: ArrayList<NewsData>) {
        binding.progress.gone()
        newsAdapter.differ.submitList(newsList)
        binding.announcementsRv.adapter = newsAdapter
        binding.announcementsRv.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun itemCLickEvent() {
        newsAdapter.onItemClick = {
            findNavController().navigate(
                AnnouncementScreenDirections.actionAnnouncementScreenToAnnouncementInfoScreen(
                    it.id
                )
            )
        }
    }

}
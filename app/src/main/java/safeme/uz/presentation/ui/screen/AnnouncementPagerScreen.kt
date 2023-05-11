package safeme.uz.presentation.ui.screen

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.databinding.ScreenAnnouncementPagerBinding
import safeme.uz.presentation.ui.adapter.NewsRecyclerAdapter
import safeme.uz.presentation.viewmodel.announcement.AnnouncementViewModel
import safeme.uz.utils.AnnouncementResult
import safeme.uz.utils.gone
import safeme.uz.utils.snackMessage
import safeme.uz.utils.visible

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
        viewModel.apply {
            getAllNewsByCategory(argument.toString())
            getAllNewsLiveData.observe(viewLifecycleOwner, newsObserver)
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
            val bundle = Bundle()
            bundle.putInt("key", it.id)
            requireActivity().supportFragmentManager.commit {
                replace(R.id.containerMain, AnnouncementInfoScreen::class.java, bundle)
                addToBackStack(AnnouncementPagerScreen::class.qualifiedName)
            }
        }
    }

}
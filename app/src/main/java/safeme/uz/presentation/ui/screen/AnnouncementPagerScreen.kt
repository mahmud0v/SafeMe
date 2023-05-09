package safeme.uz.presentation.ui.screen

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
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
import safeme.uz.utils.snackMessage

@AndroidEntryPoint
class AnnouncementPagerScreen : Fragment(R.layout.screen_announcement_pager) {
    private val binding: ScreenAnnouncementPagerBinding by viewBinding()
    private val viewModel: AnnouncementViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loadData()

    }


    private fun loadData() {
        val argument = requireArguments().getInt("key")
        viewModel.apply {
            getAllNewsByCategory(argument.toString())
            getAllNewsLiveData.observe(viewLifecycleOwner,newsObserver)
        }

    }

    private val newsObserver = Observer<AnnouncementResult<AnnouncementCategoryResponse<ArrayList<NewsData>>>>{
        when(it){
            is AnnouncementResult.Success ->initRecyclerView(it.data?.body!!)
            is AnnouncementResult.Error -> snackMessage(it.message!!)
            else -> {}
        }
    }


    private fun initRecyclerView(newsList:ArrayList<NewsData>){
        val rvAdapter = NewsRecyclerAdapter()
        rvAdapter.differ.submitList(newsList)
        binding.announcementsRv.adapter = rvAdapter
        binding.announcementsRv.layoutManager = LinearLayoutManager(requireContext())

    }

}
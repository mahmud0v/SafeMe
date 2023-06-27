package safeme.uz.presentation.ui.screen

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.remote.response.GameRecommendationResponse
import safeme.uz.data.remote.response.RecommendationInfo
import safeme.uz.data.remote.response.RecommendationResponse
import safeme.uz.databinding.ScreenAnnouncementsInfoBinding
import safeme.uz.presentation.viewmodel.announcement.AnnouncementInfoViewModel
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel
import safeme.uz.utils.AnnouncementResult
import safeme.uz.utils.Keys
import safeme.uz.utils.gone
import safeme.uz.utils.isConnected
import safeme.uz.utils.snackMessage
import safeme.uz.utils.visible

@AndroidEntryPoint
class AnnouncementInfoScreen : Fragment(R.layout.screen_announcements_info) {
    private val binding: ScreenAnnouncementsInfoBinding by viewBinding()
    private val viewModel: AnnouncementInfoViewModel by viewModels()
    private val remindViewModel: RemindListenerViewModel by viewModels()
    private val mySafeArgs: AnnouncementInfoScreenArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeData()
        backClickEvent()
    }

    private fun backClickEvent() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }


    private fun observeData() {
        remindViewModel.remindInFragment(true)
        val destinationArguments = mySafeArgs.desArg
        if (isConnected()) {
            when (destinationArguments.key) {
                Keys.ANNOUNCEMENTS -> {
                    viewModel.getNewsById(destinationArguments.id)
                    viewModel.getNewsByIdLiveData.observe(viewLifecycleOwner, newsObserver)
                }

                Keys.RECOMMENDATION -> {
                    viewModel.getRecommendationById(destinationArguments.id)
                    viewModel.getRecommendationLiveData.observe(
                        viewLifecycleOwner,
                        recommendationObserver
                    )
                }

                Keys.GAME -> {
                    viewModel.getGameById(destinationArguments.id)
                    viewModel.getGameByIdLiveData.observe(viewLifecycleOwner, gameObserver)
                }
            }
        } else {
            snackMessage(Keys.INTERNET_FAIL)
        }

    }

    private val newsObserver =
        Observer<AnnouncementResult<AnnouncementCategoryResponse<NewsData>>>() {
            when (it) {
                is AnnouncementResult.Success -> loadNews(it.data?.body!!)
                is AnnouncementResult.Error -> snackMessage(it.message!!)
                is AnnouncementResult.Loading -> binding.progress.visible()
            }
        }

    private val recommendationObserver = Observer<AnnouncementResult<RecommendationResponse>> {
        when (it) {
            is AnnouncementResult.Success -> loadRecommendation(it.data?.body!!)
            is AnnouncementResult.Loading -> binding.progress.visible()
            is AnnouncementResult.Error -> snackMessage(it.message!!)
        }
    }

    private val gameObserver =
        Observer<AnnouncementResult<ApiResponse<GameRecommendationResponse>>> {
            when (it) {
                is AnnouncementResult.Success -> loadGame(it.data?.body)
                is AnnouncementResult.Loading -> {
                    binding.progress.show()
                }

                is AnnouncementResult.Error -> {
                    binding.progress.hide()
                    snackMessage(it.data?.message!!)
                }
            }
        }

    private fun loadNews(newsData: NewsData) {
        binding.progress.gone()
        binding.apply {
            Glide.with(root).load(newsData.image).into(imageId)
            newsTitle.text = newsData.title
            newsDesc.text = newsData.content
            dateText.text = trimDate(newsData.created_date!!)

        }
    }

    private fun loadRecommendation(recommendationInfo: RecommendationInfo) {
        binding.progress.gone()
        binding.apply {
            Glide.with(root).load(recommendationInfo.image).into(imageId)
            newsTitle.text = recommendationInfo.title ?: ""
            newsDesc.text = recommendationInfo.text ?: ""
            dateText.text = trimDate(recommendationInfo.created_date) ?: ""
        }
    }

    private fun loadGame(gameRecommendationResponse: GameRecommendationResponse?) {
        binding.progress.hide()
        gameRecommendationResponse?.let {
            it.image?.let { url ->
                Glide.with(binding.root).load(url).into(binding.imageId)
                binding.newsTitle.text = gameRecommendationResponse.name ?: ""
                binding.newsDesc.text = gameRecommendationResponse.description ?: ""
                binding.dateText.text = trimDate(gameRecommendationResponse.createdDate) ?: ""
            }
        }

    }

    private fun trimDate(date: String?): String? {
        return date?.substring(0, 10)
    }


}
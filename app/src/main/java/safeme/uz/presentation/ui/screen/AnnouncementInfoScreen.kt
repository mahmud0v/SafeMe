package safeme.uz.presentation.ui.screen

import android.annotation.SuppressLint
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
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.NewsData
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.remote.response.GameRecommendationResponse
import safeme.uz.data.remote.response.RecommendationInfo
import safeme.uz.data.remote.response.RecommendationResponse
import safeme.uz.databinding.ScreenAnnouncementsInfoBinding
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.viewmodel.announcement.AnnouncementInfoViewModel
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.LocalHelper
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.gone
import safeme.uz.utils.isConnected
import safeme.uz.utils.trimDate
import safeme.uz.utils.visible

@AndroidEntryPoint
class AnnouncementInfoScreen : Fragment(R.layout.screen_announcements_info) {
    private val binding: ScreenAnnouncementsInfoBinding by viewBinding()
    private val viewModel: AnnouncementInfoViewModel by viewModels()
    private val remindViewModel: RemindListenerViewModel by viewModels()
    private val mySafeArgs: AnnouncementInfoScreenArgs by navArgs()
    private val appSharedPreference by lazy { AppSharedPreference(requireContext()) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeData()
        backClickEvent()
        checkLang()
    }

    private fun backClickEvent() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun observeData() {
        remindViewModel.remindInFragment(true)
        binding.newsDesc.settings.javaScriptEnabled = true
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
            binding.progress.gone()
            val messageDialog = MessageDialog(getString(R.string.no_data))
            messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
        }

    }

    private val newsObserver =
        Observer<RemoteApiResult<AnnouncementCategoryResponse<NewsData>>>() {
            when (it) {
                is RemoteApiResult.Success -> {
                    binding.progress.hide()
                    loadNews(it.data?.body!!)
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

    private val recommendationObserver = Observer<RemoteApiResult<RecommendationResponse>> {
        when (it) {
            is RemoteApiResult.Success -> {
                binding.progress.hide()
                loadRecommendation(it.data?.body!!)
            }

            is RemoteApiResult.Loading -> {
                binding.progress.visible()
            }

            is RemoteApiResult.Error -> {
                binding.progress.hide()
                val messageDialog = MessageDialog(it.message)
                messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
            }
        }
    }


    private val gameObserver =
        Observer<RemoteApiResult<ApiResponse<GameRecommendationResponse>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    binding.progress.hide()
                    loadGame(it.data?.body)
                }

                is RemoteApiResult.Loading -> {
                    binding.progress.show()
                }

                is RemoteApiResult.Error -> {
                    binding.progress.hide()
                    val messageDialog = MessageDialog(it.message)
                    messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                }
            }
        }

    private fun loadNews(newsData: NewsData) {
        binding.progress.gone()
        binding.apply {
            Glide.with(root).load(newsData.image).into(imageId)
            newsTitle.text = newsData.title
            newsData.content?.let {
                newsDesc.loadDataWithBaseURL(null, it, "text/html", "UTF-8", "about:blank")
            }
            dateLayout.visible()
            viewedLayout.visible()
            dateText.text = newsData.created_date.trimDate() ?: ""
            viewedText.text = newsData.views.toString()

        }
    }

    private fun checkLang() {
        when (appSharedPreference.locale) {
            "uz" -> LocalHelper.changeLanguage("uz-rUz", requireContext())
            "ru" -> LocalHelper.changeLanguage("ru", requireContext())
            "sr" -> LocalHelper.changeLanguage("uz", requireContext())
            "en" -> LocalHelper.changeLanguage("en", requireContext())
        }
    }

    private fun loadRecommendation(recommendationInfo: RecommendationInfo) {
        binding.progress.gone()
        binding.apply {
            Glide.with(root).load(recommendationInfo.image).into(imageId)
            newsTitle.text = recommendationInfo.title ?: ""
            recommendationInfo.text?.let {
                newsDesc.loadDataWithBaseURL(null, it, "text/html", "UTF-8", "about:blank")
            }
            dateLayout.gone()
            viewedLayout.gone()
        }
    }

    private fun loadGame(gameRecommendationResponse: GameRecommendationResponse?) {
        binding.progress.hide()
        gameRecommendationResponse?.let {
            it.image?.let { url ->
                Glide.with(binding.root).load(url).into(binding.imageId)
                binding.newsTitle.text = gameRecommendationResponse.name ?: ""
                gameRecommendationResponse.description?.let {
                    binding.newsDesc.loadDataWithBaseURL(
                        null,
                        it,
                        "text/html",
                        "UTF-8",
                        "about:blank"
                    )
                }
            }
        }
        binding.dateLayout.gone()
        binding.viewedLayout.gone()


    }

//    private fun trimDate(date: String?): String? {
//        return date?.substring(0, 10)
//    }


}
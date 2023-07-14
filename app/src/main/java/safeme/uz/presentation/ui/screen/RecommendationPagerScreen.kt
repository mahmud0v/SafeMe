package safeme.uz.presentation.ui.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.DestinationArguments
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.request.RecommendationRequest
import safeme.uz.data.remote.response.AgeCategoryResponse
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.remote.response.RecommendationInfo
import safeme.uz.data.remote.response.RecommendationInfoResponse
import safeme.uz.databinding.ScreenRecommendPagerBinding
import safeme.uz.presentation.ui.adapter.RecommendationAdapter
import safeme.uz.presentation.ui.adapter.RecommendationInfoAdapter
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.ui.screen.main.RecommendationsScreenDirections
import safeme.uz.presentation.viewmodel.recommendation.RecommendationPagerViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.MarginItemDecoration
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.gone
import safeme.uz.utils.isConnected
import safeme.uz.utils.visible

@AndroidEntryPoint
class RecommendationPagerScreen : Fragment(R.layout.screen_recommend_pager) {
    private val binding: ScreenRecommendPagerBinding by viewBinding()
    private val viewModel: RecommendationPagerViewModel by viewModels()
    private val recommendationAdapter by lazy { RecommendationAdapter(Keys.RECOMMENDATION) }
    private val recommendationInfoAdapter by lazy { RecommendationInfoAdapter() }
    private var isHaveRecList = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        loadData()
        clickEvent()
    }

    private fun loadData() {
        val category = requireArguments().getInt(Keys.KEY_VALUE)
        if (isConnected()) {
            loadViews(category)
        } else {
            val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
            messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
        }
    }

    private fun loadViews(category: Int) {
        viewModel.recommendationAllCategoriesLiveData.observe(
            viewLifecycleOwner,
            recommendationObserves
        )
        viewModel.getRecommendationInfoByCategory(AgeCategoryRequest(category))
        viewModel.recommendationInfoLiveData.observe(
            viewLifecycleOwner,
            recommendationInfoObserver
        )
    }


    private val recommendationObserves =
        Observer<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>> {
            when (it) {
                is RemoteApiResult.Success -> setDataRecommends(it.data?.body)
                is RemoteApiResult.Loading -> binding.progress.visible()
                is RemoteApiResult.Error -> {
                    binding.progress.gone()
                    val messageDialog = MessageDialog(it.message)
                    messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                }
            }
        }


    private val recommendationInfoObserver =
        Observer<RemoteApiResult<RecommendationInfoResponse>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    binding.placeHolder.gone()
                    setRecommendsAgeInfo(it.data?.body)
                }

                is RemoteApiResult.Loading -> {
                    binding.placeHolder.gone()
                    binding.progress.visible()
                }

                is RemoteApiResult.Error -> {
                    binding.progress.gone()
                    if (it.message == getString(R.string.no_data)) {
                        binding.placeHolder.visible()
                    } else {
                        val messageDialog = MessageDialog(it.message)
                        messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                    }
                }
            }
        }

    private fun initView() {
        binding.rec1Rv.adapter = recommendationAdapter
        binding.rec1Rv.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.rec1Rv.addItemDecoration(MarginItemDecoration())
        binding.rec2Rv.adapter = recommendationInfoAdapter
        binding.rec2Rv.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.rec2Rv.addItemDecoration(MarginItemDecoration())

    }

    private fun setDataRecommends(list: ArrayList<CategoriesData>?) {
        binding.progress.gone()
        recommendationAdapter.differ.submitList(list)
        recommendationAdapter.onItemClick = {
            it.id?.let {
                viewModel.getRecommendationByCategory(RecommendationRequest(it))
                viewModel.recommendationByCategoryLiveData.observe(
                    viewLifecycleOwner,
                    recommendationByCategoryObserver
                )
            }
        }
    }

    private val recommendationByCategoryObserver =
        Observer<RemoteApiResult<AgeCategoryResponse<RecommendationInfo>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    binding.placeHolder.gone()
                    setRecommendsCategoryInfo(it.data?.body)
                }

                is RemoteApiResult.Error -> {
                    if (it.message == getString(R.string.no_data)) {
                        binding.placeHolder.visible()
                    } else {
                        val messageDialog = MessageDialog(it.message!!)
                        messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                    }
                    setRecommendsCategoryInfo(null)
                }

                is RemoteApiResult.Loading -> {
                    binding.progress.visible()
                    binding.placeHolder.gone()
                }
            }
        }

    private fun setRecommendsAgeInfo(list: ArrayList<RecommendationInfo>?) {
        binding.progress.gone()
        recommendationInfoAdapter.differ.submitList(list)
        isHaveRecList = true
    }


    private fun setRecommendsCategoryInfo(list: ArrayList<RecommendationInfo>?) {
        binding.progress.gone()
        if (isHaveRecList) {
            recommendationInfoAdapter.differ.submitList(list)
        }
    }

    private fun clickEvent() {
        recommendationInfoAdapter.onItemClick = {
            findNavController().navigate(
                RecommendationsScreenDirections.actionRecommendationsToAnnouncementInfoScreen(
                    DestinationArguments(it.id!!, Keys.RECOMMENDATION)
                )
            )
        }
    }
}

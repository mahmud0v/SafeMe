package safeme.uz.presentation.ui.screen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.DestinationArguments
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.request.AgeCatRequest
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
import safeme.uz.utils.MarginCategoryItemDecoration
import safeme.uz.utils.MarginRulesItemDecoration
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
            binding.progress.gone()
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
                    binding.placeHolder.visible()
                    Toast.makeText(requireContext(),"${it.message},${getString(R.string.not_found)}",Toast.LENGTH_SHORT).show()
                    if (it.message == getString(R.string.not_found)) {
                        binding.placeHolder.visible()
                    } else {
                        val messageDialog = MessageDialog(getString(R.string.some_error_occurred))
                        messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                    }

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
                    binding.placeHolder.visible()
                    if (it.message != getString(R.string.not_found)) {
                        val messageDialog = MessageDialog(getString(R.string.some_error_occurred))
                        messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                    }

                }
            }
        }

    private fun initView() {
        binding.rec1Rv.adapter = recommendationAdapter
        binding.rec1Rv.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.rec1Rv.addItemDecoration(MarginCategoryItemDecoration())
        binding.rec2Rv.adapter = recommendationInfoAdapter
        binding.rec2Rv.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.rec2Rv.addItemDecoration(MarginRulesItemDecoration())

    }

    private fun setDataRecommends(list: ArrayList<CategoriesData>?) {
        binding.progress.gone()
        recommendationAdapter.differ.submitList(list)
        recommendationAdapter.onItemClick = {
            it.id?.let { category ->
                val ageCategoryId = requireArguments().getInt(Keys.KEY_VALUE)
                val recAgeCatRequest = AgeCatRequest(ageCategoryId, category)
                viewModel.getRecAgeCat(recAgeCatRequest)
                viewModel.getRecAgeCatLiveData.observe(viewLifecycleOwner,recommendationByAgeCategoryObserver)

            }
        }
    }


    private val recommendationByAgeCategoryObserver =
        Observer<RemoteApiResult<ApiResponse<ArrayList<RecommendationInfo>>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    binding.placeHolder.gone()
                    binding.progress.gone()
                    recommendationInfoAdapter.differ.submitList(it.data?.body)

                }

                is RemoteApiResult.Error -> {
                    binding.progress.gone()
                    binding.placeHolder.visible()
                    if (it.message != getString(R.string.not_found)) {
                        val messageDialog = MessageDialog(getString(R.string.some_error_occurred))
                        messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                    }
                    recommendationInfoAdapter.differ.submitList(null)
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

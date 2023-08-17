package safeme.uz.presentation.ui.screen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.remote.request.AgeCatRequest
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.request.GameBookmarkRequest
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.remote.response.GameBookmarkResponse
import safeme.uz.data.remote.response.GameRecommendationResponse
import safeme.uz.databinding.GamePagerScreenBinding
import safeme.uz.presentation.ui.adapter.GameRecommendationAdapter
import safeme.uz.presentation.ui.adapter.RecommendationAdapter
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.ui.screen.main.GameScreenDirections
import safeme.uz.presentation.viewmodel.game.GamePagerViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.MarginCategoryItemDecoration
import safeme.uz.utils.MarginGameItemDecoration
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.gone
import safeme.uz.utils.visible

@AndroidEntryPoint
class GamerPagerScreen : Fragment(R.layout.game_pager_screen) {
    private val binding: GamePagerScreenBinding by viewBinding()
    private val viewModel: GamePagerViewModel by viewModels()
    private val recommendationAdapter by lazy { RecommendationAdapter(Keys.GAME) }
    private lateinit var gameRecAdapter: GameRecommendationAdapter
    private val appSharedPreference by lazy { AppSharedPreference(requireContext()) }
    private var currentCategoryId: Int = -1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadGameCategories()
        initRecyclerView()
        saveStarState()
        loadCategories()
        recyclerItemClickEvent()
        clickStateEvent()
    }

    private fun initRecyclerView() {
        gameRecAdapter = GameRecommendationAdapter(appSharedPreference.bookmarkGame)
        binding.gamesRv1.adapter = recommendationAdapter
        binding.gamesRv1.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.gamesRv1.addItemDecoration(MarginCategoryItemDecoration())
        binding.gamesRv2.adapter = gameRecAdapter
        binding.gamesRv2.layoutManager =
            LinearLayoutManager(requireContext())
        binding.gamesRv2.addItemDecoration(MarginGameItemDecoration())


    }

    private fun loadGameCategories() {
        viewModel.gameRecommendationLiveData.observe(viewLifecycleOwner, gameCategoriesObserver)
    }

    private val gameCategoriesObserver =
        Observer<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    binding.progress.hide()
                    binding.placeHolder.gone()
                    recommendationAdapter.differ.submitList(it.data?.body)

                }

                is RemoteApiResult.Loading -> {
                    binding.progress.show()
                }

                is RemoteApiResult.Error -> {
                    binding.progress.hide()
                    binding.placeHolder.visible()
                    if (it.message != getString(R.string.not_found)) {
                        Toast.makeText(requireContext(),"${it.message}, ${getString(R.string.not_found)}",Toast.LENGTH_SHORT).show()
                        val messageDialog = MessageDialog(getString(R.string.some_error_occurred))
                        messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                    }
                }
            }
        }


    private fun loadCategories() {
        if (appSharedPreference.bookmarkGame) {
            if (currentCategoryId != -1) {
                allBookmarkGame()
            } else {
                allBookmarkGameByAgeCategory()
            }
        } else {
            if (currentCategoryId != -1) {
                unBookmarkedGame()
            } else {
                unBookmarkedGameByAgeCategory()
            }
        }

    }

    private fun saveStarState() {
        val startState = appSharedPreference.bookmarkGame
        if (startState) {
            binding.headStar.setImageResource(R.drawable.star_yellow)
        } else {
            binding.headStar.setImageResource(R.drawable.white_star)
        }
    }


    private fun unBookmarkedGame() {
        val currentAgeId = requireArguments().getInt("key")
        viewModel.allGameUnBookmark(AgeCatRequest(currentAgeId, currentCategoryId))
        viewModel.allGameUnBookmarkedLiveData.observe(
            viewLifecycleOwner,
            unBookmarkedObserver
        )
    }

    private fun unBookmarkedGameByAgeCategory() {
        val currentAgeId = requireArguments().getInt("key")
        viewModel.allGameUnBookmarkByAgeCategory(AgeCategoryRequest(currentAgeId))
        viewModel.allGameUnBookmarkedByAgeCategoryLiveData.observe(
            viewLifecycleOwner,
            unBookmarkedByAgeObserver
        )


    }

    private fun clickStateEvent() {
        binding.headStar.setOnClickListener {
            val starSaveState = appSharedPreference.bookmarkGame
            if (starSaveState) {
                if (currentCategoryId != -1) {
                    unBookmarkedGame()
                } else {
                    unBookmarkedGameByAgeCategory()
                }

                binding.headStar.setImageResource(R.drawable.white_star)
                appSharedPreference.bookmarkGame = false
            } else {

                if (currentCategoryId != -1) {
                    allBookmarkGame()
                } else {
                    allBookmarkGameByAgeCategory()
                }

                binding.headStar.setImageResource(R.drawable.star_yellow)
                appSharedPreference.bookmarkGame = true
            }

        }
    }


    private fun recyclerItemClickEvent() {
        recommendationAdapter.onItemClick = { categoryData ->
            categoryData.id?.let { id ->
                currentCategoryId = id
                if (appSharedPreference.bookmarkGame) {
                    if (currentCategoryId != -1) {
                        allBookmarkGame()
                    } else {
                        allBookmarkGameByAgeCategory()
                    }
                } else {
                    if (currentCategoryId != -1) {
                        unBookmarkedGame()
                    } else {
                        unBookmarkedGameByAgeCategory()
                    }
                }
            }
        }

        gameRecAdapter.onItemClick = {
            it.id?.let { id ->
                val action = GameScreenDirections.actionGameToGameInfoScreen(
                    id
                )
                findNavController().navigate(action)
            }
        }

        gameRecAdapter.onStarItemClick = { gameBookmarkState ->
            gameBookmarkState.id?.let {
                if (gameBookmarkState.bookmarkState) {
                    viewModel.gameItemBookmark(GameBookmarkRequest(it))
                    viewModel.gameItemBookmarkLiveData.observe(
                        viewLifecycleOwner,
                        gameItemBookmarkObserver
                    )
                } else {
                    viewModel.gameItemDeleteBookmark(GameBookmarkRequest(it))
                    viewModel.gameItemDeleteBookmarkLiveData.observe(
                        viewLifecycleOwner,
                        gameItemDeleteBookmarkObserver
                    )
                }
            }
        }

    }

    private val gameItemDeleteBookmarkObserver = Observer<RemoteApiResult<ApiResponse<Nothing>>> {}


    private val gameItemBookmarkObserver =
        Observer<RemoteApiResult<ApiResponse<GameBookmarkResponse>>> {}

    private fun allBookmarkGame() {
        val currentAgeId = requireArguments().getInt("key")
        viewModel.allGameBookmark(currentAgeId, currentCategoryId)
        viewModel.allGameBookmarkLiveData.observe(viewLifecycleOwner, allGameBookmarkObserver)
    }

    private fun allBookmarkGameByAgeCategory() {
        val currentAgeId = requireArguments().getInt("key")
        viewModel.allGameBookmarkByAgeCategory(currentAgeId)
        viewModel.allGameBookmarkedByAgeCategoryLiveData.observe(
            viewLifecycleOwner,
            allGameBookmarkByAgeObserver
        )
    }


    private val allGameBookmarkObserver =
        Observer<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    binding.progress.hide()
                    binding.placeHolder.gone()
                    gameRecAdapter.differ.submitList(it.data?.body)
                    gameRecAdapter.setResultBookmark(true)
                }

                is RemoteApiResult.Error -> {
                    binding.progress.hide()
                    binding.placeHolder.visible()
                    if (it.message != getString(R.string.not_found)) {
                        val messageDialog = MessageDialog(getString(R.string.some_error_occurred))
                        messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                    }
                    gameRecAdapter.differ.submitList(null)
                }

                is RemoteApiResult.Loading -> {
                    binding.progress.visible()
                    binding.placeHolder.gone()
                }
            }
        }

    private val allGameBookmarkByAgeObserver =
        Observer<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    binding.progress.hide()
                    binding.placeHolder.gone()
                    gameRecAdapter.differ.submitList(it.data?.body)
                    gameRecAdapter.setResultBookmark(true)
                }

                is RemoteApiResult.Error -> {
                    binding.progress.hide()
                    binding.placeHolder.visible()
                    if (it.message != getString(R.string.not_found)) {
                        val messageDialog = MessageDialog(getString(R.string.some_error_occurred))
                        messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                    }
                    gameRecAdapter.differ.submitList(null)
                }

                is RemoteApiResult.Loading -> {
                    binding.progress.visible()
                    binding.placeHolder.gone()
                }
            }
        }

    private val unBookmarkedObserver =
        Observer<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    binding.progress.hide()
                    binding.placeHolder.gone()
                    gameRecAdapter.differ.submitList(it.data?.body)
                    gameRecAdapter.setResultBookmark(false)
                }

                is RemoteApiResult.Error -> {
                    binding.progress.hide()
                    binding.placeHolder.visible()
                    if (it.message != getString(R.string.not_found)) {
                        val messageDialog = MessageDialog(it.message!!)
                        messageDialog.show(
                            requireActivity().supportFragmentManager,
                            Keys.DIALOG
                        )
                    }
                    gameRecAdapter.differ.submitList(emptyList())
                }

                is RemoteApiResult.Loading -> {
                    binding.progress.show()
                }
            }
        }

    private val unBookmarkedByAgeObserver =
        Observer<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    binding.progress.hide()
                    binding.placeHolder.gone()
                    gameRecAdapter.differ.submitList(it.data?.body)
                    gameRecAdapter.setResultBookmark(false)

                }

                is RemoteApiResult.Error -> {
                    binding.progress.hide()
                    binding.placeHolder.visible()
                    if (it.message != getString(R.string.not_found)) {
                        binding.placeHolder.gone()
                        val messageDialog = MessageDialog(it.message!!)
                        messageDialog.show(
                            requireActivity().supportFragmentManager,
                            Keys.DIALOG
                        )
                    }
                    gameRecAdapter.differ.submitList(null)
                }

                is RemoteApiResult.Loading -> {
                    binding.progress.show()

                }
            }
        }


}
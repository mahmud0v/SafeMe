package safeme.uz.presentation.ui.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.CategoriesData
import safeme.uz.data.model.DestinationArguments
import safeme.uz.data.model.ManageScreen
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.request.RecommendationRequest
import safeme.uz.data.remote.response.AgeCategoryInfo
import safeme.uz.data.remote.response.AgeCategoryResponse
import safeme.uz.data.remote.response.AnnouncementCategoryResponse
import safeme.uz.data.remote.response.GameRecommendationResponse
import safeme.uz.databinding.ScreenGameBinding
import safeme.uz.presentation.ui.adapter.GameRecommendationAdapter
import safeme.uz.presentation.ui.adapter.RecommendationAdapter
import safeme.uz.presentation.viewmodel.announcement.RemindListenerViewModel
import safeme.uz.presentation.viewmodel.game.GameScreenViewModel
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.Keys
import safeme.uz.utils.MarginItemDecoration
import safeme.uz.utils.backPressDispatcher
import safeme.uz.utils.snackMessage

@AndroidEntryPoint
class GameScreen : Fragment(R.layout.screen_game) {
    private val binding: ScreenGameBinding by viewBinding()
    private val backRemindedViewModel: RemindListenerViewModel by activityViewModels()
    private val viewModel: GameScreenViewModel by viewModels()
    private val recommendationAdapter by lazy { RecommendationAdapter(Keys.GAME) }
    private val gameRecAdapter by lazy { GameRecommendationAdapter() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadAgeCategory()
        backListenerEvent()
        initRecyclerView()
        loadGameCategories()
        recyclerItemClickEvent()
        moveToProfile()
        moveToSOS()
        backPressDispatcher()

    }

    private fun loadAgeCategory() {
        viewModel.ageCategoryLiveData.observe(viewLifecycleOwner,ageCategoryObserver)
    }

    private fun initRecyclerView() {
        binding.gamesRv1.adapter = recommendationAdapter
        binding.gamesRv1.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.gamesRv1.addItemDecoration(MarginItemDecoration())
        binding.gamesRv2.adapter = gameRecAdapter
        binding.gamesRv2.layoutManager =
            LinearLayoutManager(requireContext())

    }

    private fun initTabLayout(ageCategoryList: ArrayList<AgeCategoryInfo>?) {
        ageCategoryList?.let {
            for (i in ageCategoryList) {
                val tab = binding.tabLayout.newTab()
                tab.text = i.title
                binding.tabLayout.addTab(tab)
            }
        }


        ageCategoryList?.let {
            viewModel.getGameRecommendationByAge(AgeCategoryRequest(it[0].id))
            viewModel.gameRecByAgeLiveData.observe(
                viewLifecycleOwner,
                Observer {
                    when (it) {
                        is RemoteApiResult.Success -> {
                            gameRecAdapter.differ.submitList(it.data?.body)
                            binding.progress.hide()
                        }

                        is RemoteApiResult.Loading -> {
                            binding.progress.show()
                        }

                        is RemoteApiResult.Error -> {
                            binding.progress.hide()
                            gameRecAdapter.differ.submitList(emptyList())

                        }
                    }
                }
            )
        }


        ageCategoryList?.let { categoryList ->
            binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        val ageCategoryInfo = categoryList[tab.position]
                        viewModel.getGameRecommendationByAge(AgeCategoryRequest(ageCategoryInfo.id))
                        viewModel.gameRecByAgeLiveData.observe(
                            viewLifecycleOwner,
                            gameRecommendationObserver
                        )
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })
        }


    }

    private fun loadCategory(categoryList: ArrayList<GameRecommendationResponse>?) {
        val oldList = gameRecAdapter.differ.currentList.toMutableList()
        val newList = ArrayList<GameRecommendationResponse>()
        for (i in oldList.indices) {
            val gameData = oldList[i]
            categoryList?.let {
                if (categoryList.contains(gameData)) {
                    newList.add(gameData)
                }
            }
        }
        gameRecAdapter.differ.submitList(newList)

    }

    private val ageCategoryObserver =
        Observer<RemoteApiResult<AgeCategoryResponse<AgeCategoryInfo>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    initTabLayout(it.data?.body)
                }

                is RemoteApiResult.Error -> {
                    snackMessage(it.data?.message!!)
                }

                else -> {}
            }
        }

    private fun backListenerEvent() {
        binding.ivMenu.setOnClickListener {
            backRemindedViewModel.remindInFragment(true)
        }
    }

    private fun loadGameCategories() {
        viewModel.gameRecommendationLiveData.observe(viewLifecycleOwner, gameCategoriesObserver)
    }

    private val gameCategoriesObserver =
        Observer<RemoteApiResult<AnnouncementCategoryResponse<ArrayList<CategoriesData>>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    binding.progress.hide()
                    recommendationAdapter.differ.submitList(it.data?.body)
                }

                is RemoteApiResult.Loading -> {
                    binding.progress.show()
                }

                is RemoteApiResult.Error -> {
                    binding.progress.hide()
                    snackMessage(it.data?.message!!)
                }
            }
        }

    private val gameRecommendationObserver =
        Observer<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    binding.progress.hide()
                    gameRecAdapter.differ.submitList(it.data?.body)
                }

                is RemoteApiResult.Loading -> {
                    binding.progress.show()
                }

                is RemoteApiResult.Error -> {
                    binding.progress.hide()
                    gameRecAdapter.differ.submitList(emptyList())

                }
            }
        }

    private fun recyclerItemClickEvent() {
        recommendationAdapter.onItemClick = { categoryData ->
            categoryData.id?.let { id ->
                viewModel.getGameRecommendationByCategory(RecommendationRequest(id))
                viewModel.gameRecByCategoryLiveData.observe(
                    viewLifecycleOwner,
                    gameCategoryObserver
                )
            }
        }

        gameRecAdapter.onItemClick = {
            it.id?.let { id ->
                val action = GameScreenDirections.actionGameToAnnouncementInfoScreen(
                    DestinationArguments(id, Keys.GAME)
                )
                findNavController().navigate(action)
            }
        }

        gameRecAdapter.shareBtnClick = {
//            val intent = Intent().apply {
//                action = Intent.ACTION_SEND
//                putExtra(Intent.EXTRA_TEXT,"http://cyberpolice.uz/uz/api/v1.0/games/view/1")
//                type = "text/plain"
//            }
//            startActivity(Intent.createChooser(intent,null))
        }
    }

    private val gameCategoryObserver =
        Observer<RemoteApiResult<ApiResponse<ArrayList<GameRecommendationResponse>>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    binding.progress.hide()
                    loadCategory(it.data?.body)
                }

                is RemoteApiResult.Loading -> {
                    binding.progress.show()
                }

                is RemoteApiResult.Error -> {
                    binding.progress.hide()
                    snackMessage(it.data?.message!!)
                }
            }
        }

    private fun moveToProfile() {
        binding.ivProfile.setOnClickListener {
            val manageScreen = ManageScreen(Keys.GAME_SCREEN, Keys.PROFILE_TO_EDIT)
            val bundle = Bundle().apply {
                putSerializable(Keys.BUNDLE_KEY, manageScreen)
            }
            findNavController().navigate(R.id.action_game_to_profileScreen,bundle)
        }
    }

    private fun moveToSOS(){
        binding.ivSOS.setOnClickListener {
            val action = GameScreenDirections.actionGameToSosScreen()
            findNavController().navigate(action)
        }

    }


}
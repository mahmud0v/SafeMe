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
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.response.GameRecommendationResponse
import safeme.uz.databinding.GamingInfoScreenBinding
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.viewmodel.game.GameInfoScreenViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.gone
import safeme.uz.utils.isConnected
import safeme.uz.utils.trimDate
import safeme.uz.utils.visible

@AndroidEntryPoint
class GameInfoScreen : Fragment(R.layout.gaming_info_screen) {
    private val binding: GamingInfoScreenBinding by viewBinding()
    private val viewModel: GameInfoScreenViewModel by viewModels()
    private val navArgs: GameInfoScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadGaming()
        backClickEvent()

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadGaming() {
        binding.gamingRecommendation.settings.javaScriptEnabled = true
        if (isConnected()) {
            viewModel.getGameById(navArgs.destionationArguments)
            viewModel.getGameByIdLiveData.observe(viewLifecycleOwner, gameObserver)
        } else {
            binding.progress.gone()
            val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
            messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
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
                    binding.placeHolder.visible()
                    binding.headCard.gone()
                    val messageDialog = MessageDialog(it.message)
                    messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                }
            }
        }

    private fun loadGame(gameRecommendationResponse: GameRecommendationResponse?) {
        binding.headCard.visible()
        binding.progress.hide()
        binding.placeHolder.gone()
        gameRecommendationResponse?.let {
            it.image?.let {
                Glide.with(binding.imageId).load(it).into(binding.imageId)
            }
            binding.gamingName.text = it.name ?: ""
            binding.gamingDesc.text = it.description ?: ""
            it.recommendation?.let {
                binding.gamingRecommendation.loadDataWithBaseURL(null,it,"text/html","UTF-8","about:blank")
            }
            binding.dateLayout.visible()
            binding.gamingOwner.text = it.developerCompany ?: ""
            binding.dateText.text = it.createdDate.trimDate() ?: ""
        }


    }

    private fun backClickEvent() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }


}
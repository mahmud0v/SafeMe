package safeme.uz.presentation.ui.screen

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
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
import safeme.uz.data.remote.request.PollAnswerRequest
import safeme.uz.data.remote.response.PollAnswerResponse
import safeme.uz.data.remote.response.PollDetailResponse
import safeme.uz.databinding.ScreenPollDetailBinding
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.viewmodel.poll.PollDetailScreenViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.enable
import safeme.uz.utils.gone
import safeme.uz.utils.isConnected


@AndroidEntryPoint
class PollDetailScreen : Fragment(R.layout.screen_poll_detail) {
    private val binding: ScreenPollDetailBinding by viewBinding()
    private val viewModel: PollDetailScreenViewModel by viewModels()
    private val navArgs: PollDetailScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loadPollDetailData()
        backClickEvent()
        selectPollOption()


    }


    private fun loadPollDetailData() {
        val id = navArgs.pollId
        if (isConnected()) {
            viewModel.getPollById(id)
            viewModel.getPollByIdLiveData.observe(viewLifecycleOwner, pollObserver)
        } else {
            binding.progress.gone()
            val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
            messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
        }

    }

    private val pollObserver = Observer<RemoteApiResult<ApiResponse<PollDetailResponse>>> {
        when (it) {
            is RemoteApiResult.Success -> {
                binding.progress.hide()
                setDataPollOptions(it.data?.body)
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

    private fun setDataPollOptions(pollOptions: PollDetailResponse?) {

        pollOptions?.media?.let {
            Glide.with(binding.imageId).load(it).into(binding.imageId)
        }

        binding.pollsTitle.text = pollOptions?.text ?: ""
        pollOptions?.result?.let {
            for (i in it) {
                val radioButton = RadioButton(requireContext())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    radioButton.setButtonIcon(
                        Icon.createWithResource(
                            requireContext(),
                            R.drawable.radio_button_icon
                        )
                    )
                }
                radioButton.text = i.text ?: ""
                i.pollId?.let { id ->
                    radioButton.id = id
                }
                radioButton.setTextColor(Color.parseColor(Keys.radioButtonLabelColor))
                binding.radioGroup.addView(radioButton)
                radioButton.setPadding(22, 0, 0, 0)
                val typeFace = Typeface.createFromAsset(requireActivity().assets, Keys.fontDrawable)
                radioButton.typeface = typeFace
            }
        }
    }


    private fun backClickEvent() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun selectPollOption() {
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            binding.btnSave.enable()
        }

        binding.btnSave.setOnClickListener {
            val questionId = navArgs.pollId
            val optionId = binding.radioGroup.checkedRadioButtonId
            if (isConnected()){
                viewModel.giveAnswerPoll(
                    PollAnswerRequest(
                        question = questionId.toString(),
                        answer = optionId.toString()
                    )
                )
                viewModel.pollAnswerLiveData.observe(viewLifecycleOwner, pollAnswerObserver)
            }else {
                val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
                messageDialog.show(requireActivity().supportFragmentManager,Keys.DIALOG)
            }

        }
    }

    private val pollAnswerObserver = Observer<RemoteApiResult<ApiResponse<PollAnswerResponse>>> {
        when (it) {
            is RemoteApiResult.Success -> {
                binding.progress.hide()
                val messageDialog = MessageDialog(getString(R.string.successfully_done))
                messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                messageDialog.btnClickEvent = {
                    findNavController().popBackStack()
                }
            }

            is RemoteApiResult.Loading -> {
                binding.progress.show()
            }

            is RemoteApiResult.Error -> {
                binding.progress.hide()
                val messageDialog = MessageDialog(it.message!!)
                messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
            }
        }
    }


}
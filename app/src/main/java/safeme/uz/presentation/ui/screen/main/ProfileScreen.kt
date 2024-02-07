package safeme.uz.presentation.ui.screen.main


import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ManageScreen
import safeme.uz.data.remote.response.UserInfo
import safeme.uz.data.remote.response.UserResponse
import safeme.uz.databinding.ScreenProfileBinding
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.viewmodel.profileInfo.ProfileScreenViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.formatBirthDay
import safeme.uz.utils.gone
import safeme.uz.utils.isConnected
import safeme.uz.utils.visible

@AndroidEntryPoint
class ProfileScreen : Fragment(R.layout.screen_profile) {
    private val binding: ScreenProfileBinding by viewBinding()
    private val viewModel: ProfileScreenViewModel by viewModels()
    private var userInfoDetail: UserInfo? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadUserData()
        moveToProfileEditScreen()
        backEvent()
        editPassword()

    }

    private fun loadUserData() {
        if (isConnected()) {
            viewModel.getProfileInfo()
            viewModel.userInfoLiveData.observe(viewLifecycleOwner, userObserver)
        } else {
            binding.progress.gone()
            binding.littleProgress.gone()
            val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
            messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
        }

    }

    private val userObserver = Observer<RemoteApiResult<UserResponse>> {
        when (it) {
            is RemoteApiResult.Success -> initView(it.data?.body!!)
            is RemoteApiResult.Error -> {
                binding.progress.hide()
                binding.littleProgress.hide()
                val messageDialog = MessageDialog(it.message)
                messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
            }

            is RemoteApiResult.Loading -> binding.progress.visible()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView(userInfo: UserInfo) {
        binding.progress.gone()
        userInfoDetail = userInfo
        binding.apply {
            userInfo.photo?.let {
                Glide
                    .with(ivProfile)
                    .load(it)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.littleProgress.hide()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.littleProgress.hide()
                            return false
                        }

                    })
                    .into(ivProfile)

            }
            tvName.text = "${userInfo.firstName} ${userInfo.lastName}"
            tvDateOfBirth.text = userInfo.birthDay?.formatBirthDay() ?: ""
            tvRegion.text = userInfo.region ?: ""
            tvDistrict.text = userInfo.district ?: ""
            tvNeighborhood.text = userInfo.mahalla ?: ""
            tvGender.text = userInfo.gender ?: ""

        }
    }

    private fun moveToProfileEditScreen() {
        binding.btnEditProfile.setOnClickListener {
            val action =
                ProfileScreenDirections.actionProfileScreenToEditProfileScreen(userInfoDetail)
            findNavController().navigate(action)
        }
    }

    private fun backEvent() {
        binding.ivBack.setOnClickListener {
            val manageScreen = arguments?.getSerializable(Keys.BUNDLE_KEY) as ManageScreen
            when (manageScreen.hostScreen) {
                Keys.RECOMMENDATION_SCREEN -> findNavController().navigate(R.id.recommendations)
                Keys.ANNOUNCEMENT_SCREEN -> findNavController().navigate(R.id.announcements)
                Keys.INSPECTOR_SCREEN -> findNavController().navigate(R.id.prevention_inspector)
                Keys.GAME_SCREEN -> findNavController().navigate(R.id.game)
                Keys.ABOUT_SCREEN -> findNavController().navigate(R.id.about_us)
                Keys.APPEAL_SCREEN -> findNavController().navigate(R.id.appeals)
                Keys.POLL_PAGER -> findNavController().navigate(R.id.questionnaire)
                Keys.SETTINGS_SCREEN -> findNavController().navigate(R.id.settingsScreen)
                else -> {
                    findNavController().popBackStack()
                }
            }
        }
    }


    private fun editPassword() {
        binding.btnEditPassword.setOnClickListener {
            val manageScreen = arguments?.getSerializable(Keys.BUNDLE_KEY) as ManageScreen
            manageScreen.phoneNumber = userInfoDetail?.phone ?: ""
            val bundle = Bundle().apply {
                putSerializable(Keys.BUNDLE_KEY, manageScreen)
            }
            findNavController().navigate(R.id.action_profileScreen_to_passwordRecoverScreen, bundle)
        }
    }


}
package safeme.uz.presentation.ui.screen.main


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.remote.response.UserInfo
import safeme.uz.data.remote.response.UserResponse
import safeme.uz.databinding.ScreenProfileBinding
import safeme.uz.presentation.ui.screen.ProfileInfoScreenDirections
import safeme.uz.presentation.viewmodel.profileInfo.ProfileScreenViewModel
import safeme.uz.utils.AnnouncementResult
import safeme.uz.utils.formatBirthDay
import safeme.uz.utils.gone
import safeme.uz.utils.orderBirthDay
import safeme.uz.utils.snackMessage
import safeme.uz.utils.visible

@AndroidEntryPoint
class ProfileScreen : Fragment(R.layout.screen_profile) {
    private val binding: ScreenProfileBinding by viewBinding()
    private val viewModel: ProfileScreenViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadUserData()
        moveToProfileEditScreen()
        backEvent()
    }

    private fun loadUserData() {
        viewModel.getProfileInfo()
        viewModel.userInfoLiveData.observe(viewLifecycleOwner, userObserver)
    }

    private val userObserver = Observer<AnnouncementResult<UserResponse>> {
        when (it) {
            is AnnouncementResult.Success -> initView(it.data?.body!!)
            is AnnouncementResult.Error -> snackMessage(it.data?.message!!)
            is AnnouncementResult.Loading -> binding.progress.visible()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView(userInfo: UserInfo) {
        binding.progress.gone()
        binding.apply {
            userInfo.photo?.let { Glide.with(ivProfile).load(it).into(ivProfile) }
            tvName.text = "${userInfo.firstName} ${userInfo.lastName}"
            tvDateOfBirth.text = userInfo.birthDay?.formatBirthDay() ?: ""
            tvRegion.text = userInfo.region ?: ""
            tvDistrict.text = userInfo.district ?: ""
            tvNeighborhood.text = userInfo.mahalla ?: ""
            tvGender.text = userInfo.gender ?: ""
        }
    }

    private fun moveToProfileEditScreen() {
        val action = ProfileScreenDirections.actionProfileScreenToEditProfileScreen()
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(action)
        }
    }

    private fun backEvent() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }


}
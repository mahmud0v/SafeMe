package safeme.uz.presentation.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import safeme.uz.R
import safeme.uz.data.remote.request.UserDataRequest
import safeme.uz.data.remote.response.Address
import safeme.uz.data.remote.response.UserDataResponse
import safeme.uz.databinding.ScreenProfileInfoBinding
import safeme.uz.presentation.ui.dialog.AddressDialog
import safeme.uz.presentation.ui.dialog.GetImgDialog
import safeme.uz.presentation.viewmodel.profileInfo.ProfileInfoViewModel
import safeme.uz.presentation.viewmodel.profileInfo.ProfileInfoViewModelImpl
import safeme.uz.utils.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class ProfileInfoScreen : Fragment(R.layout.screen_profile_info), OnClickListener {
    private val binding by viewBinding(ScreenProfileInfoBinding::bind)
    private val viewModel: ProfileInfoViewModel by viewModels<ProfileInfoViewModelImpl>()
    private val address = Address()
    private val userDataRequest = UserDataRequest()

    private var isEnteredFirstName: Boolean = false
    private var isEnteredLastName: Boolean = false
    private var isEnteredBithDate: Boolean = false
    private var isSelectedMan: Boolean = true
    private var isEnteredRegion: Boolean = false
    private var isEnteredDistrict: Boolean = false
    private var isEnteredMFY: Boolean = false
    private var isEnteredAddress: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageFromCameraListener()
        imageFromGalleryListener()
        initViews()
        initObservers()

        userDataRequest.photoUri?.let {
            setImage(it)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            Keys.ADDRESS_REQUEST_KEY, this
        ) { _, it ->
            val selectedAddress = it.getSerializable(Keys.ADDRESS_BUNDLE_KEY) as Address
            when (selectedAddress.type) {
                Keys.REGION -> {
                    userDataRequest.region = selectedAddress.id
                }
                Keys.DISTRICT -> {
                    userDataRequest.district = selectedAddress.id
                }
                Keys.MFY -> {
                    userDataRequest.mahalla = selectedAddress.id
                }
            }
            showToast(selectedAddress.toString())
        }

    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initObservers() = with(viewModel) {
        errorLiveData.observe(this@ProfileInfoScreen, errorObserver)
        messageLiveData.observe(this@ProfileInfoScreen, messageObserver)
        progressLiveData.observe(viewLifecycleOwner, progressObserver)
        openLoginStep1ScreenLiveData.observe(this@ProfileInfoScreen, openLoginStep1ScreenObserver)
    }

    private val openLoginStep1ScreenObserver = Observer<UserDataResponse> {

    }

    private val errorObserver = Observer<Int> {

        snackMessage(getString(it))
    }

    private val messageObserver = Observer<String> {
        snackMessage(it)
    }

    private val progressObserver = Observer<Boolean> {
        binding.progress.isVisible = it
    }

    private fun initViews() = with(binding) {
        etDateOfBirth.setOnClickListener(this@ProfileInfoScreen)
        llMan.setOnClickListener(this@ProfileInfoScreen)
        llWoman.setOnClickListener(this@ProfileInfoScreen)
        etRegion.setOnClickListener(this@ProfileInfoScreen)
        etDistrict.setOnClickListener(this@ProfileInfoScreen)
        etMFY.setOnClickListener(this@ProfileInfoScreen)
        llCloseImage.setOnClickListener(this@ProfileInfoScreen)
        vlImageContainer.setOnClickListener(this@ProfileInfoScreen)
        btnAddImage.setOnClickListener(this@ProfileInfoScreen)
        button.setOnClickListener(this@ProfileInfoScreen)

        etFirstName.addTextChangedListener {
            etFirstNameLayout.isErrorEnabled = false
        }
        etLastName.addTextChangedListener {
            etLastNameLayout.isErrorEnabled = false
        }
        etDateOfBirth.addTextChangedListener {
            etDateOfBirthLayout.isErrorEnabled = false
        }
        etRegion.addTextChangedListener {
            etRegionLayout.isErrorEnabled = false
        }
        etDistrict.addTextChangedListener {
            etDistrictLayout.isErrorEnabled = false
        }
        etMFY.addTextChangedListener {
            etMFYLayout.isErrorEnabled = false
        }
        etAddress.addTextChangedListener {
            etAddressLayout.isErrorEnabled = false
        }
    }

    override fun onClick(v: View?): Unit = with(binding) {
        when (v) {
            etDateOfBirth -> {
                Util.openDatePickerDotly(
                    requireContext(), etDateOfBirth.text.toString(), etDateOfBirth
                )
            }
            llMan -> {
                if (isSelectedMan) {
                    isSelectedMan = false
                    isMan.isChecked = false
                    isWoman.isChecked = true
                } else {
                    isSelectedMan = true
                    isMan.isChecked = true
                    isWoman.isChecked = false
                }
            }
            llWoman -> {
                if (isSelectedMan) {
                    isSelectedMan = false
                    isMan.isChecked = false
                    isWoman.isChecked = true
                } else {
                    isSelectedMan = true
                    isMan.isChecked = true
                    isWoman.isChecked = false
                }
            }
            etRegion -> {
                address.type = Keys.REGION
                val dialog = AddressDialog(address)
                dialog.show(childFragmentManager, "AddressDialog")
            }
            etDistrict -> {
                address.type = Keys.DISTRICT
                val dialog = AddressDialog(address)
                dialog.show(childFragmentManager, "AddressDialog")
            }
            etMFY -> {
                address.type = Keys.MFY
                val dialog = AddressDialog(address)
                dialog.show(childFragmentManager, "AddressDialog")
            }
            llCloseImage -> {
                userDataRequest.photo = null
                vlImageContainer.gone()
            }
            vlImageContainer -> {
                findNavController().navigate(
                    ProfileInfoScreenDirections.actionProfileInfoScreenToExpandedProfileImageScreen(
                        UserDataResponse(
                            first_name = userDataRequest.first_name,
                            last_name = userDataRequest.last_name,
                            photoUri = userDataRequest.photoUri,
                        )
                    )
                )
            }
            btnAddImage -> {
                val getImgDialog = GetImgDialog()
                getImgDialog.show(childFragmentManager, "GetImgDialog")
            }
            button -> {
                check()
                if (isEnteredFirstName && isEnteredLastName && isEnteredBithDate && isSelectedMan && isEnteredRegion && isEnteredDistrict && isEnteredMFY && isEnteredAddress) {
                    userDataRequest.first_name = etFirstName.text.toString()
                    userDataRequest.last_name = etLastName.text.toString()
                    userDataRequest.birth_day = etDateOfBirth.text.toString()
                    userDataRequest.gender =
                        if (isSelectedMan) getString(R.string.man) else getString(R.string.woman)
                    userDataRequest.adress = etAddress.text.toString()

                    viewModel.sendUserData(userDataRequest)
                }
                hideKeyboard()
            }
        }
    }

    private fun check() = with(binding) {
        if (etFirstName.text.toString().trim().isEmpty()) {
            etFirstNameLayout.error = getString(R.string.enter_first_name)
            isEnteredFirstName = false
        } else isEnteredFirstName = true

        if (etLastName.text.toString().trim().isEmpty()) {
            etLastNameLayout.error = getString(R.string.enter_last_name)
            isEnteredLastName = false
        } else isEnteredLastName = true

        if (etDateOfBirth.text.toString().trim().isEmpty()) {
            etDateOfBirthLayout.error = getString(R.string.enter_date_of_birth)
            isEnteredBithDate = false
        } else isEnteredBithDate = true

        if (etRegion.text.toString().trim().isEmpty()) {
            etRegionLayout.error = getString(R.string.enter_region)
            isSelectedMan = false
        } else isSelectedMan = true

        if (etDistrict.text.toString().trim().isEmpty()) {
            etDistrictLayout.error = getString(R.string.enter_district)
            isEnteredDistrict = false
        } else isEnteredDistrict = true

        if (etMFY.text.toString().trim().isEmpty()) {
            etMFYLayout.error = getString(R.string.enter_mfy)
            isEnteredMFY = false
        } else isEnteredMFY = true

        if (etAddress.text.toString().trim().isEmpty()) {
            etAddressLayout.error = getString(R.string.enter_address)
            isEnteredAddress = false
        } else isEnteredAddress = true
    }

    private fun imageFromCameraListener() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            Keys.REQUEST_KEY, this
        ) { _, it ->
            val imagePath = it.getString(Keys.BUNDLE_KEY)
            Log.e("TAG", "imagePath : $imagePath")
            val file = File(imagePath!!)
            if (file.exists()) {

                setImage(file.absolutePath.toUri())

                val requestFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
                val multipartBodyPart =
                    MultipartBody.Part.createFormData("img", file.name, requestFile)
                //todo
                userDataRequest.photo = multipartBodyPart
            } else {
                showToast(getString(R.string.file_not_exist))
            }
        }
    }

    private fun setImage(uri: Uri) {
        userDataRequest.photoUri = uri
        binding.ivProfile.setImageURI(uri)
        binding.vlImageContainer.visible()
    }

    private fun imageFromGalleryListener() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            Keys.REQUEST_KEY_LIST, this
        ) { _, it ->
            val fileUris: ArrayList<Uri> =
                it.getParcelableArrayList<Uri>(Keys.BUNDLE_KEY_LIST) as ArrayList<Uri>

            val prepareFilePart = prepareFilePart(fileUris.first())

            if (prepareFilePart != null) {
                //todo
                userDataRequest.photo = prepareFilePart
            }
        }
    }

    private fun prepareFilePart(fileUri: Uri): MultipartBody.Part? {
        Log.e("TAG", "fileUri : $fileUri")

        val file = getRealPathFromURI(fileUri, requireContext())?.let { File(it) }
        if (file?.exists() == true) {

            setImage(file.absolutePath.toUri())

            val requestFile: RequestBody = file.asRequestBody(
                activity?.applicationContext?.contentResolver?.getType(fileUri)?.toMediaTypeOrNull()
            )
            return MultipartBody.Part.createFormData("img", file.name, requestFile)
        } else {
            showToast(getString(R.string.file_not_exist))
        }
        return null
    }

    private fun getRealPathFromURI(uri: Uri, context: Context): String? {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val file = File(context.filesDir, name)
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 10 * 1024 * 1024
            val bytesAvailable: Int = inputStream?.available() ?: 0
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream?.read(buffers).also {
                    if (it != null) {
                        read = it
                    }
                } != -1) {
                outputStream.write(buffers, 0, read)
            }
            inputStream?.close()
            outputStream.close()

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return file.path
    }

}
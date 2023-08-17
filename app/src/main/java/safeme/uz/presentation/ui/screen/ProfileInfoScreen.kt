package safeme.uz.presentation.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.Toast
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
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.DistrictByIdRequest
import safeme.uz.data.remote.request.NeighborhoodRequest
import safeme.uz.data.remote.request.UserUpdateRequest
import safeme.uz.data.remote.response.DistrictInfo
import safeme.uz.data.remote.response.NeighborhoodInfo
import safeme.uz.data.remote.response.RegionInfo
import safeme.uz.data.remote.response.UserDataResponse
import safeme.uz.data.remote.response.UserUpdateResponse
import safeme.uz.databinding.ScreenProfileInfoBinding
import safeme.uz.presentation.ui.adapter.SpinnerAdapter
import safeme.uz.presentation.ui.dialog.GetImgDialog
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.viewmodel.profileInfo.ProfileEditScreenViewModel
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
    private val profileEditViewModel: ProfileEditScreenViewModel by viewModels()
    private var regionArrayList: ArrayList<RegionInfo>? = null
    private var districtByRegionArrayList: ArrayList<DistrictInfo>? = null
    private var mfyByDistrictArrayList: ArrayList<NeighborhoodInfo>? = null
    private var isEnteredFirstName: Boolean = false
    private var isEnteredLastName: Boolean = false
    private var isEnteredBirthDate: Boolean = false
    private var isSelectedGender: Boolean = true
    private var isEnteredRegion: Boolean = false
    private var isEnteredDistrict: Boolean = false
    private var isEnteredMFY: Boolean = false
    private var isEnterPhoto:Boolean = false
    private var photo:MultipartBody.Part? = null
    private var requestFile:RequestBody? = null
    private var file:File? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkInternet()
        backPressDispatcher()
    }

    private fun checkInternet(){
        if (isConnected()){
            imageFromCameraListener()
            imageFromGalleryListener()
            loadRegionData()
            initViews()
            initObservers()
            spinnerHandler()
        }else {
            binding.progress.gone()
            val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
            messageDialog.show(requireActivity().supportFragmentManager,Keys.DIALOG)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun spinnerHandler() {

            binding.regionSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        loadDistrictData()
                        isEnteredRegion =
                            binding.regionSpinner.selectedItem.toString() != getString(R.string.select_name)
                        checkAllFieldExist()

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                }



        binding.districtSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    loadMYFByDistrict()

                    isEnteredDistrict =
                        binding.districtSpinner.selectedItem.toString() != getString(R.string.select_name)

                    checkAllFieldExist()


                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

        binding.mfySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItemText = binding.mfySpinner.selectedItem
                isEnteredMFY =
                    selectedItemText != null && selectedItemText.toString() != getString(R.string.select_name)
                checkAllFieldExist()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                isEnteredMFY = false
                binding.button.disable()
            }

        }

        binding.districtSpinner.setOnTouchListener { view, motionEvent ->
            if (binding.regionSpinner.selectedItemPosition == 0) {
                customToast(getString(R.string.before_select_region))
            }
            false
        }

        binding.mfySpinner.setOnTouchListener { view, motionEvent ->
            if (binding.districtSpinner.selectedItemPosition < 0) {
                customToast(getString(R.string.before_select_district))
            }
            false
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
        binding.button.snackBar(getString(it))
    }

    private val messageObserver = Observer<String> {
        binding.button.snackBar(it)
    }

    private val progressObserver = Observer<Boolean> {
        binding.progress.isVisible = it
    }



    private fun initViews() = with(binding) {
        etDateOfBirth.setOnClickListener(this@ProfileInfoScreen)
        llMan.setOnClickListener(this@ProfileInfoScreen)
        llWoman.setOnClickListener(this@ProfileInfoScreen)
        llCloseImage.setOnClickListener(this@ProfileInfoScreen)
        vlImageContainer.setOnClickListener(this@ProfileInfoScreen)
        btnAddImage.setOnClickListener(this@ProfileInfoScreen)
        button.setOnClickListener(this@ProfileInfoScreen)

        etFirstName.addTextChangedListener {
            etFirstNameLayout.isErrorEnabled = false
            isEnteredFirstName = etFirstName.text.toString().isNotBlank()
            checkAllFieldExist()
        }
        etLastName.addTextChangedListener {
            etLastNameLayout.isErrorEnabled = false
            isEnteredLastName = etLastName.text.toString().isNotBlank()
            checkAllFieldExist()
        }
        etDateOfBirth.addTextChangedListener {
            etDateOfBirthLayout.isErrorEnabled = false
            isEnteredBirthDate = etDateOfBirth.text.toString().isNotBlank()
            if(isEnteredBirthDate){
                hideKeyboard()
            }
            checkAllFieldExist()
        }

    }

    private fun loadDistrictData() {
        val regionSelectedItem = binding.regionSpinner.selectedItem
        if (regionArrayList != null) {
            for (i in regionArrayList!!) {
                if (i.name == regionSelectedItem) {
                    if (isConnected()) {
                        profileEditViewModel.getDistrictsById(DistrictByIdRequest(i.id))
                        profileEditViewModel.districtByRegionLiveData.observe(
                            viewLifecycleOwner,
                            districtObserver
                        )
                        break
                    } else {
                        val messageDialog =
                            MessageDialog(getString(R.string.internet_not_connected))
                        messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                    }

                }
            }
        }
    }

    private fun loadRegionData() {
        if (isConnected()) {
            profileEditViewModel.getRegions()
            profileEditViewModel.regionsDataLiveData.observe(viewLifecycleOwner, regionObserver)
        } else {
            val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
            messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
        }

    }

    private fun checkAllFieldExist() {
        if (isEnteredFirstName&&isEnteredLastName&&isSelectedGender&&isEnteredBirthDate&&
                isEnteredDistrict&&isEnteredRegion&&isEnteredMFY&&isEnterPhoto){
            binding.button.enable()
        }else {
            binding.button.disable()
        }
    }


    private val regionObserver = Observer<RemoteApiResult<ApiResponse<ArrayList<RegionInfo>>>> {
        when (it) {
            is RemoteApiResult.Success -> {
                regionAttach(it.data?.body)
            }

            is RemoteApiResult.Error -> {
                regionAttach(null)

            }


            else -> {}
        }
    }

    private fun regionAttach(regionList: ArrayList<RegionInfo>?) {
        regionArrayList = ArrayList()
        regionArrayList = regionList
        var regionSpinnerAdapter: SpinnerAdapter? = null
        if (regionList != null) {
            regionSpinnerAdapter =
                SpinnerAdapter(requireContext(), refToStringList((regionList)))
        } else {
            isEnteredRegion = false
            binding.button.disable()
            val emptyList = ArrayList<String>()
            regionSpinnerAdapter = SpinnerAdapter(requireContext(), emptyList)
        }
        regionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.regionSpinner.adapter = regionSpinnerAdapter
        loadDistrictData()
    }

    private fun refToStringList(regionList: ArrayList<RegionInfo>): ArrayList<String> {
        val list = ArrayList<String>()
        list.add(resources.getString(R.string.select_name))
        for (i in regionList) {
            list.add(i.name!!)
        }
        return list
    }


    private val districtObserver =
        Observer<RemoteApiResult<ApiResponse<ArrayList<DistrictInfo>>>> {
            when (it) {
                is RemoteApiResult.Success -> {
                    districtAttach(it.data?.body)
                }

                is RemoteApiResult.Error -> {
                    districtAttach(null)
                }

                else -> {}
            }

        }

    private fun districtAttach(districtList: ArrayList<DistrictInfo>?) {
        var districtSpinnerAdapter: SpinnerAdapter? = null
        if (districtList != null) {
            val districtNameList = ArrayList<String>()
            districtByRegionArrayList = ArrayList()
            districtByRegionArrayList = districtList
            districtNameList.add(resources.getString(R.string.select_name))
            for (i in districtList) {
                districtNameList.add(i.name!!)
            }
            districtSpinnerAdapter =
                SpinnerAdapter(requireContext(), districtNameList)
        } else {
            isEnteredDistrict = false
            binding.button.disable()
            val emptyDistrictList = ArrayList<String>()
            districtSpinnerAdapter = SpinnerAdapter(requireContext(), emptyDistrictList)
        }
        districtSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.districtSpinner.adapter = districtSpinnerAdapter
        loadMYFByDistrict()


    }


    override fun onClick(v: View?): Unit = with(binding) {
        when (v) {
            etDateOfBirth -> {
                Util.openDatePickerDotly(
                    requireContext(), etDateOfBirth.text.toString(), etDateOfBirth
                )
            }
            llMan -> {
                if (!binding.isMan.isChecked) {
                    isSelectedGender = true
                    isMan.isChecked = true
                    isWoman.isChecked = false
                }
                else {
                  isSelectedGender = false
                    isMan.isChecked = false
                    isWoman.isChecked = false
                }
                checkAllFieldExist()
            }
            llWoman -> {
                if (binding.isWoman.isChecked) {
                    isSelectedGender = false
                    isMan.isChecked = false
                    isWoman.isChecked = false
                } else {
                    isSelectedGender = true
                    isMan.isChecked = false
                    isWoman.isChecked = true
                }
                checkAllFieldExist()
            }

            llCloseImage -> {
                file = null
                photo = null
                vlImageContainer.gone()
                isEnterPhoto = false
                checkAllFieldExist()
            }
            vlImageContainer -> {
//                findNavController().navigate(
//                    ProfileInfoScreenDirections.actionProfileInfoScreenToExpandedProfileImageScreen(
//                        UserDataResponse(
//                            first_name = userUpdateRequest.first_name,
//                            last_name = userUpdateRequest.last_name,
//
//                        )
//                    )
//                )
            }

            btnAddImage -> {
                val getImgDialog = GetImgDialog()
                getImgDialog.show(childFragmentManager, "GetImgDialog")
            }
            button -> {

                val gender =
                    if (isSelectedGender) getString(R.string.man_lower_case).lowercase() else getString(R.string.woman_lower_case).lowercase()


                if(isConnected()){
                    val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                    builder.addFormDataPart("first_name",etFirstName.text.toString())
                    builder.addFormDataPart("last_name",etLastName.text.toString())
                    builder.addFormDataPart("birth_day",etDateOfBirth.text.toString().orderBirthDay())
                    builder.addFormDataPart("gender",gender)
                    builder.addFormDataPart("region",getRegionId().toString())
                    builder.addFormDataPart("district",getDistrictId().toString())
                    builder.addFormDataPart("mahalla",getMFYId().toString())
                    builder.addFormDataPart("adress","")
                    Log.d("LLL",file!!.name)
                    builder.addFormDataPart("photo",file!!.name,requestFile!!)
                    profileEditViewModel.userUpdate(builder.build())
                    profileEditViewModel.userUpdateLiveData.observe(viewLifecycleOwner, userUpdateObserver)

                }else {
                    binding.progress.gone()
                    val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
                    messageDialog.show(requireActivity().supportFragmentManager,Keys.DIALOG)
                }


                }
            }
        }

    private val userUpdateObserver = Observer<RemoteApiResult<ApiResponse<UserUpdateResponse>>> {
        when (it) {
            is RemoteApiResult.Success -> {
                binding.progress.gone()
                val messageDialog = MessageDialog(it.data?.message!!)
                messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                messageDialog.btnClickEvent = {
                    val bundle = Bundle().apply {
                        putString(Keys.PIN_BUNDLE_KEY,Keys.PIN_CREATE_AFTER_LOGIN)
                    }
                  findNavController().navigate(R.id.pinCodeScreen,bundle)
                }
            }

            is RemoteApiResult.Loading -> {
                binding.progress.visible()
            }

            is RemoteApiResult.Error -> {
                binding.progress.gone()
                val messageDialog = MessageDialog(it.message!!)
                messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
            }
        }
    }

    private fun loadMYFByDistrict() {
        val selectedDistrictByRegion = binding.districtSpinner.selectedItem
        selectedDistrictByRegion?.let {
            for (i in districtByRegionArrayList!!) {
                if (i.name == selectedDistrictByRegion) {
                    if (isConnected()) {
                        profileEditViewModel.getMFYByDistrict(NeighborhoodRequest(i.id.toString()))
                        profileEditViewModel.getMFYByDistrictLiveData.observe(viewLifecycleOwner, mfyObserver)
                    } else {
                        val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
                        messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                    }

                }
            }
        }

    }

    private val mfyObserver =
        Observer<RemoteApiResult<ApiResponse<ArrayList<NeighborhoodInfo>>>> {
            when (it) {
                is RemoteApiResult.Success -> mfyDataAttach(it.data?.body)

                is RemoteApiResult.Error -> {
                    mfyDataAttach(null)
                }


                else -> {}
            }
        }

    private fun mfyDataAttach(mfyList: ArrayList<NeighborhoodInfo>?) {
        mfyByDistrictArrayList = ArrayList()
        mfyByDistrictArrayList = mfyList
        var mfySpinnerAdapter: SpinnerAdapter? = null
        mfySpinnerAdapter = if (mfyList != null) {
            val mfyStringList = ArrayList<String>()
            mfyStringList.add(getString(R.string.select_name))
            for (i in mfyList) {
                mfyStringList.add(i.name!!)
            }
            SpinnerAdapter(requireContext(), mfyStringList)

        } else {
            isEnteredMFY = false
            binding.button.disable()
            val emptyMFYList = ArrayList<String>()
            SpinnerAdapter(requireContext(), emptyMFYList)
        }
        mfySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mfySpinner.adapter = mfySpinnerAdapter

    }

    private fun getRegionId(): Int {
        var id = -1
        val selectedRegionSpinnerItem = binding.regionSpinner.selectedItem
        if (selectedRegionSpinnerItem != null && regionArrayList != null) {
            for (i in regionArrayList!!) {
                if (selectedRegionSpinnerItem.toString() == i.name) {
                    id = i.id
                    break
                }
            }
        }
        return id
    }


    private fun getDistrictId(): Int {
        var id = -1
        val selectedDistrictSpinner = binding.districtSpinner.selectedItem
        if (selectedDistrictSpinner != null && districtByRegionArrayList != null) {
            for (i in districtByRegionArrayList!!) {
                if (selectedDistrictSpinner.toString() == i.name) {
                    id = i.id
                    break
                }
            }
        }
        return id
    }


    private fun getMFYId(): Int {
        var id = -1
        val selectedMfySpinner = binding.mfySpinner.selectedItem
        if (selectedMfySpinner != null && mfyByDistrictArrayList != null) {
            for (i in mfyByDistrictArrayList!!) {
                if (selectedMfySpinner.toString() == i.name) {
                    id = i.id
                    break
                }
            }
        }
        return id
    }


    private fun setImage(uri: Uri) {
        binding.ivProfile.setImageURI(uri)
        binding.vlImageContainer.visible()
        if (file!=null){
            isEnterPhoto = true
            checkAllFieldExist()
        }else {
            isEnterPhoto = false
            checkAllFieldExist()
        }
    }

    private fun imageFromCameraListener() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            Keys.REQUEST_KEY, this
        ) { _, it ->
            val imagePath = it.getString(Keys.BUNDLE_KEY)
            Log.e("TAG", "imagePath : $imagePath")
            file = File(imagePath!!)
            if (file!!.exists()) {
                setImage(file!!.absolutePath.toUri())
                requestFile = file!!.asRequestBody("image/jpg".toMediaTypeOrNull())
                val multipartBodyPart =
                    MultipartBody.Part.createFormData("img", file!!.name, requestFile!!)

                photo = multipartBodyPart
            } else {
                showToast(getString(R.string.file_not_exist))
            }
        }
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
//             requestFile = prepareFilePart
            }
        }
    }

    private fun prepareFilePart(fileUri: Uri): MultipartBody.Part? {
        Log.e("TAG", "fileUri : $fileUri")

        file = getRealPathFromURI(fileUri, requireContext())?.let { File(it) }
        if (file?.exists() == true) {

            setImage(file!!.absolutePath.toUri())

            requestFile = file!!.asRequestBody(
                activity?.applicationContext?.contentResolver?.getType(fileUri)?.toMediaTypeOrNull()
            )
//            return MultipartBody.Part.createFormData("img", file!!.name, requestFile)
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


    private fun customToast(str: String) {
        Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
    }

}
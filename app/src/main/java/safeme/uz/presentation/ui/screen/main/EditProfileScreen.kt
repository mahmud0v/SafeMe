package safeme.uz.presentation.ui.screen.main

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import safeme.uz.data.remote.response.DistrictInfo
import safeme.uz.data.remote.response.NeighborhoodInfo
import safeme.uz.data.remote.response.RegionInfo
import safeme.uz.data.remote.response.UserUpdateResponse
import safeme.uz.databinding.ScreenEditProfileBinding
import safeme.uz.presentation.ui.adapter.SpinnerAdapter
import safeme.uz.presentation.ui.dialog.GetImgDialog
import safeme.uz.presentation.ui.dialog.MessageDialog
import safeme.uz.presentation.viewmodel.profileInfo.ProfileEditScreenViewModel
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import safeme.uz.utils.Util
import safeme.uz.utils.disable
import safeme.uz.utils.enable
import safeme.uz.utils.gone
import safeme.uz.utils.hideKeyboard
import safeme.uz.utils.isConnected
import safeme.uz.utils.orderBirthDay
import safeme.uz.utils.reFormatBirthDay
import safeme.uz.utils.showToast
import safeme.uz.utils.visible
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class EditProfileScreen : Fragment(R.layout.screen_edit_profile) {
    private val binding: ScreenEditProfileBinding by viewBinding()
    private val viewModel: ProfileEditScreenViewModel by viewModels()
    private val navArgs: EditProfileScreenArgs by navArgs()
    private var regionArrayList: ArrayList<RegionInfo>? = null
    private var districtByRegionArrayList: ArrayList<DistrictInfo>? = null
    private var mfyByDistrictArrayList: ArrayList<NeighborhoodInfo>? = null
    private var resultDateOfBirth = false
    private var resultEtWho = false
    private var resultEtFirstName = false
    private var resultEtLastname = false
    private var resultEtRegion = false
    private var resultEtDistrict = false
    private var resultEtMfy = false
    private var resultGender = false
    private var resultEnterPhoto: Boolean = false
    private var requestFile: RequestBody? = null
    private var file: File? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        attachUserInfo()
        backEvent()
        checkEnableInputViews()
        loadParentData()
        loadRegionData()
        imageFromGalleryListener()
        imageFromCameraListener()
        clickImageButtonEvent()
        btnSaveClickEvent()

    }

    private fun attachUserInfo() {
        val userInfo = navArgs.userInfo
        userInfo.firstName?.let {
            binding.etFirstName.setText(it)
            resultEtFirstName = true
        }
        userInfo.lastName?.let {
            binding.etLastName.setText(it)
            resultEtLastname = true
        }

        userInfo.birthDay?.let {
            binding.etDateOfBirth.setTextKeepState(it.reFormatBirthDay())
            resultDateOfBirth = true
        }


    }


    @SuppressLint("ClickableViewAccessibility")
    private fun checkEnableInputViews() {

        binding.etFirstName.addTextChangedListener {
            resultEtFirstName = !binding.etFirstName.text.isNullOrBlank()
            checkAllFields()
        }

        binding.etLastName.addTextChangedListener {
            resultEtLastname = !binding.etLastName.text.isNullOrBlank()
            checkAllFields()
        }

        binding.calendarId.setOnClickListener {
            Util.openDatePickerDotly(
                requireContext(),
                binding.etDateOfBirth.text.toString(),
                binding.etDateOfBirth
            )
        }

        binding.etDateOfBirth.addTextChangedListener {
            resultDateOfBirth = binding.etDateOfBirth.text.toString().length == 10
            if (resultDateOfBirth) {
                hideKeyboard()
            }
            checkAllFields()
        }


//        binding.parentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                resultEtWho =
//                    binding.parentSpinner.selectedItem.toString() != getString(R.string.select_name)
//                if (resultDateOfBirth && resultEtWho && resultEtFirstName && resultEtLastname && resultEtRegion && resultEtDistrict
//                    && resultEtMfy && resultGender
//                ) {
//                    binding.btnSave.enable()
//                } else {
//                    binding.btnSave.disable()
//                }
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//            }
//
//        }


        binding.regionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                loadDistrictData()
                mfyDataAttach(null)
                resultEtRegion =
                    binding.regionSpinner.selectedItem.toString() != getString(R.string.select_name)
                resultEtMfy = false
                resultEtDistrict = false
                checkAllFields()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }



        binding.districtSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    loadMYFByDistrict()

                    resultEtDistrict =
                        binding.districtSpinner.selectedItem.toString() != getString(R.string.select_name)
                    resultEtMfy = false
                    checkAllFields()


                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

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

        binding.mfySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItemText = binding.mfySpinner.selectedItem
                resultEtMfy =
                    selectedItemText != null && selectedItemText.toString() != getString(R.string.select_name)
                checkAllFields()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                resultEtMfy = false
                binding.btnSave.disable()
            }

        }

        binding.isMan.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.isWoman.isChecked = false
            }
            resultGender = genderSelectionCheckBox() != getString(R.string.nothing)

            checkAllFields()
        }

        binding.isWoman.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.isMan.isChecked = false
            }
            resultGender = genderSelectionCheckBox() != getString(R.string.nothing)

            checkAllFields()


        }


    }


    private fun loadParentData() {
        val parentList = stringArrayToList(resources.getStringArray(R.array.parent_array))
        val adapter = SpinnerAdapter(requireContext(), parentList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.parentSpinner.adapter = adapter
    }


    private fun loadRegionData() {
        if (isConnected()) {
            viewModel.getRegions()
            viewModel.regionsDataLiveData.observe(viewLifecycleOwner, regionObserver)
        } else {
            val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
            messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
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


    private fun genderSelectionCheckBox(): String {
        return if (binding.isMan.isChecked && !binding.isWoman.isChecked) {
            getString(R.string.man_lower_case)
        } else if (!binding.isMan.isChecked && binding.isWoman.isChecked) {
            getString(R.string.woman_lower_case)
        } else {
            getString(R.string.nothing)
        }

    }

    private fun clickImageButtonEvent() {
        binding.llCloseImage.setOnClickListener {
            file = null
            binding.vlImageContainer.gone()
            resultEnterPhoto = false
            checkAllFields()
        }

        binding.btnAddImage.setOnClickListener {
            val getImgDialog = GetImgDialog()
            getImgDialog.show(childFragmentManager, "GetImgDialog")
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
            resultEtDistrict = false
            binding.btnSave.disable()
            val emptyDistrictList = ArrayList<String>()
            districtSpinnerAdapter = SpinnerAdapter(requireContext(), emptyDistrictList)
        }
        districtSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.districtSpinner.adapter = districtSpinnerAdapter
        loadMYFByDistrict()


    }

    private fun regionAttach(regionList: ArrayList<RegionInfo>?) {
        regionArrayList = ArrayList()
        regionArrayList = regionList
        var regionSpinnerAdapter: SpinnerAdapter? = null
        if (regionList != null) {
            regionSpinnerAdapter =
                SpinnerAdapter(requireContext(), refToStringList((regionList)))
        } else {
            resultEtRegion = false
            binding.btnSave.disable()
            val emptyList = ArrayList<String>()
            regionSpinnerAdapter = SpinnerAdapter(requireContext(), emptyList)
        }
        regionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.regionSpinner.adapter = regionSpinnerAdapter
        loadDistrictData()
    }

    private fun loadDistrictData() {
        val regionSelectedItem = binding.regionSpinner.selectedItem
        regionSelectedItem?.let {
           if (regionArrayList != null) {
                for (i in regionArrayList!!) {
                    if (i.name == regionSelectedItem) {
                        if (isConnected()) {
                            viewModel.getDistrictsById(DistrictByIdRequest(i.id))
                            viewModel.districtByRegionLiveData.observe(
                                viewLifecycleOwner,
                                districtObserver
                            )
                            break
                        } else {
                            binding.progress.gone()
                            val messageDialog =
                                MessageDialog(getString(R.string.internet_not_connected))
                            messageDialog.show(
                                requireActivity().supportFragmentManager,
                                Keys.DIALOG
                            )
                        }

                    }
                }
            }
        }


    }

    private fun loadMYFByDistrict() {
        val selectedDistrictByRegion = binding.districtSpinner.selectedItem
        selectedDistrictByRegion?.let {
            for (i in districtByRegionArrayList!!) {
                if (i.name == selectedDistrictByRegion) {
                    if (isConnected()) {
                        viewModel.getMFYByDistrict(NeighborhoodRequest(i.id.toString()))
                        viewModel.getMFYByDistrictLiveData.observe(viewLifecycleOwner, mfyObserver)
                    } else {
                        val messageDialog =
                            MessageDialog(getString(R.string.internet_not_connected))
                        messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                    }
                    break
                }
            }
        }

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

    private fun checkAllFields() {
        if (resultDateOfBirth && resultEtFirstName && resultEtLastname && resultEtRegion && resultEtDistrict
            && resultEtMfy && resultGender && resultEnterPhoto
        ) {
            binding.btnSave.enable()
        } else {
            binding.btnSave.disable()
        }
    }

    private val userUpdateObserver = Observer<RemoteApiResult<ApiResponse<UserUpdateResponse>>> {
        when (it) {
            is RemoteApiResult.Success -> {
                binding.progress.gone()
                val messageDialog = MessageDialog(it.data?.message!!)
                messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                messageDialog.btnClickEvent = {
                    findNavController().popBackStack()
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
            resultEtMfy = false
            binding.btnSave.disable()
            val emptyMFYList = ArrayList<String>()
            SpinnerAdapter(requireContext(), emptyMFYList)
        }
        mfySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mfySpinner.adapter = mfySpinnerAdapter

    }

    private fun btnSaveClickEvent() {
        binding.btnSave.setOnClickListener {
            val firstName = binding.etFirstName.text
            val lastName = binding.etLastName.text
            val birthDay = binding.etDateOfBirth.text
            val getGender = genderSelectionCheckBox()
            val getRegionId = getRegionId()
            val getDistrictId = getDistrictId()
            val getMFYId = getMFYId()
            if (binding.etFirstName.text != null && birthDay != null && lastName != null && getGender != getString(
                    R.string.select_name
                )
                && getRegionId != -1 && getDistrictId != -1 && getMFYId != -1
            ) {
                if (isConnected() && file != null && requestFile != null) {
                    val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                    builder.addFormDataPart("first_name", firstName.toString())
                    builder.addFormDataPart("last_name", lastName.toString())
                    builder.addFormDataPart("birth_day", birthDay.toString().orderBirthDay())
                    builder.addFormDataPart("gender", getGender)
                    builder.addFormDataPart("region", getRegionId.toString())
                    builder.addFormDataPart("district", getDistrictId.toString())
                    builder.addFormDataPart("mahalla", getMFYId.toString())
                    builder.addFormDataPart("adress", "sdss")
                    Log.d("LLL", file!!.name)
                    builder.addFormDataPart("photo", file!!.name, requestFile!!)
                    viewModel.userUpdate(builder.build())
                    viewModel.userUpdateLiveData.observe(viewLifecycleOwner, userUpdateObserver)
                } else {
                    val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
                    messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                }
            } else {
                val messageDialog = MessageDialog(getString(R.string.some_error_occurred))
                messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
            }

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

            } else {
                showToast(getString(R.string.file_not_exist))
            }
        }
    }

    private fun setImage(uri: Uri) {
        binding.ivProfile.setImageURI(uri)
        binding.vlImageContainer.visible()
        if (file != null) {
            resultEnterPhoto = true
            checkAllFields()
        } else {
            resultEnterPhoto = false
            checkAllFields()
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


    private fun refToStringList(regionList: ArrayList<RegionInfo>): ArrayList<String> {
        val list = ArrayList<String>()
        list.add(resources.getString(R.string.select_name))
        for (i in regionList) {
            list.add(i.name!!)
        }
        return list
    }

    private fun stringArrayToList(arrayString: Array<String>): ArrayList<String> {
        val arrayList = ArrayList<String>()
        for (i in arrayString) {
            arrayList.add(i)
        }
        return arrayList
    }

    private fun backEvent() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun customToast(str: String) {
        Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
    }


}
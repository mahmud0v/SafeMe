package safeme.uz.presentation.ui.screen.main

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.DistrictByIdRequest
import safeme.uz.data.remote.request.NeighborhoodRequest
import safeme.uz.data.remote.request.UserUpdateRequest
import safeme.uz.data.remote.response.DistrictInfo
import safeme.uz.data.remote.response.NeighborhoodInfo
import safeme.uz.data.remote.response.RegionInfo
import safeme.uz.data.remote.response.UserUpdateResponse
import safeme.uz.databinding.ScreenEditProfileBinding
import safeme.uz.presentation.ui.adapter.SpinnerAdapter
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
import safeme.uz.utils.visible

@AndroidEntryPoint
class EditProfileScreen : Fragment(R.layout.screen_edit_profile) {
    private val binding: ScreenEditProfileBinding by viewBinding()
    private val viewModel: ProfileEditScreenViewModel by viewModels()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        backEvent()
        checkEnableInputViews()
        loadParentData()
        loadRegionData()
        btnSaveClickEvent()
    }


    private fun checkEnableInputViews() {

        binding.etFirstName.addTextChangedListener {
            resultEtFirstName = !binding.etFirstName.text.isNullOrBlank()
            if (resultDateOfBirth && resultEtWho && resultEtFirstName && resultEtLastname && resultEtRegion && resultEtDistrict
                && resultEtMfy && resultGender
            ) {
                binding.btnSave.enable()
            } else {
                binding.btnSave.disable()
            }
        }

        binding.etLastName.addTextChangedListener {
            resultEtLastname = !binding.etLastName.text.isNullOrBlank()
            if (resultDateOfBirth && resultEtWho && resultEtFirstName && resultEtLastname && resultEtRegion && resultEtDistrict
                && resultEtMfy && resultGender
            ) {
                binding.btnSave.enable()
            } else {
                binding.btnSave.disable()
            }
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
            if (resultDateOfBirth && resultEtWho && resultEtFirstName && resultEtLastname && resultEtRegion && resultEtDistrict
                && resultEtMfy && resultGender
            ) {
                binding.btnSave.enable()
            } else {
                binding.btnSave.disable()
            }
        }




        binding.parentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                resultEtWho =
                    binding.parentSpinner.selectedItem.toString() != getString(R.string.select_name)
                if (resultDateOfBirth && resultEtWho && resultEtFirstName && resultEtLastname && resultEtRegion && resultEtDistrict
                    && resultEtMfy && resultGender
                ) {
                    binding.btnSave.enable()
                } else {
                    binding.btnSave.disable()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }


        binding.regionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                loadDistrictData()
                resultEtRegion =
                    binding.regionSpinner.selectedItem.toString() != getString(R.string.select_name)
                if (resultDateOfBirth && resultEtWho && resultEtFirstName && resultEtLastname && resultEtRegion && resultEtDistrict
                    && resultEtMfy && resultGender
                ) {
                    binding.btnSave.enable()
                } else {
                    binding.btnSave.disable()
                }

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

                    if (resultDateOfBirth && resultEtWho && resultEtFirstName && resultEtLastname && resultEtRegion && resultEtDistrict
                        && resultEtMfy && resultGender
                    ) {
                        binding.btnSave.enable()
                    } else {
                        binding.btnSave.disable()
                    }


                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

        binding.mfySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItemText = binding.mfySpinner.selectedItem
                resultEtMfy =
                    selectedItemText != null && selectedItemText.toString() != getString(R.string.select_name)
                if (resultDateOfBirth && resultEtWho && resultEtFirstName && resultEtLastname && resultEtRegion && resultEtDistrict
                    && resultEtMfy && resultGender
                ) {
                    binding.btnSave.enable()
                } else {
                    binding.btnSave.disable()
                }
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

            if (resultDateOfBirth && resultEtWho && resultEtFirstName && resultEtLastname && resultEtRegion && resultEtDistrict
                && resultEtMfy && resultGender
            ) {
                binding.btnSave.enable()
            } else {
                binding.btnSave.disable()
            }
        }

        binding.isWoman.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.isMan.isChecked = false
            }
            resultGender = genderSelectionCheckBox() != getString(R.string.nothing)

            if (resultDateOfBirth && resultEtWho && resultEtFirstName && resultEtLastname && resultEtRegion && resultEtDistrict
                && resultEtMfy && resultGender
            ) {
                binding.btnSave.enable()
            } else {
                binding.btnSave.disable()
            }


        }


    }


    private fun loadParentData() {
        val parentList = stringArrayToList(resources.getStringArray(R.array.parent_array))
        val adapter = SpinnerAdapter(requireContext(), parentList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.parentSpinner.adapter = adapter
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
        val regionSelectedItem = binding.regionSpinner.selectedItem.toString()
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
                        val messageDialog =
                            MessageDialog(getString(R.string.internet_not_connected))
                        messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
                    }

                }
            }
        }
    }

    private fun loadMYFByDistrict() {
        val selectedDistrictByRegion = binding.districtSpinner.selectedItem.toString()
        for (i in districtByRegionArrayList!!) {
            if (i.name == selectedDistrictByRegion) {
                if (isConnected()) {
                    viewModel.getMFYByDistrict(NeighborhoodRequest(i.id.toString()))
                    viewModel.getMFYByDistrictLiveData.observe(viewLifecycleOwner, mfyObserver)
                } else {
                    val messageDialog = MessageDialog(getString(R.string.internet_not_connected))
                    messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
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

    private val userUpdateObserver = Observer<RemoteApiResult<ApiResponse<UserUpdateResponse>>> {
        when (it) {
            is RemoteApiResult.Success -> {
                binding.progress.gone()
                val messageDialog = MessageDialog(it.data?.message!!)
                messageDialog.show(requireActivity().supportFragmentManager, Keys.DIALOG)
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
                val userUpdateRequest = UserUpdateRequest(
                    firstName.toString(),
                    lastName.toString(),
                    birthDay.toString().orderBirthDay(),
                    getGender,
                    getRegionId,
                    getDistrictId,
                    getMFYId
                )
                if (isConnected()) {
                    viewModel.userUpdate(userUpdateRequest)
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


}
package safeme.uz.presentation.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.remote.response.Address
import safeme.uz.data.remote.response.AddressResponse
import safeme.uz.databinding.DialogAddressBinding
import safeme.uz.presentation.ui.adapter.AddressAdapter
import safeme.uz.presentation.viewmodel.addressdialog.AddressDialogViewModel
import safeme.uz.presentation.viewmodel.addressdialog.AddressDialogViewModelImpl
import safeme.uz.utils.Keys
import safeme.uz.utils.hideKeyboard
import safeme.uz.utils.snackMessage

@AndroidEntryPoint
class AddressDialog(val address: Address) : DialogFragment(R.layout.dialog_address) {
    private val binding by viewBinding(DialogAddressBinding::bind)
    private val viewModel: AddressDialogViewModel by viewModels<AddressDialogViewModelImpl>()
    private val addressAdapter: AddressAdapter by lazy { AddressAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_Dialog_Custom)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initViews()
    }

    private fun initObserver() = with(viewModel) {
        progressBarLiveData.observe(viewLifecycleOwner, progressBarObserver)
        errorLiveData.observe(this@AddressDialog, errorObserver)
        messageLiveData.observe(this@AddressDialog) { hideKeyboard(); snackMessage(it) }
        getRegionsLiveData.observe(this@AddressDialog, getRegionsObserver)
        getDistrictsByIdLiveData.observe(this@AddressDialog, getDistrictsByIdObserver)
        getMFYsByIdLiveData.observe(this@AddressDialog, getMFYLsByIdObserver)
    }

    private val progressBarObserver = Observer<Boolean> {
        binding.progress.isVisible = it
    }

    private val errorObserver = Observer<Int> {
        hideKeyboard()
        snackMessage(getString(it))
    }

    private val getRegionsObserver = Observer<AddressResponse> {
        addressAdapter.submitList(it.results)
    }

    private val getDistrictsByIdObserver = Observer<List<Address>> {
        addressAdapter.submitList(it)
    }


    private val getMFYLsByIdObserver = Observer<List<Address>> {
        addressAdapter.submitList(it)
    }

    private fun initViews() = with(binding) {
        addressAdapter.setOnItemClickListener {
            requireActivity().supportFragmentManager.setFragmentResult(
                Keys.ADDRESS_REQUEST_KEY, bundleOf(Keys.ADDRESS_BUNDLE_KEY to it)
            )
            dismiss()
        }

        when (address.type) {
            Keys.REGION -> {
                tvDialogTitle.text = getString(R.string.region_of_residence)
                viewModel.getRegions()
            }
            Keys.DISTRICT -> {
                tvDialogTitle.text = getString(R.string.district_of_residence)


//                viewModel.getCategoryList()
            }
            Keys.MFY -> {
                tvDialogTitle.text = getString(R.string.mfy_of_residence)


//                viewModel.getCategoryList()
            }
        }

        binding.recyclerView.adapter = addressAdapter

    }

}
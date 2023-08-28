package safeme.uz.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import safeme.uz.R
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.databinding.ProminentDisclosureDialogBinding

class ProminentDisclosureDialog : DialogFragment() {
    private val binding: ProminentDisclosureDialogBinding by viewBinding()
    private val appSharedPreference by lazy { AppSharedPreference(requireContext()) }
    var allowBtnClick: (() -> Unit)? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.prominent_disclosure_dialog, container, false)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_back)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.denyBtn.setOnClickListener {
            appSharedPreference.prominentDisclosureResult = false
            dismiss()
        }

        binding.allowBtn.setOnClickListener {
            allowBtnClick?.invoke()
            appSharedPreference.prominentDisclosureResult = true
            dismiss()
        }
    }
}
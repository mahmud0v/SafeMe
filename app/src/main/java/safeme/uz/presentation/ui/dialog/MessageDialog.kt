package safeme.uz.presentation.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import safeme.uz.R
import safeme.uz.databinding.MessageDialogBinding

class MessageDialog(private val errorMessage:String?,private val btnText:String? = null) : DialogFragment() {
    private val binding: MessageDialogBinding by viewBinding()
    var btnClickEvent : (()->Unit)? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.message_dialog, container, false)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_back)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.dialogBtn.setOnClickListener {
            btnClickEvent?.invoke()
            dismiss()
        }

        if (errorMessage != null){
            binding.dialogText.text = errorMessage
        }else {
            binding.dialogText.text = getString(R.string.some_error_occurred)
        }

        btnText?.let {
            binding.dialogBtn.text = it
            Log.d("BBB", "$it")
        }

    }






}
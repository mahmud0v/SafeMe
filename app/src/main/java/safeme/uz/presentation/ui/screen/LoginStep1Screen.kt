package safeme.uz.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.remote.request.AddingChildDataRequest
import safeme.uz.data.remote.response.AddingChildDataResponse
import safeme.uz.databinding.ScreenLoginStep1Binding
import safeme.uz.presentation.viewmodel.loginstep1.LoginStep1ViewModel
import safeme.uz.presentation.viewmodel.loginstep1.LoginStep1ViewModelImpl
import safeme.uz.utils.Util
import safeme.uz.utils.snackMessage

@AndroidEntryPoint
class LoginStep1Screen : Fragment(R.layout.screen_login_step1), OnClickListener {
    private val binding by viewBinding(ScreenLoginStep1Binding::bind)
    private val viewModel: LoginStep1ViewModel by viewModels<LoginStep1ViewModelImpl>()

    private val addingChildDataRequest = AddingChildDataRequest()
    private var isSelectedMan: Boolean = true
    private var isEnteredName: Boolean = false
    private var isEnteredBithDate: Boolean = false
    private var isEnteredWhoAreYouThisBaby: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initObservers() = with(viewModel) {
        errorLiveData.observe(this@LoginStep1Screen, errorObserver)
        messageLiveData.observe(this@LoginStep1Screen, messageObserver)
        progressLiveData.observe(viewLifecycleOwner, progressObserver)
        openLoginStep2ScreenLiveData.observe(this@LoginStep1Screen, openLoginStep2ScreenObserver)
    }

    private val openLoginStep2ScreenObserver = Observer<AddingChildDataResponse> {

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
        etDateOfChild.setOnClickListener(this@LoginStep1Screen)
        llMan.setOnClickListener(this@LoginStep1Screen)
        llWoman.setOnClickListener(this@LoginStep1Screen)
        btnOneId.setOnClickListener(this@LoginStep1Screen)
        button.setOnClickListener(this@LoginStep1Screen)

        etDateOfChild.addTextChangedListener {
            etDateOfChildLayout.isErrorEnabled = false
        }
        etNameOfChild.addTextChangedListener {
            etNameOfChildLayout.isErrorEnabled = false
        }
        etWhoAreYouThisBaby.addTextChangedListener {
            etWhoAreYouThisBabyLayout.isErrorEnabled = false
        }
    }

    override fun onClick(v: View?): Unit = with(binding) {
        when (v) {
            etDateOfChild -> {
                Util.openDatePickerDotly(
                    requireContext(), etDateOfChild.text.toString(), etDateOfChild
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
            btnOneId -> {

            }
            button -> {
                check()

            }
        }
    }

    private fun check() = with(binding) {
        if (etDateOfChild.text.toString().trim().isEmpty()) {
            etDateOfChildLayout.error = getString(R.string.enter_date_of_birth)
            isEnteredBithDate = false
        } else isEnteredBithDate = true

        if (etNameOfChild.text.toString().trim().isEmpty()) {
            etNameOfChildLayout.error = getString(R.string.enter_first_name)
            isEnteredName = false
        } else isEnteredName = true

        if (etWhoAreYouThisBaby.text.toString().trim().isEmpty()) {
            etWhoAreYouThisBabyLayout.error = getString(R.string.enter_who_for_this_child)
            isEnteredWhoAreYouThisBaby = false
        } else isEnteredWhoAreYouThisBaby = true
    }


}
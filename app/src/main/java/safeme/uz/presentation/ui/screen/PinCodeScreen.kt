package safeme.uz.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import safeme.uz.R
import safeme.uz.data.model.VerifyModel
import safeme.uz.data.remote.response.ResetPinCodeResponse
import safeme.uz.databinding.ScreenPinCodeBinding
import safeme.uz.presentation.viewmodel.pincode.PinCodeVIewModelImpl
import safeme.uz.presentation.viewmodel.pincode.PinCodeViewModel
import safeme.uz.utils.*

@AndroidEntryPoint
class PinCodeScreen : Fragment(R.layout.screen_pin_code), View.OnClickListener {
    private var _binding: ScreenPinCodeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PinCodeViewModel by viewModels<PinCodeVIewModelImpl>()
    private var create = false
    private var createAfterLogin = false
    private var edit = false
    private var delete = false
    private var isOpen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ScreenPinCodeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        hideKeyboard()

        val state = arguments?.getString(Keys.PIN_BUNDLE_KEY)

        if (state.equals(Keys.PIN_EDIT)) {
            edit = true
            viewModel.getCurrentPin()
        } else if (state.equals(Keys.PIN_DELETE)) {
            delete = true
            viewModel.getCurrentPin()
        } else if (state.equals(Keys.PIN_CREATE)) {
            binding.skipButton.invisible()
            create = true
        } else if (state.equals(Keys.PIN_OPEN)) {
            binding.skipButton.setText(R.string.forgot_pin)
            viewModel.getCurrentPin()
            isOpen = true
            binding.skipButton.visible()
        } else if (state.equals(Keys.PIN_CREATE_AFTER_LOGIN)) {
            binding.skipButton.setText(R.string.skip)
            createAfterLogin = true
            binding.skipButton.visible()
        }

        if (edit) setStatusPin(R.string.enter_current_pin)
        else if (delete) setStatusPin(R.string.enter_pin_again)
        else if (create) setStatusPin(R.string.create_pin)
        else if (isOpen) setStatusPin(R.string.enter_current_pin)
        else if (createAfterLogin) setStatusPin(R.string.create_pin)

        initObservers()
        initViews()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initObservers() = with(viewModel) {
        errorLiveData.observe(this@PinCodeScreen, errorObserver)
        messageLiveData.observe(this@PinCodeScreen, messageObserver)
        checkPinLiveData.observe(this@PinCodeScreen, checkPinObserver)
        setPinLiveData.observe(this@PinCodeScreen, setPinObserver)
        saveNewPinLiveData.observe(this@PinCodeScreen, savePinCodeObserver)
        getCurrentPinLiveData.observe(this@PinCodeScreen) { currentPin = it }
        resetPinCodeLiveData.observe(this@PinCodeScreen, resetPinCodObserver)
    }

    private val resetPinCodObserver = Observer<ResetPinCodeResponse> {
        val verifyModel = VerifyModel(
            it.phone,
            getString(R.string.reset_pincode),
            VerifyType.VERIFY_PINCODE.ordinal
        )

        findNavController().navigate(
            PinCodeScreenDirections.actionPinCodeScreenToVerifyScreen(verifyModel)
        )
    }

    private val messageObserver = Observer<String> {
        snackMessage(it)
    }

    private val errorObserver = Observer<Int> {
        snackMessage(getString(it))
        binding.skipButton.enable()
    }

    private val savePinCodeObserver = Observer<Boolean> {
        val timer = object : CountDownTimer(500, 500) {
            override fun onTick(millisUntilFinished: Long) {
                setStatusPin(R.string.successfully_done)
                binding.tvPinStatus.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.color_success)
                )
            }

            override fun onFinish() {
                if (createAfterLogin) {
                    findNavController().navigate(PinCodeScreenDirections.actionPinCodeScreenToRecommendations())
                } else findNavController().popBackStack()
            }
        }
        timer.start()
    }

    private var currentPin = ""
    private val checkPinObserver = Observer<String> {
        object : CountDownTimer(100, 100) {
            override fun onTick(millisUntilFinished: Long) {
                isClickableButton(false)
                if (edit) editPinCodeObserver(it)
                else if (delete) deletePinCodeObserver(it)
                else if (create) createPinCodeObserver(it)
                else if (isOpen) checkIsOpened(it)
                else if (createAfterLogin) createPinCodeObserverAfterLogin(it)
            }

            override fun onFinish() {
                setView(1, true)
            }
        }.start()
    }

    private val setPinObserver = Observer<Int> {
        setView(it, false)
    }

    private fun setShakeAnimation(view: View) {
        val animShake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
        view.startAnimation(animShake)
    }

    private fun editPinCodeObserver(pin: String) {
        if (pin == currentPin) {
            setStatusPin(R.string.enter_new_pin)
            edit = false
            create = true
            isClickableButton(true)
        } else errorPin()
    }

    private fun deletePinCodeObserver(pin: String) {
        if (currentPin == pin) {
            viewModel.saveNewPin("")
        } else {
            setStatusPin(R.string.enter_pin_again)
            errorPin()
        }
    }

    private var newPin = ""
    private fun createPinCodeObserver(pin: String) {
        if (newPin == "") {
            newPin = pin
            setStatusPin(R.string.confirm_new_pin)
            isClickableButton(true)
        } else {
            if (newPin == pin) {
                viewModel.saveNewPin(newPin)
            } else {
                newPin = ""
                setStatusPin(R.string.enter_new_pin)
                errorPin()
            }
        }
    }

    private fun createPinCodeObserverAfterLogin(pin: String) {
        if (newPin == "") {
            newPin = pin
            setStatusPin(R.string.confirm_new_pin)
            isClickableButton(true)
        } else {
            if (newPin == pin) {
                viewModel.saveNewPin(newPin)

            } else {
                newPin = ""
                setStatusPin(R.string.enter_new_pin)
                errorPin()
            }
        }
    }

    private fun checkIsOpened(pin: String) {
        if (pin == currentPin) {
            findNavController().navigate(PinCodeScreenDirections.actionPinCodeScreenToRecommendations())
        } else {
            setShakeAnimation(binding.llPinContainer)
            binding.tvError.text = resources.getString(R.string.error_pin)
            binding.tvError.visible()
            isClickableButton(true)
        }
    }

    private fun setStatusPin(@StringRes idString: Int) {
        binding.tvPinStatus.text = resources.getString(idString)
    }

    private fun errorPin() {
        setShakeAnimation(binding.llPinContainer)
        binding.tvError.text = resources.getString(R.string.error_pin)
        binding.tvError.visible()
        isClickableButton(true)
    }

    private fun initViews() = with(binding) {
        binding.skipButton.enable()
        btn0.setOnClickListener(this@PinCodeScreen)
        btn1.setOnClickListener(this@PinCodeScreen)
        btn2.setOnClickListener(this@PinCodeScreen)
        btn3.setOnClickListener(this@PinCodeScreen)
        btn4.setOnClickListener(this@PinCodeScreen)
        btn5.setOnClickListener(this@PinCodeScreen)
        btn6.setOnClickListener(this@PinCodeScreen)
        btn7.setOnClickListener(this@PinCodeScreen)
        btn8.setOnClickListener(this@PinCodeScreen)
        btn9.setOnClickListener(this@PinCodeScreen)
        clearButton.setOnClickListener(this@PinCodeScreen)
        skipButton.setOnClickListener(this@PinCodeScreen)
    }

    private fun check(code: String) {
        if (pinCode.length < 4) {
            pinCode += code
            viewModel.check(pinCode)
        }
    }

    private var pinCode = ""
    override fun onClick(view: View?): Unit = with(binding) {
        when (view) {
            skipButton -> {
                if (isOpen) {
                    skipButton.disable()
                    viewModel.resetPinCode()
                } else {
                    findNavController().navigate(PinCodeScreenDirections.actionPinCodeScreenToRecommendations())
                }
            }

            btn0 -> check("0")
            btn1 -> check("1")
            btn2 -> check("2")
            btn3 -> check("3")
            btn4 -> check("4")
            btn5 -> check("5")
            btn6 -> check("6")
            btn7 -> check("7")
            btn8 -> check("8")
            btn9 -> check("9")
            clearButton -> {
                if (pinCode.isNotEmpty()) {
                    pinCode = pinCode.substring(0, pinCode.length - 1)
                    viewModel.check(pinCode)
                }
            }
        }
    }

    private fun setView(it: Int, initial: Boolean) = with(binding) {
        border1.enable()
        border2.enable()
        border3.enable()
        border4.enable()
        inner1.enable()
        inner2.enable()
        inner3.enable()
        inner4.enable()
        if (!initial) {
            binding.tvError.invisible()
            when (it) {
                1 -> {
                    border1.disable()
                    inner1.disable()
                }

                2 -> {
                    border1.disable()
                    inner1.disable()
                    border2.disable()
                    inner2.disable()
                }

                3 -> {
                    border1.disable()
                    inner1.disable()
                    border2.disable()
                    inner2.disable()
                    border3.disable()
                    inner3.disable()
                }

                4 -> {
                    border1.disable()
                    inner1.disable()
                    border2.disable()
                    inner2.disable()
                    border3.disable()
                    inner3.disable()
                    border4.disable()
                    inner4.disable()
                }
            }
        } else {
            pinCode = ""
        }
    }

    private fun isClickableButton(isClick: Boolean) = with(binding) {
        btn0.isEnabled = isClick
        btn1.isEnabled = isClick
        btn2.isEnabled = isClick
        btn3.isEnabled = isClick
        btn4.isEnabled = isClick
        btn5.isEnabled = isClick
        btn6.isEnabled = isClick
        btn7.isEnabled = isClick
        btn8.isEnabled = isClick
        btn9.isEnabled = isClick
        clearButton.isEnabled = isClick
    }


}
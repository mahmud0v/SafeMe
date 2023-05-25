package safeme.uz.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import safeme.uz.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

fun Fragment.snackMessage(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).apply {
        setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.color_snack_default))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }.show()
}

fun Fragment.snackMessageLong(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).apply {
        setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.color_snack_default))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }.show()
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Fragment.showKeyboard() {
    view?.let { activity?.showKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Activity.showKeyboard() {
    showKeyboard(currentFocus ?: View(this))
}

fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.visible() {
    if (!this.isVisible) this.visibility = View.VISIBLE
}

fun View.gone() {
    if (this.isVisible) this.visibility = View.GONE
}

fun View.invisible() {
    if (this.isVisible) this.visibility = View.INVISIBLE
}

fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}

fun getCurrentDay(): String {
    val currentYear: Number
    val currentMonth: Number
    val currentDayOfMonth: Int
    val calendar = Calendar.getInstance()
    currentYear = calendar.get(Calendar.YEAR)
    currentMonth = calendar.get(Calendar.MONTH)
    currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val monString: String
    if (currentMonth < 9) {
        monString = "0${currentMonth + 1}"
    } else {
        monString = (currentMonth + 1).toString()
    }
    val dayString: String
    if (currentDayOfMonth < 10) {
        dayString = "0${currentDayOfMonth}"
    } else {
        dayString = currentDayOfMonth.toString()
    }
    return "$dayString.$monString.$currentYear"
}

fun getTimeInMillis(): String {
    val currentMilliSecond: Long
    val calendar = Calendar.getInstance()
    currentMilliSecond = calendar.timeInMillis
    return "$currentMilliSecond"
}

fun dateParseToString(date: Date, format: String): String {
    val formatter = SimpleDateFormat(format)
    return formatter.format(date)
}

fun Fragment.enableBackToolbar() {

}

fun Number.dpToPx(): Int {
    val dp = this.toFloat()
    val px = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        Resources.getSystem().displayMetrics
    )
    return px.toInt()
}



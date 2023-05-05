package safeme.uz.utils

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.google.android.material.R

object Util {
    fun openDatePickerDotly(context: Context, currentDate: String, etDateOfBirth: AppCompatEditText) {
        val currentYear: Int
        val currentMonth: Int
        val currentDay: Int

        if (currentDate.length == 10) {
            val tvDate = currentDate.split(".")
            currentYear = tvDate[0].toInt()
            currentMonth = tvDate[1].toInt()
            currentDay = tvDate[2].toInt()
        } else {
            val tvDate = getCurrentDay().split(".")
            currentYear = tvDate[2].toInt()
            currentMonth = tvDate[1].toInt()
            currentDay = tvDate[0].toInt()
        }

        val tvDatePickerDialogListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val monthString: String = if (month < 9) {
                "0${month + 1}"
            } else {
                (month + 1).toString()
            }
            val dayString: String = if (day < 10) {
                "0${day}"
            } else {
                day.toString()
            }
            val dateOfBirth = "$year.$monthString.$dayString"
            etDateOfBirth.setText(dateOfBirth)
        }

        val tvDatePickerDialog = DatePickerDialog(
            context,
            R.style.ThemeOverlay_AppCompat_Dialog,
            tvDatePickerDialogListener,
            currentYear,
            currentMonth - 1,
            currentDay
        )
        tvDatePickerDialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(context, R.color.m3_ref_palette_white)
        )
        tvDatePickerDialog.show()
    }
}
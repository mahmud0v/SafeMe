package safeme.uz.utils

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import safeme.uz.R
import safeme.uz.data.model.DrawerItemInfo

object Util {
    fun openDatePickerDotly(
        context: Context,
        currentDate: String,
        etDateOfBirth: AppCompatEditText
    ) {
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
            val dateOfBirth = "$dayString.$monthString.$year"
            etDateOfBirth.setText(dateOfBirth)
        }

        val tvDatePickerDialog = DatePickerDialog(
            context,
            androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog,
            tvDatePickerDialogListener,
            currentYear,
            currentMonth - 1,
            currentDay
        )
        tvDatePickerDialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(context, com.google.android.material.R.color.m3_ref_palette_white)
        )
        tvDatePickerDialog.show()
    }

    fun getAppealTypes(): ArrayList<String> {
        return arrayListOf(
            "ariza", "shikoyat", "taklif"
        )
    }

    fun getInfoDrawerItems():ArrayList<DrawerItemInfo>{
        return arrayListOf(
            DrawerItemInfo(R.drawable.ic_recommendations,R.drawable.ic_white_recommendations,R.color.recommendation_icon_color,R.string.recommendations),
            DrawerItemInfo(R.drawable.ic_announcements,R.drawable.ic_white_announcements,R.color.announcement_icon_color,R.string.announcements),
            DrawerItemInfo(R.drawable.game,R.drawable.white_game,R.color.game_icon_color,R.string.games),
            DrawerItemInfo(R.drawable.ic_questionnaire,R.drawable.ic_white_questionnaire,R.color.poll_icon_color,R.string.questionnaire),
            DrawerItemInfo(R.drawable.ic_prevention_inspector,R.drawable.ic_prevention_inspector,R.color.inpector_icon_color,R.string.inspectors),
            DrawerItemInfo(R.drawable.ic_appeals,R.drawable.ic_white_appeals,R.color.appeal_icon_color,R.string.appeals),
            DrawerItemInfo(R.drawable.ic_about_us,R.drawable.ic_white_about_us,R.drawable.ic_about_us,R.string.about_us)
        )
    }
}
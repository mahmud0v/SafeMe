package safeme.uz.presentation.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import safeme.uz.R
import safeme.uz.R.color.hint_color

class SpinnerAdapter(context: Context, list: ArrayList<String>) :
    ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list) {


    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = null
        if (position == 0) {
            val textView = TextView(context)
            textView.height = 0
            view = textView
        } else {
            view = super.getDropDownView(position, null, parent)

        }
        parent.isVerticalScrollBarEnabled = false
        return view!!
    }





}
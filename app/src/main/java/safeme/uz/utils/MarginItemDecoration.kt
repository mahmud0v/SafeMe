package safeme.uz.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.apply {
                left = 16.dpToPx()
                right = 8.dpToPx()
            }
        } else {
            outRect.apply {
                left = 8.dpToPx()
                right = 16.dpToPx()
            }
        }

    }
}
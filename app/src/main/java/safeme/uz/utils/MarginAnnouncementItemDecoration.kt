package safeme.uz.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginAnnouncementItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (state.itemCount - 1 == parent.getChildAdapterPosition(view)) {
            outRect.apply {
                bottom = 16.dpToPx()
            }
        }

        when(parent.getChildAdapterPosition(view)){
            0 -> {
                outRect.apply {
                    top = 12.dpToPx()
                }
            }
            state.itemCount - 1 -> {
                outRect.apply {
                    bottom = 16.dpToPx()
                }
            }
        }

    }
}
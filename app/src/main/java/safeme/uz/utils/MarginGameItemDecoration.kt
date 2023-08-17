package safeme.uz.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginGameItemDecoration : RecyclerView.ItemDecoration() {

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

    }
}
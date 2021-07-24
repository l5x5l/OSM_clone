package com.example.osm.select

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private var size12 : Int = dpToPx(context, 12)
    private var size6 : Int = dpToPx(context, 6)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)

        if (position / 3 == 0) {
            outRect.bottom = size12
        }


        outRect.left = size6
        outRect.right = size6

    }

    private fun dpToPx(context: Context, dp : Int) : Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
    }
}

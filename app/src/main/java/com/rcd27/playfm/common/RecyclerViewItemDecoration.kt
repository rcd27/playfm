package com.rcd27.playfm.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Объект для декорирования [androidx.recyclerview.widget.RecyclerView] в рантайме.
 */
object RecyclerViewItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.apply {
            top = 8
            left = 8
            right = 8
        }
    }
}
package com.rcd27.playfm.extensions

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatButton

/** Установить нужный [Drawable] справа от текста кнопки [AppCompatButton]. */
fun AppCompatButton.setRightDrawable(d: Drawable?) {
    this.setCompoundDrawablesWithIntrinsicBounds(
        null,
        null,
        d,
        null
    )
}
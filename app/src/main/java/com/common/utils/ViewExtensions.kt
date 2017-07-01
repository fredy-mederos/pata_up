package com.common.utils

import android.databinding.BindingAdapter
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import com.common.app.CommonApplication

/**
 * Created by @Fredy.
 */
@BindingAdapter("font_name")
fun TextView.setFontName(fontName: String) {
    typeface = getTypeFace(fontName) ?: return
}

@BindingAdapter("tint_color")
fun ImageView.setTintColor(tintColor: Int) {
    setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP)
}

fun getTypeFace(fontName: String): Typeface? {
    try {
        return Typeface.createFromAsset(CommonApplication.instance.assets, fontName)
    } catch (ex: Exception) {
        MyLoggerImp.logException(ex)
        return null
    }
}
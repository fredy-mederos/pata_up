package com.common.utils

import android.databinding.BindingAdapter
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.view.View
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

fun ImageView.animateWithDrawable(drawableResource: Int) {
    animateWithDrawable(resources.getDrawable(drawableResource))
}

@BindingAdapter("animation_drawable")
fun ImageView.animateWithDrawable(drawable: Drawable) {
    setImageDrawable(drawable)
    if (drawable is AnimationDrawable)
        drawable.start()
}

@BindingAdapter("text_html")
fun TextView.setTextHtml(text: String?) {
    @Suppress("DEPRECATION")
    if (text != null && text.isNotEmpty())
        setText(Html.fromHtml(text))
    else
        setText("")
}

fun View.setHeight(height: Int) {
    val layoutP = layoutParams
    layoutP?.let {
        layoutP.height = height
    }
    layoutParams = layoutP
}

/**
 * Similar to setVisibility(View.GONE)
 */
fun View.gone() {
    this.visibility = View.GONE
}

/**
 * Similar to setVisibility(View.VISIBLE)
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

fun getTypeFace(fontName: String): Typeface? {
    try {
        return Typeface.createFromAsset(CommonApplication.instance.assets, fontName)
    } catch (ex: Exception) {
        MyLoggerImp.logException(ex)
        return null
    }
}
package com.common.utils

import android.os.Handler
import android.support.annotation.StringRes
import com.common.app.CommonApplication

/**
 * Created by @Fredy.
 */
fun <T> T.doAtTime(time: Long, action: () -> Unit) {
    Handler().postDelayed(action, time)
}

fun <T> T._getString(@StringRes stringRes: Int): String {
    return CommonApplication.instance.getString(stringRes)
}
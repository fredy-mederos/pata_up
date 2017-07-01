package com.common.utils

import android.util.Log
import com.asdevel.pataup.BuildConfig

/**
 * Created by @Fredy.
 */
object MyLoggerImp {

    /**
     * @return If myLogger will send logs or not
     */
    val isLoggable: Boolean by lazy { BuildConfig.DEBUG }

    private fun logWhite(loggerTag: String, message: () -> Any?) {
        if (isLoggable)
            Log.i(loggerTag, message()?.toString() ?: "null")
    }

    private fun logYellow(loggerTag: String, message: () -> Any?) {
        if (isLoggable)
            Log.w(loggerTag, message()?.toString() ?: "null")
    }

    private fun logRed(loggerTag: String, message: () -> Any?) {
        if (isLoggable)
            Log.e(loggerTag, message()?.toString() ?: "null")
    }

    private fun logException(message: () -> Exception?) {
        if (isLoggable)
            message()?.printStackTrace()
    }

    fun logWhite(loggerTag: String, message: Any?) {
        logWhite(loggerTag, { message })
    }

    fun logYellow(loggerTag: String, message: Any?) {
        logYellow(loggerTag, { message })
    }

    fun logRed(loggerTag: String, message: Any?) {
        logRed(loggerTag, { message })
    }

    fun logException(message: Exception?) {
        logException { message }
    }
}
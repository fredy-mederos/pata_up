package com.common.utils

/**
 * Created by @Fredy.
 */
interface MyLogger {

    val loggerTag: String
        get() = getTag(javaClass)

    fun logWhite(message: Any?) {
        MyLoggerImp.logWhite(loggerTag, message)
    }

    fun logYellow(message: Any?) {
        MyLoggerImp.logYellow(loggerTag, message)
    }

    fun logRed(message: Any?) {
        MyLoggerImp.logRed(loggerTag, message)
    }

    fun logException(message: Exception?) {
        MyLoggerImp.logException(message)
    }
}

private fun getTag(clazz: Class<*>): String {
    val tag = clazz.simpleName
    return if (tag.length <= 23) tag else tag.substring(0, 23)
}
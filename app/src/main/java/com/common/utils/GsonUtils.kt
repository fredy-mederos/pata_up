package com.common.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**

 */
object GsonUtils {

    const val CUSTOM_DATE_FORMAT = "yyyy-MM-dd"

    /**
     * @return A gson object that is ready to parse jsons using the custom dateFormat.
     */
    private fun gson(): Gson = GsonBuilder().create()

    /**
     * @return A gson object that is ready to parse json using the custom dateFormat and exclude the non marked with expose fields.
     */
    fun gsonWithExpose(): Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    fun <T> fromJson(json: String?, clazz: Class<T>): T? {
        try {
            return gson().fromJson(json, clazz)
        } catch (ex: Exception) {
            MyLoggerImp.logException(ex)
        }
        return null
    }

    fun toJson(obj: Any?): String {
        return gson().toJson(obj)
    }
}

/**
 * Return the object json or "" if any error occurs
 */
fun <T> T.toJson(): String {
    try {
        return GsonUtils.toJson(this)
    } catch (ex: Exception) {
    }
    return ""
}

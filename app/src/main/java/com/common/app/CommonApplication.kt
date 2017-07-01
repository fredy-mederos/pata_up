package com.common.app

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.asdevel.pataup.BuildConfig

/**
 * Created by @Fredy.
 */

abstract class CommonApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    /**
     * @return If the app is running in the Foreground.
     */
    fun isInForeground(): Boolean {
        val mManager = getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = mManager.runningAppProcesses

        return runningApps.any { it.uid == applicationInfo.uid && it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND }
    }

    /**
     * @return If the app is connected to internet.
     */
    private fun areWeConnected(): Boolean {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = manager.activeNetworkInfo
        return ni != null && ni.isConnected
    }

    /**
     * @return The app version name.
     */
    fun getVersionName(): String {
        var v = "0"
        try {
            v = packageManager.getPackageInfo(packageName, 0).versionName
        } catch (ignored: Exception) {
        }

        return v
    }


    /**
     * @param prefName The preferences name.
     * @return The app shared preferences.
     */
    fun getPreferences(prefName: String): SharedPreferences {
        return getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    /**
     * @return The app shared preferences.
     */
    fun getPreferences(): SharedPreferences {
        return getPreferences(javaClass.name)
    }


    companion object {

        lateinit var instance: CommonApplication

        /**
         * @return If the app is in demoMode. If true then the app is debugeable but it will work only until DEMO_EXPIRATION_DATE
         */
        fun isDebugMode(): Boolean = BuildConfig.DEBUG
    }
}

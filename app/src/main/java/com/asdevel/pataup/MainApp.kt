package com.asdevel.pataup

import com.common.app.CommonApplication

/**
 * Created by @Fredy.
 */
class MainApp: CommonApplication() {


    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        private val TEST_MODE = true
        fun isTestMode() = BuildConfig.DEBUG && TEST_MODE
    }

}
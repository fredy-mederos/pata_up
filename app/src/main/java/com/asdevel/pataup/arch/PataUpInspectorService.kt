package com.asdevel.pataup.arch

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.asdevel.pataup.arch.PataUpManager.PATA_CHECK_INTERVAL
import com.asdevel.pataup.arch.PataUpManager.PATA_URL
import com.asdevel.pataup.arch.PataUpManager.TIME_OUT_TIME
import com.common.utils.MyLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.HttpURLConnection
import java.net.URL


/**
 * Created by @Fredy.
 */
class PataUpInspectorService : Service(), MyLogger {

    var stoping = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        stoping = false
        checkPata()
        logRed("onStartCommand")
        PataUpManager.internalScanning = true
        return super.onStartCommand(intent, flags, startId)
    }

    private fun checkPata() {
        doAsync {
            while (!stoping) {
                var pataUp: Boolean
                var elapsedTime: Long
                try {
                    val url = URL(PATA_URL)
                    val httpUrlConnection = url.openConnection() as HttpURLConnection
                    httpUrlConnection.connectTimeout = TIME_OUT_TIME // Timeout is in seconds
                    httpUrlConnection.readTimeout = TIME_OUT_TIME

                    val currentTimeMillis = System.currentTimeMillis()
                    httpUrlConnection.connect()
                    pataUp = httpUrlConnection.responseCode == HttpURLConnection.HTTP_NO_CONTENT
                    elapsedTime = System.currentTimeMillis() - currentTimeMillis

                    logRed("pataUp: $pataUp Elapsed Time:$elapsedTime")

                } catch (ex: Exception) {
                    logRed("Error when trying to connect: ${ex.message}")
                    pataUp = false
                    elapsedTime = TIME_OUT_TIME.toLong()
                }
                uiThread {
                    onGetPataStatus(pataUp, elapsedTime)
                }
                try {
                    Thread.sleep(PATA_CHECK_INTERVAL)
                } catch (ex: Exception) {
                    logException(ex)
                }
            }
        }
    }

    override fun onDestroy() {
        stoping = true
        logRed("onDestroy")
        PataUpManager.internalScanning = false
        super.onDestroy()
    }

    fun onGetPataStatus(pataUp: Boolean, elapsedTime: Long) {
        logRed("onGetPataStatus -> pataUp: $pataUp")
        PataUpManager.onPataStateChangeTic(pataUp, elapsedTime)
    }
}
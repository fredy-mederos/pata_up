package com.asdevel.pataup.arch

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.common.utils.MyLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.HttpURLConnection
import java.net.URL


/**
 * Created by @Fredy.
 */
class PataUpInspectorService : Service(), MyLogger {

    val PATA_URL = "http://192.168.43.206:3000/generate_204"
    //    val PATA_URL = "http://google.com/generate_204"
    val PATA_CHECK_INTERVAL: Long = 10 * 1_000

    var stoping = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        stoping = false
        checkPata()
        logRed("onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    private fun checkPata() {
        doAsync {
            while (!stoping) {
                var status: Boolean
                try {
                    val url = URL(PATA_URL)
                    val httpUrlConnection = url.openConnection() as HttpURLConnection
                    httpUrlConnection.connectTimeout = 6000 // Timeout is in seconds
                    httpUrlConnection.readTimeout = 6000
                    httpUrlConnection.connect()
                    status = httpUrlConnection.responseCode == HttpURLConnection.HTTP_NO_CONTENT
                } catch (ex: Exception) {
                    logRed("Error when trying to connect: ${ex.message}")
                    status = false
                }
                uiThread {
                    onGetPataStatus(status)
                }
                try {
                    Thread.sleep(PATA_CHECK_INTERVAL)
                } catch (ex: Exception) {
                    logException(ex)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun isScanning() = !stoping

    override fun onDestroy() {
        stoping = true
        logRed("onDestroy")
        instance = null
        super.onDestroy()
    }

    fun onGetPataStatus(pataUp: Boolean) {
        logRed("onGetPataStatus -> pataUp: $pataUp")
        PataUpManager.onPataStateChangeTic(pataUp)
    }

    companion object {
        var instance: PataUpInspectorService? = null
    }
}
package com.asdevel.pataup.arch

import android.app.Notification
import android.app.PendingIntent
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Vibrator
import android.support.v7.app.NotificationCompat
import com.asdevel.pataup.MainActivity
import com.asdevel.pataup.R
import com.common.app.CommonApplication
import com.common.managers.NotificationManager
import com.common.utils.MyLogger
import com.common.utils._getString

/**
 * Created by @Fredy.
 */
object PataUpManager : MyLogger {

    private val LAST_PATA_STATUS_PREF = "LAST_PATA_STATUS_PREF"
    private val NOTIFICATION_ID = 1234521

    private var internalScanning = false

    var scanning = false
        get() = internalScanning || PataUpInspectorService.instance?.isScanning() ?: false
        set(value) {
            field = value
            internalScanning = value
            val serviceIntent = Intent(CommonApplication.instance, PataUpInspectorService::class.java)
            if (value) {
                CommonApplication.instance.startService(serviceIntent)
                launchNotification()
            } else {
                CommonApplication.instance.stopService(serviceIntent)
                NotificationManager.cancelNotification(NOTIFICATION_ID)
                lastPataStatus = false
            }
            (pataUpStatus as MutableLiveData).postValue(lastPataStatus)
        }

    private fun launchNotification() {
        NotificationManager.notifyGlobal(_getString(R.string.pata_inspector_on), _getString(if (lastPataStatus) R.string.pata_up else R.string.buscando_la_pata), R.drawable.ic_scanner_on_notify, NOTIFICATION_ID, object : NotificationManager.NotificationDecorator {
            override fun decorate(notificationBuilder: NotificationCompat.Builder) {
                val bitmap = BitmapFactory.decodeResource(CommonApplication.instance.resources, if (lastPataStatus) R.drawable.pata_up else R.drawable.dino_lupa_white)
                notificationBuilder.setLargeIcon(bitmap)
                notificationBuilder.color = CommonApplication.instance.resources.getColor(R.color.dinosaurColor)

                notificationBuilder.setAutoCancel(false)
                notificationBuilder.setContentIntent(PendingIntent.getActivity(CommonApplication.instance, NOTIFICATION_ID, Intent(CommonApplication.instance, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
            }

            override fun decorate(notification: Notification) {}
        })
    }

    val pataUpStatus: LiveData<Boolean> by lazy {
        val liveData = MutableLiveData<Boolean>()
        liveData.value = lastPataStatus
        liveData
    }

    /**
     * @return If the app is connected to internet.
     */
    private fun areWeConnected(): Boolean {
        val manager = CommonApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = manager.activeNetworkInfo
        return ni != null && ni.isConnected
    }

    private var lastPataStatus: Boolean
        get() = CommonApplication.instance.getPreferences().getBoolean(LAST_PATA_STATUS_PREF, false)
        set(value) {
            CommonApplication.instance.getPreferences().edit().putBoolean(LAST_PATA_STATUS_PREF, value).apply()
        }

    internal fun onPataStateChangeTic(pataUp: Boolean) {
        //if last state is different to current state. then notify!
        if (lastPataStatus != pataUp) {
            (pataUpStatus as MutableLiveData).postValue(pataUp)
            lastPataStatus = pataUp
            launchNotification()
            logRed("onPataStateChangeTic -> pataUp: $pataUp")

            (CommonApplication.instance.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(200L)
        }
    }
}
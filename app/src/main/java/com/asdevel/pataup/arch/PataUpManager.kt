package com.asdevel.pataup.arch

import android.app.Notification
import android.app.PendingIntent
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
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

    const private val LAST_PATA_STATUS_PREF = "LAST_PATA_STATUS_PREF"
    const private val INTERNAL_SCANNING_PREF = "INTERNAL_SCANNING_PREF"
    //    const private val ELAPSED_TIMES = "ELAPSED_TIMES"
    const private val LAST_ELAPSED_TIME_PREF = "LAST_ELAPSED_TIME_PREF"
    const private val NOTIFICATION_ID = 1234521

    //    const val PATA_URL = "http://192.168.43.206:3000/generate_204"
//    const val PATA_URL = "http://192.168.1.71:3000/generate_204"
    const val PATA_URL = "http://google.com/generate_204"

    const val PATA_CHECK_INTERVAL: Long = 10 * 1_000 //Ten seconds between pata checker tics

    const val TIME_OUT_TIME = 60 * 1_000

    const val MAX_ELAPSED_TIMES_COUNT = 10

    internal var internalScanning: Boolean
        get() = CommonApplication.instance.getPreferences().getBoolean(INTERNAL_SCANNING_PREF, false)
        set(value) {
            CommonApplication.instance.getPreferences().edit().putBoolean(INTERNAL_SCANNING_PREF, value).apply()
        }

    var scanning = false
        get() = internalScanning
        set(value) {
            field = value
            internalScanning = value
            lastPataStatus = false
            lastElapsedTime = 0

            val serviceIntent = Intent(CommonApplication.instance, PataUpInspectorService::class.java)
            if (value) {
                CommonApplication.instance.startService(serviceIntent)
                launchNotification()
            } else {
                CommonApplication.instance.stopService(serviceIntent)
                NotificationManager.cancelNotification(NOTIFICATION_ID)
            }
        }

    private fun launchNotification() {
        NotificationManager.notifyGlobal(_getString(R.string.buscador_de_patas), _getString(if (lastPataStatus) R.string.pata_up else R.string.buscando_la_pata), if (lastPataStatus) R.drawable.notify_pata_up else R.drawable.notify_searching_pata, NOTIFICATION_ID, object : NotificationManager.NotificationDecorator {
            override fun decorate(notificationBuilder: NotificationCompat.Builder) {
                val bitmap = BitmapFactory.decodeResource(CommonApplication.instance.resources, if (lastPataStatus) R.drawable.pata_up_gray else R.drawable.dino_lupa_gray)
                notificationBuilder.setLargeIcon(bitmap)

                notificationBuilder.setAutoCancel(false)
                notificationBuilder.setContentIntent(PendingIntent.getActivity(CommonApplication.instance, NOTIFICATION_ID, Intent(CommonApplication.instance, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
            }

            override fun decorate(notification: Notification) {
                notification.flags = notification.flags or NotificationCompat.FLAG_NO_CLEAR
            }
        })
    }

    val pataUpStatus: LiveData<Boolean> by lazy {
        val liveData = MutableLiveData<Boolean>()
        liveData.value = lastPataStatus
        liveData
    }

    val elapsedTimesStatus: LiveData<Long> by lazy {
        val liveData = MutableLiveData<Long>()
        liveData.value = lastElapsedTime
        liveData
    }

    /**
     * @return If the app is connected to internet.
     */
    fun areWeConnected(): Boolean {
        val manager = CommonApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = manager.activeNetworkInfo
        return ni != null && ni.isConnected
    }

    private var lastPataStatus: Boolean
        get() = CommonApplication.instance.getPreferences().getBoolean(LAST_PATA_STATUS_PREF, false)
        set(value) {
            CommonApplication.instance.getPreferences().edit().putBoolean(LAST_PATA_STATUS_PREF, value).apply()
            (pataUpStatus as MutableLiveData).postValue(value)
        }

    private var lastElapsedTime: Long
        get() = CommonApplication.instance.getPreferences().getLong(LAST_ELAPSED_TIME_PREF, 0)
        set(value) {
            CommonApplication.instance.getPreferences().edit().putLong(LAST_ELAPSED_TIME_PREF, value).apply()
            (elapsedTimesStatus as MutableLiveData).postValue(value)
        }
//    @SuppressLint("ApplySharedPref")
//    private fun addElapsedTime(eTime: Long, callback: (LongArray) -> Unit) {
//        doAsync {
//            val elapsedTimes: ArrayList<Long> = ArrayList(getElapsedTimes().toList())
//            if (elapsedTimes.size >= MAX_ELAPSED_TIMES_COUNT)
//                elapsedTimes.removeAt(0)
//            elapsedTimes.add(eTime)
//
//            //saving elapsed time
//            CommonApplication.instance.getPreferences().edit().putString(ELAPSED_TIMES, elapsedTimes.toJson()).commit()
//            uiThread {
//                callback(getElapsedTimes())
//            }
//        }
//    }

//    private fun cleanElapsedTimes() = CommonApplication.instance.getPreferences().edit().putString(ELAPSED_TIMES, null).apply()

//    private fun getElapsedTimes(): LongArray = GsonUtils.fromJson(CommonApplication.instance.getPreferences().getString(ELAPSED_TIMES, null), LongArray::class.java) ?: longArrayOf()

    internal fun onPataStateChangeTic(pataUp: Boolean, elapsedTime: Long) {
        //if last state is different to current state. then notify!
        if (lastPataStatus != pataUp) {
            lastPataStatus = pataUp
            launchNotification()
            logRed("onPataStateChangeTic -> pataUp: $pataUp")

            //Playing a sound!
            val mediaPlayer : MediaPlayer = MediaPlayer.create(CommonApplication.instance, if(pataUp) R.raw.pata_up else R.raw.pata_down)
            mediaPlayer.start()

            (CommonApplication.instance.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(200L)
        }
//        addElapsedTime(elapsedTime, { (elapsedTimesStatus as MutableLiveData).postValue(it) })
        lastElapsedTime = elapsedTime
    }
}
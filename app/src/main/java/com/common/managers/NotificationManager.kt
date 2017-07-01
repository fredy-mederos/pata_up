package com.common.managers

import android.app.Notification
import android.content.Context
import android.support.v7.app.NotificationCompat

import com.common.app.CommonApplication

/**
 * Created by @Fredy
 *
 *
 * Android notifications manager.
 */
object NotificationManager {

    /**
     * Make a notification.
     * @param title                 notification title.
     * @param message               notification message.
     * @param icon                  notification icon.
     * @param id                    notification id.
     * @param notificationDecorator notification decoration implementation. null to avoid external changes.
     */
    fun notifyGlobal(title: String, message: String, icon: Int, id: Int, notificationDecorator: NotificationDecorator?) {
        val context = CommonApplication.instance

        //creating the notification
        val mBuilder = NotificationCompat.Builder(context)

        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(icon)

        notificationDecorator?.decorate(mBuilder)

        val notification = mBuilder.build()
        notificationDecorator?.decorate(notification)

        (context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager).notify(id, notification)
    }

    /**
     * Cancels a notification.
     * @param id notification id.
     */
    fun cancelNotification(id: Int) {
        (CommonApplication.instance.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager).cancel(id)
    }

    /**
     * Used to decorate notifications. You can call the function [.notifyGlobal] and then if you want to add or change notification configuration you could implement this interface.
     */
    interface NotificationDecorator {
        /**
         * Called just before build the notification.
         * @param notificationBuilder notification builder.
         */
        fun decorate(notificationBuilder: NotificationCompat.Builder)

        /**
         * Called after the notification was build.
         * @param notification notification.
         */
        fun decorate(notification: Notification)
    }
}


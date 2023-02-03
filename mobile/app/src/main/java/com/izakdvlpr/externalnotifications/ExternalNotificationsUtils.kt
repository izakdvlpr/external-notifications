package com.izakdvlpr.externalnotifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

class ExternalNotificationsUtils(private val context: Context) {
  private val channelId = "MyChannelId"
  private val channelName = "MyChannelName"

  private fun createNotificationManager(): NotificationManager {
    return context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  }

  private fun createNotificationChannel(notificationManager: NotificationManager) {
    val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)

    notificationManager.createNotificationChannel(notificationChannel)
  }

  private fun createNotificationBuilder(): NotificationCompat.Builder {
    val notificationBuilder = NotificationCompat.Builder(context, channelId)

    notificationBuilder.setSmallIcon(R.drawable.ic_launcher_background)
    notificationBuilder.setAutoCancel(true)

    return notificationBuilder
  }

  fun sendNotification(title: String, message: String) {
    val notificationManager = createNotificationManager()

    createNotificationChannel(notificationManager)

    val notificationBuilder = createNotificationBuilder()

    notificationBuilder.setContentTitle(title)
    notificationBuilder.setContentText(message)

    notificationManager.notify(0, notificationBuilder.build())
  }
}
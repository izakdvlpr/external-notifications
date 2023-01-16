package com.izakdvlpr.externalnotifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class MyNotification(
  var context: Context,
  var title: String,
  var message: String,
) {
  val id = 0;

  var channelId = "MyChannelId"
  var channelName = "MyChannelName"

  val notificationManager =
    context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

  lateinit var notificationChannel: NotificationChannel
  lateinit var notificationBuilder: NotificationCompat.Builder

  fun sendNotification() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)

      notificationManager.createNotificationChannel(notificationChannel)
    }

    val intent = Intent(context, MainActivity::class.java)

    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    notificationBuilder = NotificationCompat.Builder(context, channelId)

    notificationBuilder.setSmallIcon(R.drawable.ic_launcher_background)
    notificationBuilder.addAction(R.drawable.ic_launcher_background, "Open Message", pendingIntent)
    notificationBuilder.setContentTitle(title)
    notificationBuilder.setContentText(message)
    notificationBuilder.setAutoCancel(true)

    notificationManager.notify(id, notificationBuilder.build())
  }
}
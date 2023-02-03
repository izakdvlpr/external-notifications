package com.izakdvlpr.externalnotifications

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.socket.client.IO
import org.json.JSONArray

class ExternalNotificationsService : Service() {
  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    val userId = "izak"

    val socket = IO.socket(
      "http://10.0.0.103:3333",
      IO.Options.builder()
        .setReconnection(true)
        .setQuery("userId=$userId")
        .build()
    )

    socket.on("notificationsUnread") { args ->
      val data = args[0] as JSONArray

      val totalNotifications = data.length()

      val moreThanOneNotifications = totalNotifications > 1

      val notification = ExternalNotificationsUtils(applicationContext)

      notification.sendNotification(
        if (moreThanOneNotifications) {
          "Novas notificações"
        } else "Nova notificação",
        if (moreThanOneNotifications) {
          "Você tem $totalNotifications notificaçôes não lidas."
        } else "Você tem 1 notificação não lida."
      )
    }

    socket.connect()

    return START_STICKY
  }

  override fun onBind(intent: Intent): IBinder? {
    return null
  }
}
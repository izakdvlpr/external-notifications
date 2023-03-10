package com.izakdvlpr.externalnotifications.handlers

import android.content.Context
import com.izakdvlpr.externalnotifications.utils.NotificationUtils
import io.socket.client.IO
import org.json.JSONArray

class SendUnreadNotificationsHandler(val context: Context) {
  fun setup() {
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

      val notification = NotificationUtils(context)

      val title = if (moreThanOneNotifications) {
        "Novas notificações"
      } else {
        "Nova notificação"
      }

      val message = if (moreThanOneNotifications) {
        "Você tem $totalNotifications notificaçôes não lidas."
      } else {
        "Você tem 1 notificação não lida."
      }

      notification.sendNotification(title, message)
    }

    socket.connect()
  }
}
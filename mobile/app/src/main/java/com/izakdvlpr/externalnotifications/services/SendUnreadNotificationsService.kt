package com.izakdvlpr.externalnotifications.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.izakdvlpr.externalnotifications.handlers.SendUnreadNotificationsHandler
import com.izakdvlpr.externalnotifications.utils.NotificationUtils
import io.socket.client.IO
import org.json.JSONArray

class SendUnreadNotificationsService : Service() {
  override fun onBind(intent: Intent): IBinder? {
    return null
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    SendUnreadNotificationsHandler(applicationContext).setup()

    return START_STICKY
  }
}
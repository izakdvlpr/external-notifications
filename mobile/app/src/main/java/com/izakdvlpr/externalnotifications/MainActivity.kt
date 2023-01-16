package com.izakdvlpr.externalnotifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.izakdvlpr.externalnotifications.theme.ExternalNotificationsTheme
import io.socket.client.IO
import org.json.JSONArray


class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      ExternalNotificationsTheme {
        ExternalNotifications()
      }
    }
  }
}

@Composable
fun ExternalNotifications() {
  val context = LocalContext.current

  var hasNotificationPermission by remember {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      mutableStateOf(
        ContextCompat.checkSelfPermission(
          context,
          Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
      )
    } else mutableStateOf(true)
  }

  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission(),
    onResult = { isGranted -> hasNotificationPermission = isGranted }
  )

  LaunchedEffect(key1 = Unit) {
    if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
  }

  LaunchedEffect(key1 = hasNotificationPermission) {
    if (hasNotificationPermission) {
      val socket = IO.socket("http://10.0.0.103:3333")

      socket.on("notificationsUnread") { args ->
        val data = args[0] as JSONArray
        val size = data.length()

        if (size > 1) {
          sendNotification(
            context,
            "Novas notificações",
            "Você tem mais de uma notificação. Clique aqui para visualizar."
          )
        }
      }

      socket.connect()
    }
  }

  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(text = "Notificações")
  }
}

private fun sendNotification(context: Context, title: String, message: String) {
  var channelId = "MyChannelId"
  var channelName = "MyChannelName"

  val notificationId = 0;

  val notificationManager =
    context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

  val notificationChannel =
    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    notificationManager.createNotificationChannel(notificationChannel)
  }

  val intent = Intent(context, MainActivity::class.java)

  val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

  val notificationBuilder = NotificationCompat.Builder(context, channelId)

  notificationBuilder.setSmallIcon(R.drawable.ic_launcher_background)
  notificationBuilder.addAction(R.drawable.ic_launcher_background, "Open Message", pendingIntent)
  notificationBuilder.setContentTitle(title)
  notificationBuilder.setContentText(message)
  notificationBuilder.setAutoCancel(true)

  notificationManager.notify(notificationId, notificationBuilder.build())
}
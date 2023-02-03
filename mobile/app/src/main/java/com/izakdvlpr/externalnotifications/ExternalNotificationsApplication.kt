package com.izakdvlpr.externalnotifications

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Intent
import android.os.Bundle


class ExternalNotificationsApplication : Application(), ActivityLifecycleCallbacks {
  private var externalNotificationsServiceIntent: Intent? = null

  override fun onCreate() {
    super.onCreate()

    externalNotificationsServiceIntent = Intent(this, ExternalNotificationsService::class.java)

    registerActivityLifecycleCallbacks(this)
  }

  override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}

  override fun onActivityStarted(activity: Activity) {
  }

  override fun onActivityResumed(activity: Activity) {
    startService(externalNotificationsServiceIntent)
//    stopService(externalNotificationsServiceIntent)
  }

  override fun onActivityPaused(activity: Activity) {
//    startService(externalNotificationsServiceIntent)
  }

  override fun onActivityStopped(activity: Activity) {
  }

  override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
  }

  override fun onActivityDestroyed(activity: Activity) {
  }
}

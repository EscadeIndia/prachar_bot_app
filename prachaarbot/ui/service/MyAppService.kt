package com.prachaarbot.ui.service



import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import com.prachaarbot.R

class MyAppService : Service() {

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent == null) {
            return START_STICKY
        }
        val notification = Notification.Builder(this, "2312")
            .setContentTitle("Prachaar app is running")
            .setSmallIcon(R.drawable.logo_prachaarbot)
            .build()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            startForeground(1, notification)
        } else {
            startForeground(1, notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL)
        }
        startForeground(1, notification)

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}

package com.prachaarbot

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
internal class App() : Application() {

    override fun onCreate() {
        super.onCreate()
     if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         val channel = NotificationChannel("2312", "Prachaar app running", NotificationManager.IMPORTANCE_HIGH)
         val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
         notificationManager.createNotificationChannel(channel)

     }
        Glide
            .with(applicationContext)
            .applyDefaultRequestOptions(
                RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL) )
    }
}
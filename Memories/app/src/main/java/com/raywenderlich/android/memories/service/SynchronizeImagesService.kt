package com.raywenderlich.android.memories.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.raywenderlich.android.memories.App
import com.raywenderlich.android.memories.model.result.Success
import com.raywenderlich.android.memories.ui.main.MainActivity
import com.raywenderlich.android.memories.utils.FileUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val NOTIFICATIONS_CHANNEL_NAME = "Synchronize service"
const val NOTIFICATION_ID = "1"
class SynchronizeImagesService: Service() {
    private val remoteApi by lazy { App.remoteApi }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        clearStorage()
        showNotifications()
        fetchImages()
        return START_NOT_STICKY
    }
    private fun showNotifications(){
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification = NotificationCompat.Builder(this, NOTIFICATION_ID)
                .setContentTitle("Synchronization service")
                .setContentText("Download images")
                .setContentIntent(pendingIntent)
                .build()
        startForeground(1, notification)
    }
    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(
                    NOTIFICATION_ID,
                    NOTIFICATIONS_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }
    private fun clearStorage(){
        //FileUtils.cler
    }
    private fun fetchImages(){
        GlobalScope.launch {
            val result = remoteApi.getImages()
            if (result is Success){
                val imageArray = result.data.map { it.imagePath }.toTypedArray()
                FileUtils.queueImagesForDownload(applicationContext, imageArray)
            }
        }
    }
}
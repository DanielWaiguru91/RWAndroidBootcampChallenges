package com.raywenderlich.android.memories.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.raywenderlich.android.memories.utils.FileUtils
import com.raywenderlich.android.memories.utils.toast

class DownloadService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val imagePath = intent?.getStringExtra("image_path")
        if (imagePath != null){
            downloadImage(imagePath)
        }
        else{
            Log.d("DownloadService", "Missing image")
            stopSelf()
        }
        return START_NOT_STICKY
    }
    private fun downloadImage(imagePath: String){
        Thread(Runnable {
            val file = applicationContext.externalMediaDirs.first()
            FileUtils.downloadImage(file, imagePath)
        }).start()
    }

    override fun onDestroy() {
        applicationContext.toast("Stopping service")
        super.onDestroy()
    }
}
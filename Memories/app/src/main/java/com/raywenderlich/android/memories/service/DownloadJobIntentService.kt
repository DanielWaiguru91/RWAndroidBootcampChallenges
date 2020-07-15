package com.raywenderlich.android.memories.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.raywenderlich.android.memories.utils.FileUtils
import com.raywenderlich.android.memories.utils.toast
import java.io.File

const val SERVICE_NAME = "Download Image"
class DownloadJobIntentService: JobIntentService() {
    companion object{
        private const val JOB_ID = 2
        fun startWork(context: Context, intent: Intent){
            enqueueWork(context, DownloadJobIntentService::class.java, JOB_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        val imagePath = intent.getStringExtra("image_path")
        if (imagePath != null){
            downloadImage(imagePath)
        }
        else{
            Log.d("IntentService", "Missing image path")
            stopSelf()
        }
    }
    private fun downloadImage(imagePath: String){
        val file = File(applicationContext.externalCacheDirs.first(), imagePath)
        FileUtils.downloadImage(file, imagePath)
    }
    override fun onDestroy() {
        applicationContext.toast("Stopping Service")
        super.onDestroy()
    }
}
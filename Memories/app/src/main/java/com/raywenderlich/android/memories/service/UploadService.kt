package com.raywenderlich.android.memories.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.raywenderlich.android.memories.App
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class UploadService: JobIntentService() {
    private val remoteApi by lazy { App.remoteApi }
    companion object{
        private const val JOB_ID = 4
        fun startWork(context: Context, intent: Intent){
            enqueueWork(context, UploadService::class.java, JOB_ID, intent)
        }
    }
    override fun onHandleWork(intent: Intent) {
        val filePath = intent.getStringExtra("image_path")
        if (filePath != null){
            uploadImage(filePath)
        }
    }
    private fun uploadImage(filePath: String){
        GlobalScope.launch {
            val result = remoteApi.uploadImage(File(filePath))
            val intent = Intent()
            intent.putExtra("is_upload", result.message == "Success!")
            intent.action = ACTION_IMAGE_UPLOADED
            sendBroadcast(intent)
        }
    }
}
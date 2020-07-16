package com.raywenderlich.android.memories.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

const val ACTION_IMAGE_UPLOADED = "image_uploaded"
class UploadImagesReceiver(
        private inline val onImageUploaded: (Boolean) -> Unit
): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_IMAGE_UPLOADED){
            val isUploaded = intent.getBooleanExtra("is_uploaded", false)
            onImageUploaded(isUploaded)
        }
    }
}
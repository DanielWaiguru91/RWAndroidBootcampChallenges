package com.raywenderlich.android.memories.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class ClearLocalStorageWorker(context: Context, workerParameters: WorkerParameters):
        Worker(context, workerParameters){
    override fun doWork(): Result {
        val rootFolder = applicationContext.externalMediaDirs.first()
        rootFolder?.listFiles()?.forEach {file ->
            if (file.isDirectory){
                file.deleteRecursively()
            }
            else{
                file.delete()
            }
        }
        return Result.success()
    }
}
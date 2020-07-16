/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.memories.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.raywenderlich.android.memories.App
import com.raywenderlich.android.memories.R
import com.raywenderlich.android.memories.model.Image
import com.raywenderlich.android.memories.model.result.Success
import com.raywenderlich.android.memories.networking.NetworkStatusChecker
import com.raywenderlich.android.memories.service.SynchronizeImagesService
import com.raywenderlich.android.memories.service.UploadImagesReceiver
import com.raywenderlich.android.memories.service.UploadService
import com.raywenderlich.android.memories.utils.FileUtils
import com.raywenderlich.android.memories.worker.UploadImageWorker
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Displays the user profile information.
 */

private const val REQUEST_CODE_GALLERY = 101

class SettingsFragment : Fragment() {
  private val networkStatusChecker by lazy {
    NetworkStatusChecker(activity?.getSystemService(ConnectivityManager::class.java))
  }
  private val remoteApi by lazy { App.remoteApi }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_settings, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initUi()
  }

  private fun initUi() {
    uploadImage.setOnClickListener {
      val galleryIntent = Intent(
          Intent.ACTION_PICK,
          MediaStore.Images.Media.EXTERNAL_CONTENT_URI
      )
      galleryIntent.type = "image/*"
      startActivityForResult(
          Intent.createChooser(galleryIntent, "Select Picture"),
          REQUEST_CODE_GALLERY
      )
    }
    syncImages.setOnClickListener {
      networkStatusChecker.performIfConnectedToInternet {
          GlobalScope.launch(Dispatchers.Main) {
            val result = remoteApi.getImages()
            if (result is Success){
              val images = result.data
              synchronizeImages()
            }
          }
      }
    }
  }

  private fun synchronizeImages() {
    val intent = Intent(requireContext(), SynchronizeImagesService::class.java)
    activity?.startService(intent)
    /*val clearLocalStorageWorker = OneTimeWorkRequestBuilder<ClearLocalStorageWorker>()
            .build()
    val synchronizeImageWorker = OneTimeWorkRequestBuilder<SynchronizeImageWorker>()
            .setInputData(workDataOf("images" to images.map { it.imagePath }.toTypedArray()))
            .build()
    val workManager = WorkManager.getInstance(requireContext())
    workManager.beginWith(clearLocalStorageWorker)
            .then(synchronizeImageWorker)
            .enqueue()
    workManager.getWorkInfoByIdLiveData(synchronizeImageWorker.id).observe(this, Observer {info ->
      if (info.state.isFinished){
        activity?.toast("Images Synchronized successfully!")
      }
    })*/
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
      val context = activity as? Context ?: return
      val selectedImage = data?.data ?: return
      val fileUri = FileUtils.getImagePathFromInputStreamUri(selectedImage,
      context.contentResolver,
      context)
      val intent = Intent().apply { putExtra("image_path", fileUri) }
      UploadService.startWork(requireContext(), intent)
    }
  }
}
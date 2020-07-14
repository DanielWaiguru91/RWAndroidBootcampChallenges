package com.raywenderlich.android.memories.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UploadResponse(val message:String = "", val url: String = "")
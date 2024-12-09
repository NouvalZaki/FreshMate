package com.example.freshmate.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ImageUploadResponse(

	@field:SerializedName("fruit_type")
	val fruitType: String? = null,

	@field:SerializedName("ripeness")
	val ripeness: String? = null,

	@field:SerializedName("confidence")
	val confidence: Float? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
) : Parcelable

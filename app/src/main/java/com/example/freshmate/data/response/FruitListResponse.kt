package com.example.freshmate.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class FruitListResponse(

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("data")
	val data: List<DataFruit>
) : Parcelable

@Parcelize
data class DataFruit(

	@field:SerializedName("fruit_name")
	val fruitName: String,

	@field:SerializedName("fruit_image_detail")
	val fruitImageDetail: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("fruit_desc")
	val fruitDesc: String,

	@field:SerializedName("fruit_image_preview")
	val fruitImagePreview: String
) : Parcelable

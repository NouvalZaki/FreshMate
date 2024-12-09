package com.example.freshmate.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DiseaseResponse(

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("data")
	val data: List<DataDisease>
) : Parcelable

@Parcelize
data class DataDisease(

	@field:SerializedName("dieases_preview")
	val dieasesPreview: String,

	@field:SerializedName("diseases_name")
	val diseasesName: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("diseases_desc")
	val diseasesDesc: String,

	@field:SerializedName("diseases_detail")
	val diseasesDetail: String
) : Parcelable

package com.example.trashify.data.response

import com.google.gson.annotations.SerializedName

data class CraftingResponse(

	@field:SerializedName("CraftingResponse")
	val craftingResponse: List<CraftingResponseItem?>? = null
)
data class Steps(

	@field:SerializedName("Link_Yt")
	val linkYt: String? = null,

	@field:SerializedName("Step 1")
	val step1: List<String?>? = null,

	@field:SerializedName("Step 4")
	val step4: List<String?>? = null,

	@field:SerializedName("Step 2")
	val step2: List<String?>? = null,

	@field:SerializedName("Step 3")
	val step3: List<String?>? = null
)

data class Id(

	@field:SerializedName("oid")
	val oid: String? = null
)

data class TutorialsItem(

	@field:SerializedName("Steps")
	val steps: Steps? = null,

	@field:SerializedName("Judul")
	val judul: String? = null,

	@field:SerializedName("Bahan")
	val bahan: List<String?>? = null,

	@field:SerializedName("Link_Yt")
	val linkYt: String? = null
)

data class CraftingResponseItem(

	@field:SerializedName("material")
	val material: String? = null,

	@field:SerializedName("tutorials")
	val tutorials: List<TutorialsItem?>? = null,

	@field:SerializedName("_id")
	val id: Id? = null
)

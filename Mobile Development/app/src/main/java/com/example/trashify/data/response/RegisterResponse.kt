package com.example.trashify.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("registerResult")
	val registerResult: RegisterResult? = null
)

data class RegisterResult(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)

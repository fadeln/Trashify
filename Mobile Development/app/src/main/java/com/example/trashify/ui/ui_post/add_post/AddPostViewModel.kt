package com.example.trashify.ui.ui_post.add_post

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trashify.data.response.PostResponse
import com.example.trashify.data.retrofit.ApiConfigPost
import com.example.trashify.helper.reduceFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddPostViewModel : ViewModel() {

    private val _story = MutableLiveData<PostResponse>()
    val story: LiveData<PostResponse> = _story

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadImage(imageFile: File, description: String, lat: Double?, lon: Double?, token: String) {
        _isLoading.value = true
        var latBody: RequestBody? = null
        var lonBody: RequestBody? = null
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.reduceFileImage().asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        val client = ApiConfigPost.getApiServicePost(token).addStories(multipartBody, description = descriptionBody,  lat = latBody, lon = lonBody)
        client.enqueue(object : retrofit2.Callback<PostResponse>{
            override fun onResponse(
                call: retrofit2.Call<PostResponse>,
                response: retrofit2.Response<PostResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _story.value = response.body()
                } else {
                    val failedResponse = response.body()
                    _story.value = PostResponse(error = true, message = "Upload Failed")
                    Log.e(TAG, "onFailure: $failedResponse")
                }
            }
            override fun onFailure(call: retrofit2.Call<PostResponse>, t: Throwable) {
                _isLoading.value = false
                _story.value = PostResponse(error = true, message = t.message.toString())

                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

    }

    companion object{
        private const val TAG ="ADD_STORY_VIEW_MODEL"
    }
}

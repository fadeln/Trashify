package com.example.trashify.ui.ui_post.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.trashify.data.database.PostRepository
import com.example.trashify.data.response.ListPostItem
import com.example.trashify.data.response.PostResponse
import com.example.trashify.data.retrofit.ApiConfigPost
import retrofit2.Response

class PostViewModel(private val postRepository: PostRepository) : ViewModel(){
    private val _story = MutableLiveData<PostResponse>()
    val story: LiveData<PostResponse> = _story

    private val _isLoading = MutableLiveData<Boolean>()

    val getPaging: LiveData<PagingData<ListPostItem>> = postRepository.getPaging().cachedIn(viewModelScope)

    suspend fun getAllPost(token: String){
        _isLoading.value = true
        val client = ApiConfigPost.getApiServicePost(token).getStory(location = 1)
        client.enqueue(object : retrofit2.Callback<PostResponse> {
            override fun onResponse(call: retrofit2.Call<PostResponse>, response: Response<PostResponse>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _story.value = response.body()
                }else {
                    _story.value = response.body()
                    if (response.code() == 401){
                        _story.value = PostResponse(error = true, message = response.message())
                    }
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override  fun onFailure(call: retrofit2.Call<PostResponse>, t: Throwable) {
                _isLoading.value = false
                _story.value = PostResponse(error = true, message = t.message.toString())
                Log.e(TAG, "onFailure Fatal: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "UserViewModel"
    }

}
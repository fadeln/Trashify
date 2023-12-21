package com.example.trashify.data.database

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.trashify.data.response.ListPostItem
import com.example.trashify.data.retrofit.ApiServicePost

class PostRepository (private val storyDatabase: PostDatabase, private val apiService: ApiServicePost){
    fun getPaging(): LiveData<PagingData<ListPostItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = PostRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
                storyDatabase.postDao().getAllStory()
            }
        ).liveData
    }
}
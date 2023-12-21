package com.example.trashify.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.trashify.data.database.PostDatabase
import com.example.trashify.data.response.ListPostItem

class PostPaging (private val token: String, private val database: PostDatabase) :  PagingSource<Int, ListPostItem>(){
    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListPostItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListPostItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = database.postDao().getAllStory()

            return responseData.load(params)
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}
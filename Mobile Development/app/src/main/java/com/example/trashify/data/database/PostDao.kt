package com.example.trashify.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trashify.data.response.ListPostItem

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(quote: List<ListPostItem>)

    @Query("SELECT * FROM post")
    fun getAllStory(): PagingSource<Int, ListPostItem>

    @Query("DELETE FROM post")
    suspend fun deleteAll()
}
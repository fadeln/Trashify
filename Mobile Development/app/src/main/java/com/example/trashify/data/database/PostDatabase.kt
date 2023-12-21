package com.example.trashify.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.trashify.data.response.ListPostItem

@Database(
    entities = [ListPostItem::class, RemoteKeys::class],
    version = 5,
    exportSchema = false
)
abstract class PostDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: PostDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): PostDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PostDatabase::class.java, "post_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
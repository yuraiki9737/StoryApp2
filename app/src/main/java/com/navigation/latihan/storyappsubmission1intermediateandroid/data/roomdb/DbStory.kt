package com.navigation.latihan.storyappsubmission1intermediateandroid.data.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.StoryApp


@Database(
    entities = [StoryApp::class, RemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class DbStory : RoomDatabase(){

    abstract fun daoStory(): StoryDao
    abstract fun daoKeyRemote(): RemoteKeyDao

    companion object{
        @Volatile
        private var INSTANCESTORY : DbStory? = null
        @JvmStatic
        fun getInstance(context: Context): DbStory {
            return INSTANCESTORY ?: synchronized(this) {
                INSTANCESTORY ?: Room.databaseBuilder(
                    context.applicationContext,
                    DbStory::class.java, "db_story.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCESTORY = it }
            }
        }

    }
}
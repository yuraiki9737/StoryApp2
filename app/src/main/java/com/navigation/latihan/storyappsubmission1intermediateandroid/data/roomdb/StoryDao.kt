package com.navigation.latihan.storyappsubmission1intermediateandroid.data.roomdb

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.StoryApp

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createStory(storyEntity: List<StoryApp>)

    @Query("SELECT * FROM storyapp")
    fun getAppStory(): PagingSource<Int, StoryApp>

    @Query("DELETE FROM storyapp")
    suspend fun deleteAll()
}
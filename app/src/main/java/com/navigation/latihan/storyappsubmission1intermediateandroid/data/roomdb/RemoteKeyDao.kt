package com.navigation.latihan.storyappsubmission1intermediateandroid.data.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStory(remoteKey : List<RemoteKey>)

    @Query("SELECT * FROM key_remote WHERE id = :id")
    suspend fun getKeyRemoteId(id : String): RemoteKey?

    @Query("DELETE FROM key_remote")
    suspend fun deleteKeyRemote()
}
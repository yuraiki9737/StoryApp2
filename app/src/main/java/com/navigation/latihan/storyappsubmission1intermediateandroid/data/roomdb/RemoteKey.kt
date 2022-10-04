package com.navigation.latihan.storyappsubmission1intermediateandroid.data.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "key_remote")
data class RemoteKey (

    @PrimaryKey val id : String,
    val prevKey : Int?,
    val nextKey : Int?
)
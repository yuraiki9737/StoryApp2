package com.navigation.latihan.storyappsubmission1intermediateandroid.data.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

data class StoryResponse(
    val listStory: List<StoryApp>,
    val error: Boolean,
    val message: String,

)


@Parcelize
@Entity(tableName = "storyapp")
data class StoryApp(
    val photoUrl: String,
    val name: String,
    val description: String,
    @PrimaryKey
    val id: String,
    val lat : Double,
    val lon : Double
) : Parcelable
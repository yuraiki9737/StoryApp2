package com.navigation.latihan.storyappsubmission1intermediateandroid.injection

import android.content.Context
import com.navigation.latihan.storyappsubmission1intermediateandroid.api.RetrofitClient
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.repository.StoryRepo
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.roomdb.DbStory

object Injection {
    fun provideRepository(context: Context): StoryRepo {
        val dbStory = DbStory.getInstance(context)
        val retrofitClient = RetrofitClient.getApiStoryApp()
        return StoryRepo(dbStory, retrofitClient)
    }
}
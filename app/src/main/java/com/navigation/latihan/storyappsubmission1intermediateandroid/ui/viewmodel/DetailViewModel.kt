package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.StoryApp

class DetailViewModel : ViewModel() {
    lateinit var storyAppItem: StoryApp

    fun detailStory(storyApp: StoryApp) : StoryApp{
        storyAppItem = storyApp
        return storyAppItem
    }
}
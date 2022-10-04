package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.repository.StoryRepo

class MapsStoryViewModel(private val storyRepo: StoryRepo): ViewModel() {
    fun getStory(token: String)= storyRepo.getMap(token)
}
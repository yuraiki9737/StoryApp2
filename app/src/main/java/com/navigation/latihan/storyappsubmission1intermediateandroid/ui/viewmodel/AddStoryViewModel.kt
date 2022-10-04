package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.repository.StoryRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel (private val storyRepo: StoryRepo): ViewModel() {
    fun uploadStory(token: String,
                    description: RequestBody,
                    image: MultipartBody.Part,
                    lat: RequestBody? = null,
                    lon: RequestBody? = null) = storyRepo.uploadStory(token, description, image, lat, lon)
}
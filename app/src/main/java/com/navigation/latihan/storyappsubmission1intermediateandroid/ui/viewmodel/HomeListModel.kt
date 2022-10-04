package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.repository.StoryRepo
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.StoryApp

class HomeListModel (private val storyRepo: StoryRepo) : ViewModel() {

    fun getStory(token: String): LiveData<PagingData<StoryApp>> {
        return storyRepo.getPagingStory(token).cachedIn(viewModelScope).asLiveData()
    }
}
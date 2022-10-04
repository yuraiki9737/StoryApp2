package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel

import androidx.lifecycle.*
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.model.LoginUser
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.storage.PreferenceAkunStory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel( private val preferenceAkunStory:PreferenceAkunStory) : ViewModel() {


    fun getUser(): Flow<LoginUser>{
        return preferenceAkunStory.getAkunStory()
    }

    fun logout() {
        viewModelScope.launch {
            preferenceAkunStory.logoutStoryApp()
        }
    }



}
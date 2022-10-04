package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.repository.StoryRepo


class MainViewModel (private val storyRepo: StoryRepo): ViewModel(){
    fun login(email : String, password : String)=
        storyRepo.loginAkun(email, password)
    }




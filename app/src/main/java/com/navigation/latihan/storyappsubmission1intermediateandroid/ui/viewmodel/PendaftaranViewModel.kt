package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.repository.StoryRepo

class PendaftaranViewModel (private val storyRepo: StoryRepo) : ViewModel(){

    fun registerAccount(name : String, email : String, password: String)=
        storyRepo.registerAkun(name, email, password)
    }

package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.storage.PreferenceAkunStory

class FactoryViewModelStoryApp  (private val  preferenceAkunStory: PreferenceAkunStory) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(preferenceAkunStory) as T
            }
                else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}

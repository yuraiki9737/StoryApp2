package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.repository.StoryRepo
import com.navigation.latihan.storyappsubmission1intermediateandroid.injection.Injection

class ViewModelFactory private constructor(private val storyRepo:StoryRepo): ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(HomeListModel::class.java)->{
                HomeListModel(storyRepo) as T
            } modelClass.isAssignableFrom(MapsStoryViewModel::class.java) -> {
                MapsStoryViewModel(storyRepo) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(storyRepo) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(storyRepo) as T
            }
            modelClass.isAssignableFrom(PendaftaranViewModel::class.java) -> {
                PendaftaranViewModel(storyRepo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)

        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}
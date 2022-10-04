package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.splashscreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.navigation.latihan.storyappsubmission1intermediateandroid.MenuHome
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.storage.PreferenceAkunStory
import com.navigation.latihan.storyappsubmission1intermediateandroid.databinding.ActivitySplashScreenStroryAppBinding
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.login.MainActivity
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel.FactoryViewModelStoryApp
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

private val Context.dataStoreStoryApp: DataStore<Preferences> by preferencesDataStore(name = "akunstory")

class SplashScreenStroryApp : AppCompatActivity() {

    private lateinit var binding : ActivitySplashScreenStroryAppBinding
    private val second = 2000L
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenStroryAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
    }

    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(
            this,
            FactoryViewModelStoryApp(PreferenceAkunStory.getInstanceStoryApp(dataStoreStoryApp))
        )[HomeViewModel::class.java]

        lifecycleScope.launchWhenCreated {
            launch {
                homeViewModel.getUser().collect(){
                    if(it.isLogin){
                        gotoMainActivity(true)
                    }
                    else gotoMainActivity(false)
                }
            }
        }
    }

    private fun gotoMainActivity(boolean: Boolean){
        if (boolean) {
            Handler(Looper.getMainLooper()).postDelayed({

            startActivity(
                Intent(this, MenuHome::class.java),
                ActivityOptionsCompat.makeSceneTransitionAnimation(this as Activity).toBundle()
            )
            finish()
            }, second)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
            startActivity(
                Intent(this, MainActivity::class.java),
                ActivityOptionsCompat.makeSceneTransitionAnimation(this as Activity).toBundle()
            )
            finish()
            }, second)
        }
    }


}
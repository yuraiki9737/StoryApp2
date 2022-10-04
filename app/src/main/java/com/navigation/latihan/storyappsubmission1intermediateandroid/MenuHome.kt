package com.navigation.latihan.storyappsubmission1intermediateandroid

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.model.LoginUser
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.storage.PreferenceAkunStory
import com.navigation.latihan.storyappsubmission1intermediateandroid.databinding.ActivityMenuHomeBinding
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.home.Home
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel.FactoryViewModelStoryApp
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

private val Context.dataStoreStoryApp: DataStore<Preferences> by preferencesDataStore(name = "akunstory")

class MenuHome : AppCompatActivity() {

    private lateinit var binding: ActivityMenuHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var loginUser: LoginUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel()
        button()
    }

    private fun button(){
        binding.storyLocation.setOnClickListener {
            val storyView = Intent(this@MenuHome, Home::class.java)
            storyView.putExtra(Home.EXTRA_USER, loginUser)
            startActivity(storyView)
            finish()
        }
    }
    private fun viewModel() {

        val preferences = PreferenceAkunStory.getInstanceStoryApp(dataStoreStoryApp)
        homeViewModel = ViewModelProvider(
            this, FactoryViewModelStoryApp(preferences)
        )[HomeViewModel::class.java]
        lifecycleScope.launchWhenCreated {
            launch {
                homeViewModel.getUser().collect {
                        loginUser = LoginUser(
                            it.name,
                            it.email,
                            it.userId,
                            it.token,
                            true
                        )
                }
            }
        }
    }


}
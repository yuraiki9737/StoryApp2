package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.navigation.latihan.storyappsubmission1intermediateandroid.R
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.model.LoginUser
import com.navigation.latihan.storyappsubmission1intermediateandroid.databinding.ActivityMainBinding
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.register.PendaftaranAkun
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.ResultResponse
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.storage.PreferenceAkunStory
import com.navigation.latihan.storyappsubmission1intermediateandroid.MenuHome
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel.MainViewModel
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel.ViewModelFactory

private val Context.dataStoreStoryApp: DataStore<Preferences> by preferencesDataStore(name = "akunStory")

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        bindingButton()
        loginAccount()
    }

    private fun bindingButton() {
        binding.registrationAccount.setOnClickListener {
            val view = Intent(this@MainActivity, PendaftaranAkun::class.java)
            startActivity(view)
            finish()

        }

    }


    private fun loginAccount() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.username.text.toString()
            val password = binding.password.text.toString()

            mainViewModel.login(email, password).observe(this) {
                when(it){
                    is ResultResponse.Loading -> {
                        binding.progressLogin.visibility = View.VISIBLE
                    }
                    is ResultResponse.Success ->{
                        binding.progressLogin.visibility = View.GONE
                        val login = LoginUser(
                            it.data.name,
                            email,
                            it.data.userId,
                            it.data.token,
                            true
                        )
                        Toast.makeText(applicationContext,
                            getString((R.string.sukses)),
                            Toast.LENGTH_SHORT).show()
                        val intentStory = Intent(this, MenuHome::class.java)
                        intentStory.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intentStory)
                        finish()

                        val prefrence = PreferenceAkunStory.getInstanceStoryApp(dataStoreStoryApp)
                        lifecycleScope.launchWhenCreated {
                            prefrence.saveStoryApp(login)
                        }
                    }
                    is ResultResponse.Error -> {
                        binding.progressLogin.visibility = View.GONE
                        Toast.makeText(applicationContext,
                                getString(R.string.failed),
                                Toast.LENGTH_SHORT).show()

                    }

                }

            }

        }

    }
}
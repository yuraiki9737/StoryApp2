package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.navigation.latihan.storyappsubmission1intermediateandroid.R
import com.navigation.latihan.storyappsubmission1intermediateandroid.databinding.ActivityPendaftaranAkunBinding
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.ResultResponse
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.login.MainActivity
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel.PendaftaranViewModel
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel.ViewModelFactory


class PendaftaranAkun : AppCompatActivity() {
    private lateinit var binding: ActivityPendaftaranAkunBinding

    private val pendaftaranViewModel: PendaftaranViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendaftaranAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindingConfigure()


    }

    private fun bindingConfigure() {

        binding.buttonRegistrationAccount.setOnClickListener {

            val emailAccountStoryApp = binding.username.text.toString()
            val nameAccountStoryApp = binding.nameEdit.text.toString()
            val passwordAccount = binding.password.text.toString()


            when {

                nameAccountStoryApp.isEmpty() -> {
                    binding.nameEdit.error = getString(R.string.name_account)
                }
                emailAccountStoryApp.isEmpty() -> {
                    binding.username.error = getString(R.string.email_account)
                }
                passwordAccount.isEmpty() -> {
                    binding.password.error = getString(R.string.password_account)
                }
            else ->{

                registrationAccountStoryApp()


                }

            }

        }


    }


    private fun registrationAccountStoryApp() {
        val emailAccountStoryApp = binding.username.text.toString().trim()
        val nameAccountStoryApp = binding.nameEdit.text.toString().trim()
        val passwordAccountStoryApp = binding.password.text.toString().trim()


        pendaftaranViewModel.registerAccount(nameAccountStoryApp,emailAccountStoryApp,passwordAccountStoryApp).observe(this){
            when(it){
                is ResultResponse.Loading ->{
                    binding.progressRegister.visibility = View.VISIBLE
                }
                is ResultResponse.Success -> {
                    binding.progressRegister.visibility  =View.GONE
                    Toast.makeText(applicationContext,
                                    getString(R.string.createdAccount),
                                    Toast.LENGTH_LONG).show()
                                val registrationIntentAccount = Intent(this@PendaftaranAkun, MainActivity::class.java)
                   registrationIntentAccount.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(registrationIntentAccount)
                    finish()
                }
                is ResultResponse.Error -> {
                    binding.progressRegister.visibility = View.GONE
                    Toast.makeText(applicationContext,
                                    getString(R.string.invalidAccount),
                                    Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}




package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.addstory

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.navigation.latihan.storyappsubmission1intermediateandroid.R
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.model.LoginUser
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.ResultResponse
import com.navigation.latihan.storyappsubmission1intermediateandroid.databinding.ActivityAddStoryBinding
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.addstory.utils.Utils
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.setting.Setting
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel.AddStoryViewModel
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class AddStory : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding

    private lateinit var loginUser: LoginUser
    private var getFile: File? = null
    private var result : Bitmap? = null
    private var location: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val addStoryViewModel : AddStoryViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_PERMISSION){
            if(!allPermissionGranted()){
                Toast.makeText(this, getString(R.string.permission_no_granted), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()){
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSION,
                REQUEST_CODE_PERMISSION
            )
        }
        user()
        bindingButton()


    }

    private fun bindingButton(){
        binding.btnSetting.setOnClickListener {
            val view = Intent(this@AddStory, Setting::class.java)
            startActivity(view)

        }
        binding.buttonLocation.setOnCheckedChangeListener { _,  isCheck ->
            if (isCheck) {
                checkLocation(true)
            }
        }
        binding.buttonCamera.setOnClickListener { startCameraX() }
        binding.buttonGallery.setOnClickListener { addPhoto() }
        binding.upload.setOnClickListener { lifecycleScope.launch(Dispatchers.Main) { uploadImage() } }

    }


    private fun user(){
        loginUser = intent.getParcelableExtra(EXTRA_USER)!!
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }


    private fun checkLocation (check:Boolean){
        if(check){
            getLocation()
        }else{
            location = null
        }
    }


    private fun addPhoto(){
        val intentPhoto = Intent()
            intentPhoto.action = Intent.ACTION_GET_CONTENT
            intentPhoto.type = "image/*"
        val chooserPhoto = Intent.createChooser(intentPhoto,"Choose a Picture")
        launcherGallery.launch(chooserPhoto)
    }


    private val launcherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if (result.resultCode == RESULT_OK){
            val selectedImage : Uri = result.data?.data as Uri
            val fileImage = Utils.uriToFile(selectedImage, this@AddStory)
            getFile = fileImage

            binding.photo.setImageURI(selectedImage)
        }
    }

    private fun startCameraX(){
        val intentCamera = Intent(this, CameraX::class.java)
        launcherCamera.launch(intentCamera)
    }

    private val launcherCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Camera_X_RESULT_CODE){
            val fileCamera = it.data?.getSerializableExtra("picture") as File
            val isBackCamera= it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = fileCamera
           result = Utils.rotateBitmapImage( BitmapFactory.decodeFile(getFile?.path), isBackCamera)
            binding.photo.setImageBitmap(result)
        }
    }

    private fun uploadImage() {
        when {
            getFile != null -> {
                val fileImage = Utils.reduceFileImage(getFile as File)
                val descriptionText = binding.descriptionEdit.text.toString()
                    .toRequestBody("text/plain".toMediaType())
                val requestImage = fileImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val multipartImage: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    fileImage.name,
                    requestImage
                )
                var lat: RequestBody? = null
                var lon: RequestBody? = null
                if (location != null) {
                    lat = location?.latitude.toString().toRequestBody("text/plain".toMediaType())
                    lon = location?.longitude.toString().toRequestBody("text/plain".toMediaType())
                }

                addStoryViewModel.uploadStory(loginUser.token,
                    descriptionText,
                    multipartImage,
                    lat,
                    lon).observe(this) { story ->
                    if (story != null) {
                        when (story) {
                            is ResultResponse.Loading -> {
                                binding.progressAddStory.visibility = View.VISIBLE
                            }
                            is ResultResponse.Success -> {
                                binding.progressAddStory.visibility = View.GONE
                                Toast.makeText(this@AddStory,
                                    getString(R.string.success_upload),
                                    Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            is ResultResponse.Error -> {
                                binding.progressAddStory.visibility = View.GONE
                                Toast.makeText(this@AddStory,
                                    getString(R.string.failed_upload),
                                    Toast.LENGTH_SHORT).show()
                            }
                        }

                        }
                    }
                }else -> {
            Toast.makeText(this@AddStory,
                    getString(R.string.add_warning_image),
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        if(ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if(it!=null){
                    location = it
                    Log.d(TAG, "Lat : ${it.latitude}, Lon : ${it.longitude}")

                } else {
                    Toast.makeText(this, getString(R.string.enable_gps_permission),Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        Log.d(TAG, "$it")
        if (it[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            getLocation()
        }
    }

    companion object{
        const val EXTRA_USER = "user"
        const val Camera_X_RESULT_CODE = 200
        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
        private const val TAG = "AddStory"
    }
}
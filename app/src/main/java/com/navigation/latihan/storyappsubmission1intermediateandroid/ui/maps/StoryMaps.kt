package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.navigation.latihan.storyappsubmission1intermediateandroid.R
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.model.LoginUser
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.ResultResponse
import com.navigation.latihan.storyappsubmission1intermediateandroid.databinding.ActivityStoryMapsBinding
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel.MapsStoryViewModel
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel.ViewModelFactory

class StoryMaps : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapsBinding
    private lateinit var loginUser: LoginUser
    private val mapsStoryViewModel : MapsStoryViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginUser = intent.getParcelableExtra(EXTRA_USER)!!
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
       mMap.setMapStyle(
           MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style)
       )

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        showDataStory()
        getLocation()

    }
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted: Boolean ->
        if (isGranted) {
            getLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        if(ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION,)==PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }else{
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showDataStory(){
        val builder = LatLngBounds.Builder()
        mapsStoryViewModel.getStory(loginUser.token).observe(this){
            if (it!=null){
                when(it){
                    is ResultResponse.Loading -> {
                        binding.mapProgress.visibility = View.VISIBLE
                    }
                    is ResultResponse.Success -> {
                        binding.mapProgress.visibility = View.GONE
                        it.data.forEachIndexed { _, element ->
                            val lastLatLng = LatLng(element.lat, element.lon)

                            mMap.addMarker(MarkerOptions().position(lastLatLng).title(element.id))
                            builder.include(lastLatLng)
                            val bounds: LatLngBounds = builder.build()
                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 64))
                        }
                    }
                    is ResultResponse.Error -> {
                        binding.mapProgress.visibility = View.GONE
                        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_USER = "user"
    }
}
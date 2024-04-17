package com.doshua.recommendtraveldestination.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.doshua.recommendtraveldestination.view_model.MapViewModel
import com.doshua.recommendtraveldestination.R
import com.doshua.recommendtraveldestination.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MaiActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var gMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var binding: ActivityMainBinding
    private val  mapFragment by lazy { supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment }
    private val mapViewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.apply {
            viewModel = mapViewModel
            lifecycleOwner = this@MaiActivity
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    }

    override fun onResume() {
        super.onResume()
        checkPermission()
    }
    override fun onMapReady(googleMap: GoogleMap) {

        gMap = googleMap

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener {
                if(it != null) {
                    val currLocation = it

                    var currPosition: LatLng? = null
                    currPosition = LatLng(currLocation.latitude, currLocation.longitude)

                    val markerOptions = MarkerOptions()

                    markerOptions.apply {
                        title("서울")
                        position(currPosition)
                        snippet("한국 수도")
                    }

                    gMap.addMarker(markerOptions)

                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currPosition, 15f))
                }
            }
        } catch (e: SecurityException) { e.printStackTrace() }
    }

    private fun checkPermission() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_DENIED
        ) {
            mapViewModel.setPermissionDenied(true)

            if(mapViewModel.firstRequestFlag.value!!) {
                mapViewModel.setFirstRequestFlag(false)
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        } else {
            mapFragment.getMapAsync(this)
            mapViewModel.setPermissionDenied(false)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mapViewModel.setPermissionDenied(false)
        } else {
            mapViewModel.setPermissionDenied(true)
        }
    }
}
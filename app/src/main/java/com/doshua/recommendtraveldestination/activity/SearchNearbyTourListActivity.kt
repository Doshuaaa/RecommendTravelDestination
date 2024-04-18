package com.doshua.recommendtraveldestination.activity

import android.Manifest
import android.animation.Animator
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.doshua.recommendtraveldestination.R
import com.doshua.recommendtraveldestination.databinding.ActivitySearchNearbyTourListBinding
import com.doshua.recommendtraveldestination.dialog.SearchNearByOptionDialog
import com.doshua.recommendtraveldestination.view_model.SearchDialogViewModel
import com.doshua.recommendtraveldestination.view_model.TourListViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


private lateinit var gMap: GoogleMap

class SearchNearbyTourListActivity : AppCompatActivity(), OnMapReadyCallback {

    private val tourListViewModel: TourListViewModel by viewModels()
    private val dlgViewModel: SearchDialogViewModel by viewModels()
    private var currPosition: LatLng? = null
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val currPositionMarker by lazy {
        MarkerOptions().title("I am here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)) }
    private lateinit var binding: ActivitySearchNearbyTourListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_nearby_tour_list)
        binding.apply {
            lifecycleOwner = this@SearchNearbyTourListActivity
            tourListViewModel = this@SearchNearbyTourListActivity.tourListViewModel
            activity = this@SearchNearbyTourListActivity
        }

        tourListViewModel.tourListLiveData.observe(this) {

            for (tour in it) {
                val markerOptions = MarkerOptions()
                markerOptions.apply {
                    title(tour.addr1)
                    position(LatLng(tour.mapy.toDouble(), tour.mapx.toDouble()))
                }
                gMap.addMarker(markerOptions)

            }
            binding.loadingAnimation.cancelAnimation()
        }

        dlgViewModel.thumbPosition.observe(this) {
            searchBySetting()
        }

        binding.loadingAnimation.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator) {}

            override fun onAnimationEnd(p0: Animator) {}

            override fun onAnimationCancel(p0: Animator) {
                tourListViewModel.isLoadingLiveData.value = false
            }

            override fun onAnimationRepeat(p0: Animator) {}

        })
        getCurrLocation()
        startProcess()
    }

    private fun getCurrLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        try {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener {
                if(it != null) {
                    val currLocation = it

                    currPosition = LatLng(currLocation.latitude, currLocation.longitude)

                    currPositionMarker.position(currPosition!!)

                    gMap.addMarker(currPositionMarker)
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currPosition!!, 15f))
                }
            }
        } catch (e: SecurityException) { e.printStackTrace() }
    }

    private fun updateLocation() {

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000
        ).build()

        locationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.let {
                    for(location in it.locations) {
                        if(location != null) {
                            setLastLocation(location)
                        }
                    }
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun setLastLocation(lastLocation: Location){
        val currPosition = LatLng(lastLocation.latitude,lastLocation.longitude)

        currPositionMarker.position(currPosition)
    }

    private fun startProcess(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync (this)
    }

    fun goToMyLocation() {
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currPosition!!, 15f))
    }

    fun showOptionDialog() {
        val dlg = SearchNearByOptionDialog(this, dlgViewModel, tourListViewModel, currPosition!!)
        dlg.show(this.supportFragmentManager, "setting_dialog")
    }

    fun refresh() {
        gMap.clear()
        if(dlgViewModel.searchType != 0) {
            binding.loadingAnimation.playAnimation()
            tourListViewModel.isLoadingLiveData.value = true
            tourListViewModel.setTourListLiveData(currPosition!!.longitude, currPosition!!.latitude, dlgViewModel.searchType, dlgViewModel.thumbPosition.value!!)
        }
        getCurrLocation()
    }

    private fun searchBySetting() {
        gMap.clear()
        binding.loadingAnimation.playAnimation()
        tourListViewModel.isLoadingLiveData.value = true
        getCurrLocation()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        updateLocation()
    }
}
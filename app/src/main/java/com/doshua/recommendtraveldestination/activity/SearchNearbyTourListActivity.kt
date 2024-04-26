package com.doshua.recommendtraveldestination.activity

import android.Manifest
import android.animation.Animator
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.doshua.recommendtraveldestination.R
import com.doshua.recommendtraveldestination.SelectedLocation
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
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private lateinit var gMap: GoogleMap

class SearchNearbyTourListActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

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
                Glide.with(this@SearchNearbyTourListActivity).load(tour.firstimage).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        markerOptions.apply {
                            title(tour.addr1)
                            position(LatLng(tour.mapy.toDouble(), tour.mapx.toDouble()))
                        }

                        CoroutineScope(Dispatchers.Main).launch {
                            tourListViewModel.addMarkerItem( gMap.addMarker(markerOptions)!!,
                                SelectedLocation(
                                    tour.title,
                                    tour.addr1,
                                    tour.mapx,
                                    tour.mapy,
                                    tour.tel,
                                    tour.firstimage
                                )
                            )
                        }
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        val icon = BitmapDescriptorFactory.fromBitmap(convertToBitmap(resource, 100, 100))

                        markerOptions.apply {
                            title(tour.addr1)
                            position(LatLng(tour.mapy.toDouble(), tour.mapx.toDouble()))
                            icon(icon)
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            tourListViewModel.addMarkerItem( gMap.addMarker(markerOptions)!!,
                                SelectedLocation(
                                    tour.title,
                                    tour.addr1,
                                    tour.mapx,
                                    tour.mapy,
                                    tour.tel,
                                    tour.firstimage
                                )
                            )
                        }
                        return true
                    }
                }).submit()


            }
            binding.loadingAnimation.cancelAnimation()
        }

        dlgViewModel.searchType.observe(this) {
            try {
                getCurrLocationAndSearch()
            } catch (e: UninitializedPropertyAccessException) { e.printStackTrace() }
        }

        binding.loadingAnimation.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator) {}

            override fun onAnimationEnd(p0: Animator) {}

            override fun onAnimationCancel(p0: Animator) {
                tourListViewModel.isLoadingLiveData.value = false
            }

            override fun onAnimationRepeat(p0: Animator) {}

        })
        startProcess()
        getCurrLocationAndSearch()
    }

    private fun getCurrLocationAndSearch() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.loadingAnimation.playAnimation()
        tourListViewModel.isLoadingLiveData.value = true

        try {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener {
                if(it != null) {
                    gMap.clear()
                    val currLocation = it

                    currPosition = LatLng(currLocation.latitude, currLocation.longitude)
                    currPositionMarker.position(currPosition!!)

                    search()
                    gMap.addMarker(currPositionMarker)
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currPosition!!, 15f))
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
        try {
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currPosition!!, 15f))
        } catch (e: UninitializedPropertyAccessException) { e.printStackTrace() }
    }

    fun showOptionDialog() {
        val dlg = SearchNearByOptionDialog(this, dlgViewModel)
        dlg.show(this.supportFragmentManager, "setting_dialog")
    }

    fun refresh() {

        getCurrLocationAndSearch()
    }

    private fun search() {
        tourListViewModel.setTourListLiveData(currPosition?.longitude!!, currPosition?.latitude!!, dlgViewModel.searchType.value!!, dlgViewModel.progress.value!!.toInt() * 1000)
    }

    fun convertToBitmap(drawable: Drawable, widthPixels: Int, heightPixels: Int): Bitmap {
        val mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mutableBitmap)
        val color = Paint()
        color.textSize = 35f
        color.color = Color.CYAN
        drawable.setBounds(0, 0, widthPixels, heightPixels)

        drawable.draw(canvas)


        return mutableBitmap
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.setOnMarkerClickListener(this)
        updateLocation()
    }

    override fun onMarkerClick(marker: Marker): Boolean {

        val selectedItem = tourListViewModel.tourListHashMapLiveDate.value!![marker]
        val intent = Intent(this, SelectedLocationActivity::class.java)
        intent.putExtra("selected location", selectedItem)
        intent.putExtra("current mapX", currPosition?.longitude.toString())
        intent.putExtra("current mapY", currPosition?.latitude.toString())
        startActivity(intent)

        return true
    }
}
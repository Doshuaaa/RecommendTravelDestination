package com.doshua.recommendtraveldestination.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.doshua.recommendtraveldestination.api.Location
import com.doshua.recommendtraveldestination.api.response.LocationRes
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val okHttpClient: OkHttpClient by lazy {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()
}
var gson= GsonBuilder().setLenient().create()

private val retrofit = Retrofit.Builder()
    .baseUrl("http://apis.data.go.kr/B551011/KorService1/")
    .addConverterFactory(GsonConverterFactory.create(gson))
    .client(okHttpClient)
    .build()

object ApiObject {
    val retrofitService: Location by lazy {
        retrofit.create(Location::class.java)
    }
}

private lateinit var gMap: GoogleMap

class SearchNearbyTourListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getCurrLocation()
        CoroutineScope(Dispatchers.Main).launch {

            CoroutineScope(Dispatchers.Default).launch {

            }
        }
    }

    fun getCurrLocation() : LatLng? {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        var currPosition: LatLng? = null
        try {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener {
                if(it != null) {
                    val currLocation = it

                    currPosition = LatLng(currLocation.latitude, currLocation.longitude)

                    val markerOptions = MarkerOptions()

                    markerOptions.apply {
                        title("서울")
                        position(currPosition!!)
                        snippet("한국 수도")
                    }

//                    gMap.addMarker(markerOptions)
//
//                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currPosition!!, 15f))

                    setTravelList(currLocation.latitude, currLocation.longitude)
                }
            }
        } catch (e: SecurityException) { e.printStackTrace() }

        return currPosition
    }
    private fun setTravelList(latitude: Double, longitude: Double) {

        val call = ApiObject.retrofitService.getTourListByLocation(
            "JSON", 10, 1, "AND",  "test", longitude, latitude, "39", "20000000")

        call.enqueue(object : retrofit2.Callback<LocationRes> {
            override fun onResponse(call: Call<LocationRes>, response: Response<LocationRes>) {

                var list: List<LocationRes.Item> = response.body()!!.response.body.items.item
                val a = list.size
            }

            override fun onFailure(call: Call<LocationRes>, t: Throwable) {
                val a = t.message
                Toast.makeText(this@SearchNearbyTourListActivity, "관광지를 불러오는데 실패했어요", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
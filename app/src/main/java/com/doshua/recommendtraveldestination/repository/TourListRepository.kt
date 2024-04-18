package com.doshua.recommendtraveldestination.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.doshua.recommendtraveldestination.api.Location
import com.doshua.recommendtraveldestination.api.response.LocationRes
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TourListRepository {

    companion object {
        private const val BASE_URL = "http://apis.data.go.kr/B551011/KorService1/"
    }

    var gson= GsonBuilder().setLenient().create()

    private val okHttpClient: OkHttpClient by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    private val retrofitService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build().create(Location::class.java)

    fun setTourListByLocation(tourLiveData: MutableLiveData<List<LocationRes.Item>>, latitude: Double, longitude: Double, searchType: Int, radius: Int) {

        val call = retrofitService.getTourListByLocation(
            "JSON", 10, 1, "AND",  "test", latitude, longitude, searchType.toString(), radius.toString())

        call.enqueue(object : retrofit2.Callback<LocationRes> {
            override fun onResponse(call: Call<LocationRes>, response: Response<LocationRes>) {

                tourLiveData.value = response.body()!!.response.body.items.item
            }

            override fun onFailure(call: Call<LocationRes>, t: Throwable) {
                Log.e("call enqueue failure", t.message!!)
            }
        })
    }
}
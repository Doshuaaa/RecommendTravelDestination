package com.doshua.recommendtraveldestination.api

import com.doshua.recommendtraveldestination.api.response.LocationRes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Location {

    @GET("locationBasedList1?serviceKey=WZwxilxVy3aFeI8v4uIuEXdFkT%2BPIqais5w%2Bk9i3ib8Yz8uz5kl4wjQbwIe0KWAcvyvE0bh1rnIaJkjrFUX7eQ%3D%3D")
    fun getTourListByLocation(
        @Query("_type") dataType: String,
        @Query("numOfRows") numOfRows: Number,
        @Query("pageNo") pageNo: Number,
        @Query("MobileOS") mobileOS: String,
        @Query("MobileApp") mobileApp: String,
        @Query("mapX") mapX: Double,
        @Query("mapY") mapY: Double,
        @Query("contentTypeId") contentTypeId: String,
        @Query("radius") radius: String,

    ) : Call<LocationRes>
}
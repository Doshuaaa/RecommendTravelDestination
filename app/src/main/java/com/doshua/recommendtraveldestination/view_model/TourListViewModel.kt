package com.doshua.recommendtraveldestination.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doshua.recommendtraveldestination.SelectedLocation
import com.doshua.recommendtraveldestination.api.response.LocationRes
import com.doshua.recommendtraveldestination.repository.TourListRepository
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class TourListViewModel : ViewModel() {

    private val repository = TourListRepository()
    private val _tourListLiveData = MutableLiveData<List<LocationRes.Item>>()
    private val _tourListHashMapLiveData = MutableLiveData<HashMap<Marker, SelectedLocation>>()
    val tourListLiveData get() = _tourListLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData get() = _isLoadingLiveData
    val tourListHashMapLiveDate get() = _tourListHashMapLiveData
    init {
        _tourListHashMapLiveData.value = hashMapOf()
        _isLoadingLiveData.value = true
    }
    fun setTourListLiveData(longitude: Double, latitude: Double, searchType: Int, radius: Int) {

        repository.setTourListByLocation(_tourListLiveData, longitude, latitude, searchType, radius)
    }

    fun addMarkerItem(marker: Marker, item: SelectedLocation) {
        _tourListHashMapLiveData.value!![marker] = item
    }
}
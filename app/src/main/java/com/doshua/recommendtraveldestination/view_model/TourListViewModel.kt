package com.doshua.recommendtraveldestination.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doshua.recommendtraveldestination.api.response.LocationRes
import com.doshua.recommendtraveldestination.repository.TourListRepository

class TourListViewModel : ViewModel() {

    private val repository = TourListRepository()
    private val _tourListLiveData = MutableLiveData<List<LocationRes.Item>>()
    val tourListLiveData get() = _tourListLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData get() = _isLoadingLiveData
    init {
        _isLoadingLiveData.value = true
    }
    fun setTourListLiveData(longitude: Double, latitude: Double) {

        repository.setTourListByLocation(_tourListLiveData, longitude, latitude)
    }
}
package com.doshua.recommendtraveldestination.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {

    private val _permissionDenied = MutableLiveData<Boolean>()
    val permissionDenied get() = _permissionDenied

    private val _firstRequestFlag = MutableLiveData<Boolean>()
    val firstRequestFlag get() = _firstRequestFlag

    init {
        _firstRequestFlag.value = true
    }

    fun setPermissionDenied(isDenied: Boolean) {

        _permissionDenied.value = isDenied
    }

    fun setFirstRequestFlag(flag: Boolean) {
        _firstRequestFlag.value = flag
    }
}
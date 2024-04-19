package com.doshua.recommendtraveldestination.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchDialogViewModel : ViewModel() {

    private val _thumbPosition = MutableLiveData<Int>()
    private val _progress = MutableLiveData<String>()
    val progress get() = _progress    // radius km

    private val _searchTypeLiveData = MutableLiveData<Int>()
    val searchType get() = _searchTypeLiveData
    val thumbPosition get() = _thumbPosition

    init {
        _searchTypeLiveData.value = 12
        _progress.value = "10"


        _thumbPosition.value = 12
    }

}
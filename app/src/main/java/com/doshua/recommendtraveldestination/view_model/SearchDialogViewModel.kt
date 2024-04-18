package com.doshua.recommendtraveldestination.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchDialogViewModel : ViewModel() {

    private val _thumbPosition = MutableLiveData<Int>()
    private val _progress = MutableLiveData<String>()
    val progress get() = _progress    // radius km


     var searchType = 0
    val thumbPosition get() = _thumbPosition

}
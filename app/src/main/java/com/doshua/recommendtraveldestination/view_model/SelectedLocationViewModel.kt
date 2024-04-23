package com.doshua.recommendtraveldestination.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SelectedLocationViewModel : ViewModel() {

    private val _selectedName =  MutableLiveData<String>()
    private val _selectedPosition = MutableLiveData<String>()
    private val _selectedTel = MutableLiveData<String>()

    val selectedName get() = _selectedName
    val selectedPosition get() = _selectedPosition
    val selectedTel get() = _selectedTel

    fun setSelectedData(name: String, position: String, tel: String) {

        _selectedName.value = name
        _selectedPosition.value = position
        _selectedTel.value = tel
    }
}
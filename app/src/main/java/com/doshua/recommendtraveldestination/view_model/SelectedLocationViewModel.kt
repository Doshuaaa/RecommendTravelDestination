package com.doshua.recommendtraveldestination.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SelectedLocationViewModel : ViewModel() {

    private val _selectedName =  MutableLiveData<String>()
    private val _selectedPosition = MutableLiveData<String>()
    private val _selectedTel = MutableLiveData<String>()
    private val _selectedMapX = MutableLiveData<String>()
    private val _selectedMapY = MutableLiveData<String>()

    val selectedName get() = _selectedName
    val selectedPosition get() = _selectedPosition
    val selectedTel get() = _selectedTel
    val selectedMapX get() = _selectedMapX
    val selectedMapY get() = _selectedMapY

    fun setSelectedData(name: String, position: String, tel: String, mapX: String, mapY: String) {

        _selectedName.value = name
        _selectedPosition.value = position
        _selectedTel.value = tel
        _selectedMapX.value = mapX
        _selectedMapY.value = mapY
    }
}
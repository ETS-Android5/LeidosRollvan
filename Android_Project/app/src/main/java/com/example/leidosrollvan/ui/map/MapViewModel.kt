package com.example.leidosrollvan.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MapViewModel {
    private val _text = MutableLiveData<String>().apply {
        value = "This is map Fragment"
    }
    val text: LiveData<String> = _text
}
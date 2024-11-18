package com.example.futour_app.ui.findout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FindOutViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Find Out Fragment"
    }
    val text: LiveData<String> = _text
}
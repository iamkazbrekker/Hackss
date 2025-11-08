package com.runanywhere.startup_hackathon20.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EduVentureViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EduVentureViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EduVentureViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

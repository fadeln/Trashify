package com.example.trashify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trashify.model.AuthViewModel
import com.example.trashify.preferences.AuthPreferences

class ViewModelFactory(private val pref: AuthPreferences) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)){
            return AuthViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }
}
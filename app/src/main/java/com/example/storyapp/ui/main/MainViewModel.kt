package com.example.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.remote.response.LoginResult
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<LoginResult> {
        return userRepository.getSession().asLiveData()
    }

    fun logout(){
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
package com.example.storyapp.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import kotlinx.coroutines.launch

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun registerUser(name: String, email: String, password: String){
        viewModelScope.launch {
            userRepository.registerUser(name, email, password)
        }
    }
}
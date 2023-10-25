package com.example.storyapp.ui.signup

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.UserStoryRepository

class SignupViewModel(private val userStoryRepository: UserStoryRepository) : ViewModel() {
    fun registerUser(name: String, email: String, password: String) = userStoryRepository.registerUser(name, email, password)
}
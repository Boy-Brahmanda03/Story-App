package com.example.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserStoryRepository
import com.example.storyapp.data.local.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val userStoryRepository: UserStoryRepository): ViewModel() {
    fun loginUser(email: String, password: String) = userStoryRepository.loginUser(email, password)

    fun saveSession(user: UserModel){
        viewModelScope.launch {
            userStoryRepository.saveSessionUser(user)
        }
    }
}
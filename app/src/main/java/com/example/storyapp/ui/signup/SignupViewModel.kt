package com.example.storyapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.Result
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.remote.response.RegisterResponse
import kotlinx.coroutines.async

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {
    suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> {
        val result =
            viewModelScope.async {
                userRepository.registerUser(name, email, password)

            }
        return result.await()
    }
}
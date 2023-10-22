package com.example.storyapp.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.storyapp.data.Result
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.remote.response.RegisterResponse
import com.google.gson.Gson
import retrofit2.HttpException

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun registerUser(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val succesResponse = userRepository.registerUser(name, email, password)
            emit(Result.Success(succesResponse))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }
}
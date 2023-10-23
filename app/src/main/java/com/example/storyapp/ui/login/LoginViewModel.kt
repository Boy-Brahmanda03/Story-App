package com.example.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.Result
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.response.LoginResult
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val userRepository: UserRepository): ViewModel() {
    fun loginUser(email: String, password: String) = liveData{
        emit(Result.Loading)
        try {
            val successResponse = userRepository.loginUser(email, password)
            emit(Result.Success(successResponse))
        } catch (e : HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun saveSession(user: LoginResult){
        viewModelScope.launch {
            userRepository.saveSessionUser(user)
        }
    }
}
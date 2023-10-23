package com.example.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.Result
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.remote.response.LoginResult
import com.example.storyapp.data.remote.response.StoriesResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getAllStories(token: String) = liveData {
        emit(Result.Loading)
        try {
            val response = userRepository.getAllStories(token)
            emit(Result.Success(response))
        } catch (e : HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoriesResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun getSession(): LiveData<LoginResult> {
        return userRepository.getSession().asLiveData()
    }

    fun logout(){
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
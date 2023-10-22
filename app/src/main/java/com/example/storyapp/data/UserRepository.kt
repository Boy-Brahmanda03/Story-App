package com.example.storyapp.data

import androidx.lifecycle.liveData
import com.example.storyapp.data.remote.response.RegisterResponse
import com.example.storyapp.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService
) {
    suspend fun registerUser(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val succesResponse = apiService.registerUser(name, email, password)
            emit(Result.Success(succesResponse))
        } catch (e: HttpException){
            val errorBody = e.response()?.errorBody().toString()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}
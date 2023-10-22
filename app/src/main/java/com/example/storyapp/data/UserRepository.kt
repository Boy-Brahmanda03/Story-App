package com.example.storyapp.data

import com.example.storyapp.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService
) {
    suspend fun registerUser(name: String, email: String, password: String) = apiService.registerUser(name, email, password)


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
package com.example.storyapp.data

import com.example.storyapp.data.pref.UserPreferences
import com.example.storyapp.data.remote.response.LoginResult
import com.example.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val apiService: ApiService,
    private val pref: UserPreferences
) {
    suspend fun registerUser(name: String, email: String, password: String) = apiService.registerUser(name, email, password)

    suspend fun loginUser(email: String, password: String) = apiService.loginUser(email, password)

    suspend fun saveSessionUser(user: LoginResult){
        pref.saveSession(user)
    }

    fun getSession(): Flow<LoginResult> {
        return pref.getSession()
    }

    suspend fun logout(){
        pref.logout()
    }

    suspend fun getAllStories(token: String) = apiService.getStories(token)



    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            pref: UserPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, pref)
            }.also { instance = it }
    }
}
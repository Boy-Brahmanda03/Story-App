package com.example.storyapp.data.di

import android.content.Context
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.pref.UserPreferences
import com.example.storyapp.data.pref.dataStore
import com.example.storyapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, pref)
    }
}
package com.example.storyapp.data.di

import android.content.Context
import com.example.storyapp.data.UserStoryRepository
import com.example.storyapp.data.pref.UserPreferences
import com.example.storyapp.data.pref.dataStore
import com.example.storyapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserStoryRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService =  ApiConfig.getApiService(user.token)
        return UserStoryRepository.getInstance(apiService, pref)
    }
}
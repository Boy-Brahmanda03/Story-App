package com.example.storyapp.data.di

import android.content.Context
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.pref.PreferencesDataStore
import com.example.storyapp.data.pref.dataStore
import com.example.storyapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val pref = PreferencesDataStore.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService, pref)
    }
}
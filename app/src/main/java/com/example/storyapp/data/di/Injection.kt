package com.example.storyapp.data.di

import android.content.Context
import com.example.storyapp.data.UserStoryRepository
import com.example.storyapp.data.local.pref.UserPreferences
import com.example.storyapp.data.local.pref.dataStore
import com.example.storyapp.data.local.room.StoryDatabase
import com.example.storyapp.data.remote.retrofit.ApiConfig
object Injection { fun provideRepository(context: Context): UserStoryRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val apiService =  ApiConfig.getApiService()
        val db = StoryDatabase.getDatabase(context)
        return UserStoryRepository.getInstance(apiService, pref, db)
    }
}
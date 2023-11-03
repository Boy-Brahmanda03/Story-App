package com.example.storyapp.ui.mapstory

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.UserStoryRepository

class MapsViewModel(private val repository: UserStoryRepository): ViewModel() {
    fun getAllStoriesWithLocation(token: String) = repository.getAllStoriesWithLocation(token)
}
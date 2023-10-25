package com.example.storyapp.ui.detailstory

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.UserStoryRepository

class DetailStoryViewModel(private val userStoryRepository: UserStoryRepository) : ViewModel() {
    fun getDetailStory(id: String, token: String) = userStoryRepository.getDetailStory(id, token)
}
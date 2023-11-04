package com.example.storyapp.ui.uploadstory

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.UserStoryRepository
import java.io.File

class UploadViewModel(
    private val userStoryRepository: UserStoryRepository
) : ViewModel() {
    fun uploadStory(file: File, desc: String, token: String, long: Float?, lat: Float?) = userStoryRepository.uploadStory(file, desc, token, long, lat)
}
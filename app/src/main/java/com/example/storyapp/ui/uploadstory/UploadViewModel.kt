package com.example.storyapp.ui.uploadstory

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.UserStoryRepository
import java.io.File

class UploadViewModel(
    private val userStoryRepository: UserStoryRepository
) : ViewModel() {
    fun uploadStory(file: File, desc: String, token: String) = userStoryRepository.uploadStory(file, desc, token)
}
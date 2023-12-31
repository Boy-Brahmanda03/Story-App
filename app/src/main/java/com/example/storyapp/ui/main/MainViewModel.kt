package com.example.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.storyapp.data.UserStoryRepository
import com.example.storyapp.data.local.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val userStoryRepository: UserStoryRepository) : ViewModel() {
    fun getAllStories(token: String) = userStoryRepository.getAllStories(token).cachedIn(viewModelScope)

    fun getSession(): LiveData<UserModel> {
        return userStoryRepository.getSession().asLiveData()
    }
    fun logout(){
        viewModelScope.launch {
            userStoryRepository.logout()
        }
    }
}
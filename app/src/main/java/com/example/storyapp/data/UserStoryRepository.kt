package com.example.storyapp.data

import android.util.Log
import androidx.lifecycle.liveData
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.pref.UserPreferences
import com.example.storyapp.data.remote.response.DetailStoryResponse
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.response.RegisterResponse
import com.example.storyapp.data.remote.response.StoriesResponse
import com.example.storyapp.data.remote.response.UploadStoryResponse
import com.example.storyapp.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserStoryRepository private constructor(
    private val apiService: ApiService,
    private val pref: UserPreferences
) {
    fun registerUser(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.registerUser(name, email, password)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun loginUser(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.loginUser(email, password)
            if (!successResponse.error){
                val user = UserModel(
                    name = successResponse.loginResult.name,
                    userId = successResponse.loginResult.userId,
                    token = successResponse.loginResult.token,
                    isLogin = true
                )
                saveSessionUser(user)
                Log.d("REPO", "loginUser: $successResponse")
                emit(Result.Success(successResponse))
            } else {
                emit(Result.Error(successResponse.message))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    suspend fun saveSessionUser(user: UserModel) {
        pref.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return pref.getSession()
    }

    suspend fun logout() {
        pref.logout()
    }

    fun getAllStories() = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStories()
            if (response.error){
                emit(Result.Error(response.message))
            } else {
                emit(Result.Success(response))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoriesResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun getDetailStory(id: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailStories(id)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, DetailStoryResponse::class.java)
            emit(Result.Error(errorBody.message))
        }
    }


    fun uploadStory(file: File, desc: String) = liveData {
        emit(Result.Loading)
        try {
            val requestDesc = desc.toRequestBody("text/plain".toMediaType())
            val requestFile = file.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestFile
            )
            val response = apiService.uploadStory(multipartBody, requestDesc)
            emit(Result.Success(response.message))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UploadStoryResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    companion object {
        @Volatile
        private var instance: UserStoryRepository? = null

        fun getInstance(
            apiService: ApiService,
            pref: UserPreferences
        ): UserStoryRepository =
            instance ?: synchronized(this) {
                instance ?: UserStoryRepository(apiService, pref)
            }.also { instance = it }
    }
}
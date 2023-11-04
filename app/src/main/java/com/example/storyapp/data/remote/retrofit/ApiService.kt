package com.example.storyapp.data.remote.retrofit

import com.example.storyapp.data.remote.response.DetailStoryResponse
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.response.RegisterResponse
import com.example.storyapp.data.remote.response.StoriesResponse
import com.example.storyapp.data.remote.response.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part("lat") lat: RequestBody
    ): UploadStoryResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoriesResponse

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailStoryResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1
    ): StoriesResponse

}
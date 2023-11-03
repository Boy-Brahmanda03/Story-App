package com.example.storyapp.data.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapp.data.local.room.entity.StoriesEntity

@Dao
interface StoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoriesEntity>)

    @Query("SELECT * FROM stories")
    fun getAllStories(): PagingSource<Int, StoriesEntity>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}
package com.example.storyapp

import com.example.storyapp.data.local.room.entity.StoriesEntity

object DataDummy {
    fun generateDataDummyEntity(): List<StoriesEntity>{
        val data = arrayListOf<StoriesEntity>()
        for (i in 1..10) {
            val story = StoriesEntity(
                id = i.toString(),
                name = "name $i",
                description = "description $i",
                photoUrl = "url $i",
                lon = i.toDouble(),
                lat = i.toDouble(),
            )
            data.add(story)
        }
        return data
    }
}
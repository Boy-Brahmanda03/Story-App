package com.example.storyapp.ui.detailstory

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyapp.data.Result
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.ui.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private val detailStoryViewModel by viewModels<DetailStoryViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra(EXTRA_STORY_ID).toString()

        getDetailStory(storyId)
    }

    private fun getDetailStory(storyId: String) {
        detailStoryViewModel.getDetailStory(storyId).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        val detailResponse = result.data.story
                        bind(detailResponse)
                        showToast(result.data.message)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }
                }
            }
        }
    }

    private fun bind(detailResponse: ListStoryItem) {
        binding.apply {
            Glide.with(this@DetailStoryActivity)
                .load(detailResponse.photoUrl)
                .into(ivDetailPicture)
            tvTitleDetail.text = detailResponse.name
            tvCreatedAt.text = detailResponse.createdAt
            tvDetailDescription.text = detailResponse.description
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_STORY_ID = "extra_id_story"
    }
}
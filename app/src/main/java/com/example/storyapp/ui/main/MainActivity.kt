package com.example.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.data.Result
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.ViewModelFactory
import com.example.storyapp.ui.adapter.StoriesAdapter
import com.example.storyapp.ui.detailstory.DetailStoryActivity
import com.example.storyapp.ui.uploadstory.UploadActivity
import com.example.storyapp.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.getSession().observe(this) { user ->
            Log.d("Main Activity", "session: ${user.token} ")
            if (!user.isLogin) {
                val welcomeIntent = Intent(this, WelcomeActivity::class.java)
                Log.d("Main Activity", "not login: ${user.token} ")

                startActivity(welcomeIntent)
                finish()
            } else {
                getAllStories()
                Log.d("Main Activity", "login: ${user.token} ")
            }
        }

        binding.floatingActionButton.setOnClickListener {
            val uploadIntent = Intent(this, UploadActivity::class.java)
            startActivity(uploadIntent)
        }
    }
    private fun getAllStories(){
        mainViewModel.getAllStories().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        setStoriesData(result.data.listStory)
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

    private fun setStoriesData(stories: List<ListStoryItem>) {
        val linearLayout = LinearLayoutManager(this)
        binding.rvStories.apply {
            setHasFixedSize(true)
            layoutManager = linearLayout
        }
        val adapter = StoriesAdapter{
            val detailIntent = Intent(this, DetailStoryActivity::class.java)
            detailIntent.putExtra(DetailStoryActivity.EXTRA_STORY_ID, it.id)
            startActivity(detailIntent)
        }
        adapter.submitList(stories)
        binding.rvStories.adapter = adapter
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
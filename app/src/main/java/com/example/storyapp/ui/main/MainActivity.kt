package com.example.storyapp.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.ViewModelFactory
import com.example.storyapp.ui.adapter.LoadingStateAdapter
import com.example.storyapp.ui.adapter.StoriesAdapter
import com.example.storyapp.ui.mapstory.MapsActivity
import com.example.storyapp.ui.uploadstory.UploadActivity
import com.example.storyapp.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var userToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val welcomeIntent = Intent(this, WelcomeActivity::class.java)
                startActivity(welcomeIntent)
                finish()
            } else {
                binding.mainLayout.visibility = View.VISIBLE
                userToken = "Bearer ${user.token}"
                setStoriesData(userToken)
                binding.floatingActionButton.setOnClickListener {
                    uploadImage(userToken)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            R.id.menu_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra(MapsActivity.EXTRA_TOKEN, userToken)
                startActivity(intent)
            }

            R.id.menu_logout -> {
                mainViewModel.logout()
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun uploadImage(token: String) {
        val uploadIntent = Intent(this, UploadActivity::class.java)
        uploadIntent.putExtra(UploadActivity.EXTRA_TOKEN, token)
        startActivity(uploadIntent)

    }

    private fun setStoriesData(token: String) {
        val linearLayout = LinearLayoutManager(this)
        binding.rvStories.apply {
            setHasFixedSize(true)
            layoutManager = linearLayout
        }
        val adapter = StoriesAdapter(token)
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel.getAllStories(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
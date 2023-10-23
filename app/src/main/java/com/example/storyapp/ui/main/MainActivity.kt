package com.example.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.ViewModelFactory
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
            if (!user.isLogin){
                val welcomeIntent = Intent(this, WelcomeActivity::class.java)
                startActivity(welcomeIntent)
                finish()
            }
            binding.tvMain.text = user.name
        }

        binding.button.setOnClickListener {
            mainViewModel.logout()
        }
    }
}
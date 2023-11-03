package com.example.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.R
import com.example.storyapp.data.Result
import com.example.storyapp.data.local.pref.UserModel
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.ViewModelFactory
import com.example.storyapp.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setupView()

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvLoginWelcome, View.ALPHA, 1f).setDuration(100)
        val message = ObjectAnimator.ofFloat(binding.tvLoginDescription, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(100)
        val emailEdit = ObjectAnimator.ofFloat(binding.etlEmail, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(100)
        val passwordEdit = ObjectAnimator.ofFloat(binding.etlPassword, View.ALPHA, 1f).setDuration(100)
        val buttonLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(title, message, email, emailEdit, password, passwordEdit, buttonLogin)
            startDelay = 100
        }.start()
    }

    private fun loginUser() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        loginViewModel.loginUser(email, password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)

                    is Result.Success -> {
                        showLoading(false)
                        val user = result.data.loginResult
                        val userModel = UserModel(
                            user.name,
                            user.userId,
                            user.token
                        )
                        loginViewModel.saveSession(userModel)
                        showToast(result.data.message)
                        showDialog()
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }

                }
            }

        }
    }

    private fun showDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.dialog_login_title))
            setMessage(getString(R.string.dialog_login))
            setPositiveButton(getString(R.string.dialog_positive)) { _, _ ->
                val homeIntent = Intent(context, MainActivity::class.java)
                homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(homeIntent)
                finish()
            }
            create()
            show()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
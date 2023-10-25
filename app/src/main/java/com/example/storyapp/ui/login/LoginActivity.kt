package com.example.storyapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.data.Result
import com.example.storyapp.data.pref.UserModel
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

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
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
            setTitle("Yeah!")
            setMessage("Anda berhasil login. Sudah tidak sabar untuk belajar ya?")
            setPositiveButton("Lanjut") { _, _ ->
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
        binding.progressBar4.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
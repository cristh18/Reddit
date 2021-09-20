package com.tolodev.reddit.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tolodev.reddit.databinding.ActivityLoginBinding
import com.tolodev.reddit.ui.dialog.AuthorizationDialog
import com.tolodev.reddit.ui.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity :
    AppCompatActivity(),
    View.OnClickListener,
    AuthorizationDialog.RedditAuthorizationListener {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        subscribe()
    }

    private fun initListeners() {
        binding.buttonSignIn.setOnClickListener(this)
    }

    private fun subscribe() {
        with(loginViewModel) {
            hasSessionObserver().observe(this@LoginActivity, {})
        }
    }

    private fun redirectView() {
        startActivity(Intent(this, RedditActivity::class.java))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.buttonSignIn.id -> {
                Toast.makeText(this, "Click", Toast.LENGTH_LONG).show()
                AuthorizationDialog(this, this).show()
            }
        }
    }

    override fun onSuccessfulAuthorization() {
        redirectView()
    }
}

package com.tolodev.reddit.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tolodev.reddit.databinding.ActivityRedditBinding
import com.tolodev.reddit.ui.viewModel.RedditViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RedditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRedditBinding

    private val viewModel by viewModels<RedditViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRedditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribe()
    }

    private fun subscribe() {
        viewModel.accessTokenObserver().observe(this, {
            Timber.e("Token: ".plus(it))
        })
    }
}

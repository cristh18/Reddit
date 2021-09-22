package com.tolodev.reddit.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tolodev.reddit.databinding.ActivityRedditBinding
import com.tolodev.reddit.ui.adapter.RedditAdapter
import com.tolodev.reddit.ui.models.RedditPost
import com.tolodev.reddit.ui.viewModel.RedditViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RedditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRedditBinding

    private val viewModel by viewModels<RedditViewModel>()

    private val redditAdapter: RedditAdapter by lazy {
        RedditAdapter {
            showRecipeDetail(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRedditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        subscribe()
    }

    private fun initViews() {
        binding.srlRedditPosts.setOnRefreshListener { viewModel.getTop() }
        binding.recyclerViewRedditPosts.adapter = redditAdapter
    }

    private fun subscribe() {
        with(viewModel) {
            redditPostsObserver().observe(this@RedditActivity, { showRedditPosts(it) })
            updatingViewObserver().observe(
                this@RedditActivity,
                { binding.srlRedditPosts.isRefreshing = it }
            )
        }
    }

    private fun showRedditPosts(redditPosts: List<RedditPost>) {
        binding.recyclerViewRedditPosts.adapter = redditAdapter
        redditAdapter.setPosts(redditPosts)
    }

    private fun showRecipeDetail(redditPost: RedditPost) {
        Timber.d("Item selected")
        redditPost.visited = true
    }
}

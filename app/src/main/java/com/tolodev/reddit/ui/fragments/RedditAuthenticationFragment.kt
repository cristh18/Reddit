package com.tolodev.reddit.ui.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tolodev.reddit.databinding.FragmentRedditAuthenticationBinding
import com.tolodev.reddit.ui.activities.RedditActivity
import com.tolodev.reddit.ui.viewModel.RedditAuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RedditAuthenticationFragment : Fragment() {

    private var binding: FragmentRedditAuthenticationBinding? = null

    private val viewModel by viewModels<RedditAuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRedditAuthenticationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniViews()
        subscribe()
    }

    private fun iniViews() {
        binding?.webViewRedditAuthorization?.let { webView ->
            webView.settings.javaScriptEnabled = true
            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    url?.let { viewModel.onPageFinished(it) }
                }
            }
        }
    }

    private fun subscribe() {
        with(viewModel) {
            showAuthorizationScreenObserver().observe(viewLifecycleOwner, {
                binding?.webViewRedditAuthorization?.loadUrl(it)
            })
            authenticationStatusObserver().observe(
                viewLifecycleOwner,
                {
                    startActivity(
                        Intent(
                            requireActivity(), RedditActivity::class.java
                        )
                    )
                }
            )
        }
    }
}

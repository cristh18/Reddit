package com.tolodev.reddit.ui.fragments

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder
import androidx.datastore.rxjava2.RxDataStore
import androidx.fragment.app.Fragment
import com.tolodev.reddit.databinding.FragmentRedditAuthenticationBinding
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RedditAuthenticationFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var binding: FragmentRedditAuthenticationBinding? = null

    private val CLIENT_ID = "lR_u5noYNvjKAi31UPEQaA"
    private val CLIENT_SECRET = ""
    private val REDIRECT_URI = "https://localhost/authorize_callback"
    private val GRANT_TYPE = "https://oauth.reddit.com/grants/installed_client"
    private val GRANT_TYPE2 = "authorization_code"
    private val TOKEN_KEY = "access_token"
    private val OAUTH_URL = "https://www.reddit.com/api/v1/authorize"
    private val OAUTH_SCOPE = "read"
    private val DURATION = "permanent"

    val authorizationUrl =
        "$OAUTH_URL?client_id=$CLIENT_ID&response_type=token&state=aioros&redirect_uri=$REDIRECT_URI&scope=$OAUTH_SCOPE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRedditAuthenticationBinding.inflate(inflater, container, false)
        iniViews()
        return binding?.root
    }

    private fun iniViews() {
        binding?.webViewRedditAuthorization?.let { webView ->
            webView.settings.javaScriptEnabled = true

            webView.loadUrl(authorizationUrl)
            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    val formattedUrl = url?.replace("#", "?") ?: ""
                    super.onPageFinished(view, formattedUrl)
                    if (formattedUrl.isNotBlank() && URLUtil.isValidUrl(formattedUrl)) {
                        if (formattedUrl.contains(TOKEN_KEY)) {
                            val uri: Uri = Uri.parse(formattedUrl)
                            val authToken = uri.getQueryParameter(TOKEN_KEY)
                            Timber.e("Authorization token is: $authToken")

                            val dataStore: RxDataStore<Preferences> =
                                RxPreferenceDataStoreBuilder(
                                    requireActivity().applicationContext,
                                    "authentication"
                                ).build()

                            // redditAuthorizationListener.onSuccessfulAuthorization()
//
//                        val edit: SharedPreferences.Editor = pref.edit()
//                        edit.putString("Code", authCode)
//                        edit.commit()
                            // closeFragment
                        } else if (formattedUrl.contains("error=access_denied")) {
                            Timber.e("ACCESS_DENIED_HERE")
                            // closeFragment
                        }
                    } else {
                        // closeFragment
                    }

                    if (url?.equals("https://www.reddit.com/", true) == true) {
                        webView.loadUrl(authorizationUrl)
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RedditAuthenticationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
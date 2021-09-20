package com.tolodev.reddit.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.datastore.preferences.core.Preferences
import com.tolodev.reddit.databinding.DialogAuthorizationBinding
import timber.log.Timber
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder

import androidx.datastore.rxjava2.RxDataStore




class AuthorizationDialog(
    context: Context,
    val redditAuthorizationListener: RedditAuthorizationListener
) : Dialog(context) {

    private lateinit var binding: DialogAuthorizationBinding

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
        binding = DialogAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        iniViews()
    }

    private fun iniViews() {
        with(binding.webViewRedditAuthorization) {
            settings.javaScriptEnabled = true

            loadUrl(authorizationUrl)
            webViewClient = object : WebViewClient() {
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
                                RxPreferenceDataStoreBuilder(context,  "authentication").build()

                            redditAuthorizationListener.onSuccessfulAuthorization()
//
//                        val edit: SharedPreferences.Editor = pref.edit()
//                        edit.putString("Code", authCode)
//                        edit.commit()
                            dismiss()
                        } else if (formattedUrl.contains("error=access_denied")) {
                            Timber.e("ACCESS_DENIED_HERE")
                            dismiss()
                        }
                    } else {
                        dismiss()
                    }

                    if (url?.equals("https://www.reddit.com/", true) == true) {
                        loadUrl(authorizationUrl)
                    }
                }
            }
        }
    }

    interface RedditAuthorizationListener {
        fun onSuccessfulAuthorization()
    }
}

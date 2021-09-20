package com.tolodev.reddit.ui.viewModel

import android.net.Uri
import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolodev.reddit.BuildConfig
import com.tolodev.reddit.managers.RedditPrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val CLIENT_ID = BuildConfig.REDDIT_CLIENT_ID
private const val CLIENT_SECRET = BuildConfig.REDDIT_CLIENT_SECRET
private const val REDIRECT_URI = "https://localhost/authorize_callback"
private const val GRANT_TYPE = "https://oauth.reddit.com/grants/installed_client"
private const val GRANT_TYPE2 = "authorization_code"
private const val TOKEN_KEY = "access_token"
private const val OAUTH_URL = "https://www.reddit.com/api/v1/authorize"
private const val OAUTH_SCOPE = "read"
private const val DURATION = "permanent"

@HiltViewModel
class RedditAuthenticationViewModel @Inject constructor(private val redditPrefsManager: RedditPrefsManager) :
    ViewModel() {

    private val authorizationUrl =
        "$OAUTH_URL?client_id=$CLIENT_ID&response_type=token&state=aioros&redirect_uri=$REDIRECT_URI&scope=$OAUTH_SCOPE"

    private val showAuthorizationScreen = MutableLiveData<String>()

    private val successfulAuthentication = MutableLiveData<Unit>()

    init {
        setAuthorizationUrl()
    }

    private fun setAuthorizationUrl() {
        showAuthorizationScreen.postValue(authorizationUrl)
    }

    fun onPageFinished(url: String) {
        val formattedUrl = url.replace("#", "?")
        if (formattedUrl.isNotBlank() && URLUtil.isValidUrl(formattedUrl)) {
            if (formattedUrl.contains(TOKEN_KEY)) {
                val uri: Uri = Uri.parse(formattedUrl)
                val authToken = uri.getQueryParameter(TOKEN_KEY)
                Timber.e("Authorization token is: $authToken")

                authToken?.let {
                    saveToken(it)
                }
                // closeFragment
            } else if (formattedUrl.contains("error=access_denied")) {
                Timber.e("ACCESS_DENIED_HERE")
                // closeFragment
            }
        } else {
            // closeFragment
        }

        if (url.equals("https://www.reddit.com/", true)) {
            setAuthorizationUrl()
        }
    }

    private fun saveToken(token: String) {
        viewModelScope.launch {
            redditPrefsManager.saveAuthorizationToken(token)
            successfulAuthentication.value = Unit
        }
    }

    fun showAuthorizationScreenObserver(): LiveData<String> = showAuthorizationScreen

    fun authenticationStatusObserver(): LiveData<Unit> = successfulAuthentication
}

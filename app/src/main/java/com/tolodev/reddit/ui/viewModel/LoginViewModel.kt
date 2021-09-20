package com.tolodev.reddit.ui.viewModel

import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _hasSession = MutableLiveData<Boolean>()

    fun getAuthorization(): String {
        val apiKey = "lR_u5noYNvjKAi31UPEQaA"
        val apiSecret = ""
        val accessToken = "$apiKey:$apiSecret"
        val encodedString = Base64.encode(accessToken.toByteArray(), Base64.DEFAULT)
        Timber.e("Authorization: ".plus(String(encodedString)))
        return String(encodedString)
    }

    fun isLogged() {
        val isLogged = false
        _hasSession.value = isLogged
    }

    fun hasSessionObserver(): LiveData<Boolean> = _hasSession
}

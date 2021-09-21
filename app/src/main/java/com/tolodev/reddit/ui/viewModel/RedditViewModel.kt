package com.tolodev.reddit.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolodev.reddit.managers.RedditPrefsManager
import com.tolodev.reddit.network.api.RedditApi
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RedditViewModel @Inject constructor(
    private val redditPrefsManager: RedditPrefsManager,
    private val redditApi: RedditApi
) :
    ViewModel() {

    private val disposables = CompositeDisposable()

    private val accessToken = MutableLiveData<String>()

    init {
        getAuthorizationToken()
        getTop()
    }

    private fun getAuthorizationToken() {
        viewModelScope.launch {
            val token = redditPrefsManager.authorizationTokenFlow.first()
            accessToken.value = token
            // You should also handle IOExceptions here.
        }
    }

    private fun getTop() {
        disposables.add(
            redditApi.getTop()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ Timber.e("Response: ".plus(it)) }, {
                    Timber.e("Error", it)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun accessTokenObserver(): LiveData<String> = accessToken
}

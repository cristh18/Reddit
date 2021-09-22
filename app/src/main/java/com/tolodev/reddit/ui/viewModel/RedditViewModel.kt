package com.tolodev.reddit.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolodev.reddit.managers.RedditPrefsManager
import com.tolodev.reddit.network.api.RedditApi
import com.tolodev.reddit.network.models.RedditResponse
import com.tolodev.reddit.ui.models.RedditPost
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

    private val redditPosts = MutableLiveData<List<RedditPost>>()

    private val updatingView = MutableLiveData(false)

    private val postViewed = MutableLiveData(false)

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

    fun getTop() {
        disposables.add(
            redditApi.getTop()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { updatingView.postValue(true) }
                .doFinally { updatingView.postValue(false) }
                .subscribe(
                    { redditPosts.postValue(getRedditPosts(it)) },
                    {
                        Timber.e(it, "Error")
                    }
                )
        )
    }

    private fun getRedditPosts(redditResponse: RedditResponse): List<RedditPost> {
        return redditResponse.data.children.map {
            RedditPost(it.data)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun accessTokenObserver(): LiveData<String> = accessToken

    fun redditPostsObserver(): LiveData<List<RedditPost>> = redditPosts

    fun updatingViewObserver(): LiveData<Boolean> = updatingView

    fun postViewedObserver(): LiveData<Boolean> = postViewed
}

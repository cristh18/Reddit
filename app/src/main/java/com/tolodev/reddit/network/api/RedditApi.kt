package com.tolodev.reddit.network.api

import com.tolodev.reddit.network.RedditService
import com.tolodev.reddit.network.models.RedditResponse
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import javax.inject.Inject

class RedditApi @Inject constructor(
    private val services: RedditService
) : BaseApi() {

    @CheckReturnValue
    fun getTop(scheduler: Scheduler? = null): Single<RedditResponse> =
        subscribe(services.getTop(), scheduler)
}

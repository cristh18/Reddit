package com.tolodev.reddit.network

import com.tolodev.reddit.network.models.RedditResponse
import io.reactivex.Single
import retrofit2.http.GET

interface RedditService {
    @GET("top")
    fun getTop(): Single<RedditResponse>
}

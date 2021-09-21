package com.tolodev.reddit.network

import io.reactivex.Single
import retrofit2.http.GET

interface RedditService {
    @GET("top")
    fun getTop(): Single<Any>
}

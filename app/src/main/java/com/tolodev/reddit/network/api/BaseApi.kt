package com.tolodev.reddit.network.api

import android.os.Looper
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

abstract class BaseApi {

    protected fun <T> subscribe(single: Single<T>, scheduler: Scheduler? = null): Single<T> {
        return scheduler?.let { single.subscribeOn(scheduler) }
            ?: kotlin.run {
                if (Looper.myLooper() == Looper.getMainLooper()) single.subscribeOn(Schedulers.io())
                else single
            }
    }
}

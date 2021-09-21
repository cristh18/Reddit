package com.tolodev.reddit.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tolodev.reddit.BuildConfig
import com.tolodev.reddit.managers.RedditPrefsManager
import com.tolodev.reddit.network.RedditService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RedditModule {

    private const val TIME_OUT = 20

    @Provides
    @RedditBaseUrl
    fun getBaseUrl(): String {
        return "https://oauth.reddit.com/"
    }

    @Provides
    @Singleton
    @RedditServices
    fun retrofitAuthenticator(
        @RedditHttpClientBuilder
        builder: OkHttpClient.Builder,
        @RedditSerializer
        moshi: Moshi,
        @RedditBaseUrl
        baseUrl: String
    ): Retrofit {
        return getRetrofitBuilder(
            builder.build(),
            baseUrl,
            moshi
        ).build()
    }

    @Provides
    @Singleton
    @RedditHttpClientBuilder
    fun getAuthorizationHttpClientBuilder(
        @RedditInterceptors
        interceptor: Interceptor
    ): OkHttpClient.Builder {
        return getHttpClientBuilder(interceptor)
    }

    @Provides
    @Singleton
    @RedditInterceptors
    fun provideDynamicHeaderInterceptor(redditPrefsManager: RedditPrefsManager): Interceptor {
        return Interceptor { chain ->
            val headers = Headers.Builder()
            val newBuilder = chain.request()
                .newBuilder()
                .headers(headers.build())
                .method(chain.request().method, chain.request().body)

            newBuilder.header(
                "Authorization", "bearer " + redditPrefsManager.authorizationToken()
            )

            chain.proceed(newBuilder.build())
        }
    }

    @Provides
    @Singleton
    @RedditSerializer
    fun getMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRedditService(@RedditServices retrofit: Retrofit): RedditService =
        retrofit.create(RedditService::class.java)

    @Provides
    @Singleton
    fun provideRedditPrefsManager(@ApplicationContext appContext: Context): RedditPrefsManager =
        RedditPrefsManager(appContext)

    private fun getRetrofitBuilder(
        httpClient: OkHttpClient,
        url: String,
        moshi: Moshi
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
    }

    private fun getHttpClientBuilder(
        vararg interceptor: Interceptor
    ): OkHttpClient.Builder {

        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)

        interceptor.forEach { clientBuilder.addInterceptor(it) }

        if (BuildConfig.DEBUG) {

            val logging = HttpLoggingInterceptor()

            logging.level = HttpLoggingInterceptor.Level.BODY

            clientBuilder.addInterceptor(logging)
        }

        return clientBuilder
    }
}

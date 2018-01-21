package com.photos.kilopixels.network

import com.photos.kilopixels.network.interceptors.CookiesInterceptor
import com.photos.kilopixels.network.interceptors.LoggingInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created by rahul on 19/1/18.
 */

object OkHttpClientSingleton {
    val okHttpClient : OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor())
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    val okHttpClientSingletonInstance by lazy {
        OkHttpClientSingleton
    }


}
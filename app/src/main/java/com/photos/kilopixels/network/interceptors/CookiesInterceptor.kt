package com.photos.kilopixels.network.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by rahul on 19/1/18.
 */
class CookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}
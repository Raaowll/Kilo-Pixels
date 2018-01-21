package com.photos.kilopixels.network.interceptors

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import timber.log.Timber
import java.io.IOException

/**
 * Created by rahul on 19/1/18.
 */
class LoggingInterceptor : Interceptor {
    private val logTag: String = LoggingInterceptor::class.java.simpleName
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val t1 = System.nanoTime()

        Timber.d(request.method())

        request.body()?.let { Timber.d(bodyToString(request)) }

        Timber.d(String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()))

        val response = chain.proceed(request)

        val t2 = System.nanoTime()

        response?.let { Timber.d(String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6, response.headers())) }

        response?.let { Timber.d("Response: ", response.isSuccessful) }
        response?.let { Timber.d("Response: ", response.toString()) }

        return response
    }

    private fun bodyToString(request: Request): String {

        try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body()!!.writeTo(buffer)
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "did not work"
        }

    }
}
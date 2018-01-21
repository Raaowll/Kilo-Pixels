package com.photos.kilopixels.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import com.photos.kilopixels.network.OkHttpClientSingleton.okHttpClientSingletonInstance

/**
 * Created by rahul on 19/1/18.
 */
object RetrofitClientSingleton {
    private const val baseUrl : String = "https://api.flickr.com"

    val retrofitClient : Retrofit

    init {
        retrofitClient = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClientSingletonInstance.okHttpClient)
                .build()
    }

    val retrofitClientSingletonInstance by lazy {
        RetrofitClientSingleton
    }
}
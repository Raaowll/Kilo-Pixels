package com.photos.kilopixels.network

import com.photos.kilopixels.model.PhotosResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * Created by rahul on 19/1/18.
 */
interface ApiInterface {
    @GET("/services/rest/")
    fun searchPhotos(@QueryMap options: Map<String, String>) : Flowable<PhotosResponse>
}
package com.photos.kilopixels.model

import com.google.gson.annotations.SerializedName


/**
 * Created by rahul on 19/1/18.
 */
class PhotosResponse() {
    @SerializedName("photos") var photos: Photos = Photos()

    @SerializedName("stat") var stat: String = ""

    override fun toString(): String {
        return "ClassPojo [photos = $photos, stat = $stat]"
    }
}
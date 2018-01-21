package com.photos.kilopixels.model

import com.google.gson.annotations.SerializedName
import com.photos.kilopixels.model.entity.PhotosEntity

/**
 * Created by rahul on 19/1/18.
 */
class Photos() {
    @SerializedName("total") var total: String = ""

    @SerializedName("page") var page: String = ""

    @SerializedName("pages") var pages: String = ""

    @SerializedName("photo") var photo: ArrayList<PhotoDetail> = ArrayList()

    @SerializedName("perpage") var perpage: String = ""

    var id: Long = 0

    var searchText: String = ""

    constructor(total: String, page: String, pages: String, photo: ArrayList<PhotoDetail>, perpage: String, searchText: String
    , id: Long) : this() {
        this.total = total
        this.page = page
        this.pages = pages
        this.photo = photo
        this.perpage = perpage
        this.searchText = searchText
        this.id = id
    }


    override fun toString(): String {
        return "ClassPojo [total = $total, page = $page, pages = $pages, photo = $photo, perpage = $perpage, searchText = $searchText]"
    }

    fun createEntity(photos: Photos, dataDetail: String): PhotosEntity {
        return PhotosEntity(photos.total, photos.page, photos.pages, photos.perpage, photos.searchText, dataDetail)
    }
}
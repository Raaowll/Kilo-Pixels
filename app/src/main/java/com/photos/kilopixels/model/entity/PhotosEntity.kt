package com.photos.kilopixels.model.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.example.offline.model.ModelConstants
import com.google.gson.annotations.Expose
import com.photos.kilopixels.model.PhotoDetail
import com.photos.kilopixels.model.Photos

/**
 * Created by rahul on 20/1/18.
 */
@Entity(tableName = ModelConstants.LOCAL_TABLE_NAME)
data class PhotosEntity(@Expose @ColumnInfo(name = "total") var total: String
                        ,@Expose @ColumnInfo(name = "page") var page: String
                        ,@Expose @ColumnInfo(name = "pages") var pages: String
                        ,@Expose @ColumnInfo(name = "perpage") var perPage: String
                        ,@Expose @ColumnInfo(name = "searchtext") var searchText: String
                        ,@Expose @ColumnInfo(name = "datadetail") var dataDetail: String): Parcelable {

    @Expose
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
        id = parcel.readLong()
    }

    override fun toString(): String {
        return "ClassPojo [total = $total, page = $page, pages = $pages, perpage = $perPage, searchText = $searchText]"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(total)
        parcel.writeString(page)
        parcel.writeString(pages)
        parcel.writeString(perPage)
        parcel.writeString(searchText)
        parcel.writeString(dataDetail)
        parcel.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotosEntity> {
        override fun createFromParcel(parcel: Parcel): PhotosEntity {
            return PhotosEntity(parcel)
        }

        override fun newArray(size: Int): Array<PhotosEntity?> {
            return arrayOfNulls(size)
        }
    }

    fun createPhotos(photosEntity: PhotosEntity, dataList: ArrayList<PhotoDetail>): Photos {
       return Photos(photosEntity.total, photosEntity.page, photosEntity.pages, dataList, photosEntity.perPage
        , photosEntity.searchText, photosEntity.id)
    }
}
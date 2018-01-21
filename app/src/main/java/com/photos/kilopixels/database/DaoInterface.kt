package com.photos.kilopixels.database

import android.arch.persistence.room.*
import com.photos.kilopixels.model.entity.PhotosEntity
import io.reactivex.Flowable

/**
 * Created by rahul on 20/1/18.
 */
@Dao
interface DaoInterface {
    @Query("SELECT * FROM photos WHERE searchtext LIKE :searchText AND "
            + "page LIKE :pageNumber")
    fun getPhotos(searchText: String, pageNumber: String): Flowable<PhotosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photos: PhotosEntity)

    @Update fun update(photosEntity: PhotosEntity)
}
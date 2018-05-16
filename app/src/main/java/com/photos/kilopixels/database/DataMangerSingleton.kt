package com.photos.kilopixels.database

import android.arch.persistence.room.Dao
import android.content.Context
import com.photos.kilopixels.model.Photos
import com.photos.kilopixels.model.PhotosResponse
import com.photos.kilopixels.model.entity.PhotosEntity
import com.photos.kilopixels.network.ApiInterface
import com.photos.kilopixels.network.RetrofitClientSingleton.retrofitClientSingletonInstance
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber

/**
 * Created by rahul on 19/1/18.
 */
class DataMangerSingleton {
    private val context: Context
    private var apiInterface: ApiInterface
    private var daoInterface: DaoInterface

    companion object {
        @Volatile private var INSTANCE: DataMangerSingleton? = null

        fun getInstance(context: Context): DataMangerSingleton =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: DataMangerSingleton(context).also { INSTANCE = it }
                }
    }

    private constructor(context: Context) {
        this.context = context
        apiInterface = retrofitClientSingletonInstance.retrofitClient.create(ApiInterface::class.java)
        daoInterface = LocalDatabase.getInstance(context).getDaoInterface()
    }

    fun searchPhotos(searchTag: String, pageNumber: String): Flowable<PhotosResponse> {
        val optionsMap = LinkedHashMap<String, String>()
        optionsMap["method"] = "flickr.photos.search"
        optionsMap["api_key"] = "8aec588dcb8a3f07b58d67a39ce395ad"
        optionsMap["content_type"] = "1"
        optionsMap["media"] = "photos"
        optionsMap["extras"] = "url_o,url_c,url_z"
        optionsMap["per_page"] = "50"
        optionsMap["page"] = pageNumber
        optionsMap["format"] = "json"
        optionsMap["text"] = searchTag
        optionsMap["format"] = "json"
        optionsMap["nojsoncallback"] = "1"
        return apiInterface.searchPhotos(optionsMap)
    }

    fun searchPhotosLocally(searchTag: String, pageNumber: String): Flowable<PhotosEntity> {
        return daoInterface.getPhotos(searchTag, pageNumber)
    }

    fun syncDataLocally(photos: PhotosEntity): Completable {
        Timber.d("Syncing data for %s", photos.page)

        return Completable.fromCallable {
            daoInterface.insert(photos)
            Timber.d("Data synced locally" + photos.page)
        }
    }

    fun updateDataLocally(photos: PhotosEntity): Completable {
        Timber.d("Updating data for %s", photos.page)

        return Completable.fromCallable {
            daoInterface.update(photos)
            Timber.d("Data updated locally for %s", photos.page)
        }
    }
}
package com.photos.kilopixels.view.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.photos.kilopixels.App
import com.photos.kilopixels.database.DataMangerSingleton
import com.photos.kilopixels.model.PhotoDetail
import com.photos.kilopixels.model.Photos
import com.photos.kilopixels.model.entity.PhotosEntity
import com.photos.kilopixels.network.NetworkUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import timber.log.Timber
import java.util.*

/**
 * Created by rahul on 19/1/18.
 */
class SearchPhotosViewModel : ViewModel() {
    private var disposable: CompositeDisposable = CompositeDisposable()
    private var liveData: MutableLiveData<List<PhotoDetail>> = MutableLiveData()
    private var dataMangerSingleton = DataMangerSingleton.getInstance(App.INSTANCE)
    private val gson: Gson = Gson()
    private val map: HashMap<String, Photos> = HashMap()

    init {
        //getPhotos("tajmahal", "1")
    }

    fun getPhotos(searchTag: String, pageNumber: String) {
        if(NetworkUtil.isOnline(App.INSTANCE)) {
            searchPhotos(searchTag, pageNumber)
        } else {
            searchPhotosLocally(searchTag, pageNumber)
        }
    }

    override fun onCleared() {
        disposable.clear()
        clearData()
    }

    fun getLiveData(): MutableLiveData<List<PhotoDetail>> {
        return liveData
    }

    private fun checkData(photos: PhotosEntity, searchTag: String, pageNumber: String) {
        if(photos.dataDetail == null) {
            Timber.d("Locally data not available, fetching from remote")
            if(NetworkUtil.isOnline(App.INSTANCE)) {
                searchPhotos(searchTag, pageNumber)
            }
        } else {
            val photos = convertDataToJson(photos)
            map[photos.page] = photos
            liveData.value = photos.photo
        }
    }

    private fun syncDataLocally(photos: Photos, searchText: String) {
        if (!photos.photo.isEmpty()) {

            photos.searchText = searchText

            appendPageNumber(photos)

            map[photos.page] = photos

            liveData.value = photos.photo

            val dataDetail = gson.toJson(photos.photo)

            val photosEntity = photos.createEntity(photos, dataDetail)

            disposable.add(dataMangerSingleton.syncDataLocally(photosEntity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ Timber.d("Data synced successful") },
                            { t -> Timber.d("Data sync error: $t") }))
        }
    }

    fun updateDataLocally(photoDetailList: List<PhotoDetail>, position: Int) {
        if (!photoDetailList.isEmpty()) {
            val keyValue = photoDetailList[position].pageNumber
            if (!map.containsKey(keyValue)) return

            val photos = map[keyValue]

            val dataDetail = gson.toJson(photoDetailList)

            val photosEntity = photos?.createEntity(photos, dataDetail)

            disposable.add(dataMangerSingleton.updateDataLocally(photosEntity!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ Timber.d("Data updated successfully") },
                            { t -> Timber.d("Data update error, $t") }))
        }
    }

    private fun searchPhotos(searchTag: String, pageNumber: String) {
        Timber.d("Fetching images for searchText: $searchTag and pageNumber: $pageNumber")
        disposable.add(dataMangerSingleton.searchPhotos(searchTag, pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ item -> syncDataLocally(item.photos, searchText = searchTag) }
                        , { t -> Timber.e(t, "Search photos error") }))
    }

    private fun searchPhotosLocally(searchTag: String, pageNumber: String) {
        Timber.d("Fetching images for searchText: $searchTag and pageNumber: $pageNumber locally")
        disposable.add(dataMangerSingleton.searchPhotosLocally(searchTag, pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t -> checkData(t as PhotosEntity, searchTag, pageNumber) }
                        , { t -> Timber.e(t, "Search photos locally error") }))
    }

    private fun convertDataToJson(photosEntity: PhotosEntity): Photos {
        val dataValue = photosEntity.dataDetail
        val jsonArray = JSONArray(dataValue)
        val listType = object : TypeToken<List<PhotoDetail>>() {}.type
        val dataList: ArrayList<PhotoDetail> = gson.fromJson(jsonArray.toString(), listType)

        return photosEntity.createPhotos(photosEntity, dataList)
    }

    private fun appendPageNumber(photos: Photos) {
        for(photoDetail in photos.photo) {
            photoDetail.pageNumber = photos.page
            photoDetail.itemId = UUID.randomUUID().mostSignificantBits
        }
    }

    fun clearData() {
        map.clear()
        //liveData.value = null
    }
}
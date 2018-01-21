package com.photos.kilopixels.view

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.photos.kilopixels.database.DataMangerSingleton
import com.photos.kilopixels.view.search.SearchPhotosViewModel

/**
 * Created by rahul on 19/1/18.
 */
class ViewModelFactory(): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchPhotosViewModel::class.java)) {
            return SearchPhotosViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
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
            val key = "SearchPhotosViewModel"

            return if(hashMapViewModel.containsKey(key)){
                getViewModel(key) as T
            } else {
                addViewModel(key, SearchPhotosViewModel())
                getViewModel(key) as T
            }
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        private val hashMapViewModel = HashMap<String, ViewModel>()
        fun addViewModel(key: String, viewModel: ViewModel){
            hashMapViewModel[key] = viewModel
        }

        fun getViewModel(key: String): ViewModel? {
            return hashMapViewModel[key]
        }
    }
}
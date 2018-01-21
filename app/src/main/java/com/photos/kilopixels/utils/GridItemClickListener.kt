package com.photos.kilopixels.utils

import android.view.View
import android.widget.ImageView
import com.photos.kilopixels.model.PhotoDetail

/**
 * Created by rahul on 21/1/18.
 */
interface GridItemClickListener {
    fun onItemClick(photoDetail: PhotoDetail, view: View)
}
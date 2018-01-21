package com.photos.kilopixels.utils

import android.widget.ImageView
import com.photos.kilopixels.model.PhotoDetail

/**
 * Created by rahul on 21/1/18.
 */
interface GridItemClickListener {
    fun onItemClick(position: Int, photoDetail: PhotoDetail, imageView: ImageView, photoDetailList: List<PhotoDetail>)
}
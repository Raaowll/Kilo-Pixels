package com.photos.kilopixels.model.events

import com.photos.kilopixels.model.PhotoDetail

/**
 * Created by rahul on 20/1/18.
 */
data class UpdateDataEvent(val photoDetailList: List<PhotoDetail>, val position: Int) {
}
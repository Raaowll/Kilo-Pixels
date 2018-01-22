package com.photos.kilopixels.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.photos.kilopixels.model.PhotoDetail

/**
 * Created by rahul on 20/1/18.
 */
class Utility {
    companion object {
        fun drawableToBitmap(drawable: Drawable): Bitmap {
            if (drawable is BitmapDrawable) {
                if (drawable.bitmap != null) {
                    return drawable.bitmap
                }
            }

            val bitmap: Bitmap

            if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }

        fun getUrl(photoDetail: PhotoDetail): String {
            return photoDetail.localUri ?: photoDetail.url_sq ?: photoDetail.url_t ?: photoDetail.url_q ?: photoDetail.url_s ?:
            photoDetail.url_n ?: photoDetail.url_m ?: photoDetail.url_z ?: photoDetail.url_c ?:
            photoDetail.url_l ?: photoDetail.url_o ?: ""
        }

        fun getUrlFullImage(photoDetail: PhotoDetail): String {
            return photoDetail.localUri ?: photoDetail.url_l ?: photoDetail.url_c ?: photoDetail.url_z ?: photoDetail.url_m ?:
            photoDetail.url_o ?: photoDetail.url_n ?: photoDetail.url_s ?: photoDetail.url_t ?:
            photoDetail.url_s ?: photoDetail.url_sq ?: ""
        }
    }
}
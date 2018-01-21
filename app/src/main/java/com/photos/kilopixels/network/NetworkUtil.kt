package com.photos.kilopixels.network

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by rahul on 20/1/18.
 */
class NetworkUtil {
    companion object {
        fun isOnline(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return !(netInfo == null || !netInfo.isConnectedOrConnecting)
        }
    }
}
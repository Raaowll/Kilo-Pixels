package com.photos.kilopixels

import android.app.Application
import android.content.Context
import com.bumptech.glide.request.target.ViewTarget
import timber.log.Timber

/**
 * Created by rahul on 19/1/18.
 */
class App: Application() {
    companion object {
        lateinit var INSTANCE: Context
    }

    override fun onCreate() {
        super.onCreate()

        ViewTarget.setTagId(R.id.glide_tag);

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        INSTANCE = this
    }
}
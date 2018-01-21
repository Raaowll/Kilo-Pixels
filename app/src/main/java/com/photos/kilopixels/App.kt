package com.photos.kilopixels

import android.app.Application
import android.content.Context
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

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        INSTANCE = this
    }
}
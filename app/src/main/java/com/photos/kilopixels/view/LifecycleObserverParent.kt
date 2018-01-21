package com.photos.kilopixels.view

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import timber.log.Timber

/**
 * Created by rahul on 19/1/18.
 */
open class LifecycleObserverParent : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart () {
        Timber.d("onStart lifecycle event")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume () {
        Timber.d("OnResume lifecycle event")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause () {
        Timber.d("OnPause lifecycle event")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop () {
        Timber.d("onStop lifecycle event")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy () {
        Timber.d("onDestroy lifecycle event")
    }
}
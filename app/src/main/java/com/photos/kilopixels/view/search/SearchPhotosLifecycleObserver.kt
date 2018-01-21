package com.photos.kilopixels.view.search

import com.photos.kilopixels.view.LifecycleObserverParent
import org.greenrobot.eventbus.EventBus

/**
 * Created by rahul on 19/1/18.
 */
class SearchPhotosLifecycleObserver : LifecycleObserverParent() {
    override fun onStart() {
        super.onStart()
        //EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        //EventBus.getDefault().unregister(this)
    }
}
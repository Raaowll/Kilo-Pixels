package com.photos.kilopixels.view.detail

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.photos.kilopixels.R
import com.photos.kilopixels.model.PhotoDetail
import com.photos.kilopixels.model.events.LoadMoreDataEvent
import com.photos.kilopixels.model.events.NewDataAvailableEvent
import com.photos.kilopixels.view.MainActivity
import com.photos.kilopixels.view.MainActivity.Companion.EXTRA_CURRENT_ALBUM_POSITION
import com.photos.kilopixels.view.MainActivity.Companion.EXTRA_STARTING_ALBUM_POSITION
import com.photos.kilopixels.view.ViewModelFactory
import com.photos.kilopixels.view.search.SearchPhotosLifecycleObserver
import com.photos.kilopixels.view.search.SearchPhotosViewModel
import kotlinx.android.synthetic.main.fragment_view_pager.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

/**
 * Created by rahul on 21/1/18.
 */
class PhotoDetailActivity: AppCompatActivity(), LifecycleOwner {
    companion object {
        const val ITEM_ID = "itemId"
        const val ITEM_INDEX = "itemIndex"

        private const val SAVED_CURRENT_PAGE_POSITION = "current_page_position"
    }

    private lateinit var searchPhotosViewModel: SearchPhotosViewModel
    private val photosLifecycleObserver by lazy { SearchPhotosLifecycleObserver() }
    private val viewModelFactory by lazy { ViewModelFactory() }

    private var dataList: ArrayList<PhotoDetail> = MainActivity.dataList
    private var isReturning: Boolean = false
    private var startingPosition: Int = 0
    private var currentPosition: Int = 0
    private var imagePagerAdapter: ImagePagerAdapter? = null

    private val enterElementCallback: SharedElementCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
            if (isReturning) {
                val sharedElement = imagePagerAdapter?.getView(currentPosition)

                if (startingPosition != currentPosition) {
                    names.clear()
                    names.add(ViewCompat.getTransitionName(sharedElement))

                    sharedElements.clear()
                    sharedElements.put(ViewCompat.getTransitionName(sharedElement), sharedElement!!)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_view_pager)

        lifecycle.addObserver(photosLifecycleObserver)

        //searchPhotosViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchPhotosViewModel::class.java)

        ActivityCompat.postponeEnterTransition(this)
        ActivityCompat.setEnterSharedElementCallback(this, enterElementCallback)

        startingPosition = intent.getIntExtra(ITEM_INDEX, 0)
        currentPosition = savedInstanceState?.getInt(SAVED_CURRENT_PAGE_POSITION) ?: startingPosition

        imagePagerAdapter = ImagePagerAdapter(this, dataList, currentPosition)
        viewPager.adapter = imagePagerAdapter
        viewPager.currentItem = currentPosition

        //searchPhotosViewModel.getLiveData().observe(this, Observer { t -> updateData(t as ArrayList<PhotoDetail>) })

        MainActivity.liveData.observe(this, Observer { t -> updateData(t as ArrayList<PhotoDetail>) })

        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                currentPosition = position

                if (MainActivity.dataList.size - currentPosition == 5) {
                    EventBus.getDefault().postSticky(LoadMoreDataEvent())
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(SAVED_CURRENT_PAGE_POSITION, currentPosition)
    }

    override fun finishAfterTransition() {
        isReturning = true
        val data = Intent()
        data.putExtra(EXTRA_STARTING_ALBUM_POSITION, startingPosition)
        data.putExtra(EXTRA_CURRENT_ALBUM_POSITION, currentPosition)
        setResult(Activity.RESULT_OK, data)
        super.finishAfterTransition()
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
        super.onBackPressed()
    }

    private fun updateData(photoDetailList: ArrayList<PhotoDetail>) {
        dataList.addAll(photoDetailList)
        imagePagerAdapter?.notifyDataSetChanged()
    }
}
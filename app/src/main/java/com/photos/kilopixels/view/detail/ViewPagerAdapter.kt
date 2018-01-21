package com.photos.kilopixels.view.detail

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.photos.kilopixels.model.PhotoDetail


/**
 * Created by rahul on 21/1/18.
 */
class ViewPagerAdapter(fragmentManager: FragmentManager, private val photoDetailList: List<PhotoDetail>) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        val photoDetail = photoDetailList[position]
        return DetailedViewFragment.newInstance(photoDetail = photoDetail, transitionName = photoDetail.id!!)
    }

    override fun getCount(): Int {
        return photoDetailList.size
    }
}
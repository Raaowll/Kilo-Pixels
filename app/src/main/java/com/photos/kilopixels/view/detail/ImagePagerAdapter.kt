package com.photos.kilopixels.view.detail

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startPostponedEnterTransition
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewCompat
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.photos.kilopixels.model.PhotoDetail
import com.photos.kilopixels.utils.GlideApp
import com.photos.kilopixels.utils.Utility
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.github.chrisbanes.photoview.PhotoView
import com.photos.kilopixels.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail_view.*


/**
 * Created by rahul on 21/1/18.
 */
class ImagePagerAdapter (private val context: Context, private val items: List<PhotoDetail>,
        private val currentPos: Int) : PagerAdapter() {

    private var views = HashMap<Int, View>()
    //private var views = SparseArray<View?>(items.size)

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val item = items[position]

        val imageView = ImageView(collection.context)
        ViewCompat.setTransitionName(imageView, item.id)
        views.put(position, imageView)

        Picasso.with(collection.context)
                .load(Utility.getUrlFullImage(item))
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        if (position == currentPos) {
                            imageView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                                override fun onPreDraw(): Boolean {
                                    imageView.viewTreeObserver.removeOnPreDrawListener(this)
                                    ActivityCompat.startPostponedEnterTransition(context as Activity)
                                    return true
                                }
                            })
                        }
                    }

                    override fun onError() {
                        ActivityCompat.startPostponedEnterTransition(context as Activity)
                    }
                })

        collection.addView(imageView)
        return imageView

    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        views.remove(position)
        collection.removeView(view as View)
    }

    override fun getCount() = items.size
    fun getView(position: Int): View? = views.get(position)
}

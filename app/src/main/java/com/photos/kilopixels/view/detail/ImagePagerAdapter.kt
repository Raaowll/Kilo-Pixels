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
import com.github.chrisbanes.photoview.PhotoView
import com.photos.kilopixels.R
import kotlinx.android.synthetic.main.fragment_detail_view.*


/**
 * Created by rahul on 21/1/18.
 */
class ImagePagerAdapter (private val context: Context, private val items: List<PhotoDetail>,
        private val currentPos: Int, private val transitionName: String) : PagerAdapter() {

    private var inflator: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val views = SparseArray<View?>(items.size)

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val item = items[position]

        val itemView = inflator.inflate(R.layout.fragment_detail_view, collection, false)
        val imageView = itemView.findViewById(R.id.detailIv) as PhotoView
        //ViewCompat.setTransitionName(imageView, item.id)
        views.put(position, imageView)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.transitionName = transitionName;
        }

        val baseTarget = object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                imageView.setImageDrawable(resource)
            }
        }

        GlideApp.with(context)
                .load(Utility.getUrl(item))
                .dontAnimate()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        startPostponedEnterTransition(context as Activity)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        if (position == currentPos) {
                            imageView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                                override fun onPreDraw(): Boolean {
                                    imageView.viewTreeObserver.removeOnPreDrawListener(this)
                                    ActivityCompat.startPostponedEnterTransition(context as Activity)
                                    return true
                                }
                            })
                        }
                        return false
                    }
                })
                .into(baseTarget)

        collection.addView(imageView)
        return imageView
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        views.removeAt(position)
        collection.removeView(view as View)
    }

    override fun getCount() = items.size
    fun getView(position: Int): View? = views.get(position)
}

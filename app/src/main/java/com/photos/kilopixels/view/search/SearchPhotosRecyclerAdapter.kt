package com.photos.kilopixels.view.search

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.photos.kilopixels.model.PhotoDetail
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.photos.kilopixels.R
import com.photos.kilopixels.model.events.UpdateDataEvent
import com.photos.kilopixels.utils.GlideApp
import com.photos.kilopixels.utils.GridItemClickListener
import com.photos.kilopixels.utils.LoaderAdapter
import com.photos.kilopixels.utils.Utility
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.collections.ArrayList


/**
 * Created by rahul on 20/1/18.
 */
class SearchPhotosRecyclerAdapter(photoDetail: ArrayList<PhotoDetail>, private val context: Context
                                  , private val gridItemClickListener: GridItemClickListener): LoaderAdapter<PhotoDetail, Context>(photosList = photoDetail, context = context) {

    init {
        //setHasStableIds(true)
    }

    override fun getItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.grid_view_item_layout, parent, false))
    }

    override fun bindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is ItemViewHolder) return

        val photoDetail = photosList.get(index = position)
        //holder.previewIv.setImageDrawable(null)
        //holder.progressBar.visibility = View.VISIBLE

        val url = Utility.getUrl(photoDetail)

        Log.d("position $position:", url + " ");

        holder.previewIv.tag = photoDetail.id
        ViewCompat.setTransitionName(holder.previewIv, photoDetail.id);

        holder.previewIv.setOnClickListener({ gridItemClickListener.onItemClick(photoDetail, it) })

        holder.previewIv.layoutParams.height = context.resources.getDimension(R.dimen.image_height).toInt()

        //try {

            if (photoDetail.isSavedLocally && photoDetail.localUri != null) {
                holder.previewIv.scaleType = ImageView.ScaleType.CENTER_CROP
                GlideApp.with(context)
                        .load(photoDetail.localUri)
                        .centerCrop()
                        .into(holder.previewIv)
            } else {
                val baseTarget = object : SimpleTarget<Drawable>() {

                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        holder.previewIv.scaleType = ImageView.ScaleType.CENTER_CROP
                        holder.previewIv.setImageDrawable(resource)
                        if (photoDetail.localUri == null) {
                            photoDetail.localUrl = saveBitmapToFile(Utility.drawableToBitmap(resource), photoDetail.id + ".jpg")
                            val path = context.filesDir.absolutePath
                            val file = File(path + "/" + photoDetail.localUrl)
                            photoDetail.localUri = Uri.fromFile(file).toString()
                            photoDetail.isSavedLocally = true
                            EventBus.getDefault().postSticky(UpdateDataEvent(photoDetailList = photosList, position = position))
                        }
                    }
                }

                GlideApp.with(context)
                        .load(url)
                        .centerCrop()
                        .placeholder(R.drawable.abc_cab_background_top_material)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .thumbnail(0.3f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(baseTarget)
            }
        /*} catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }*/
    }

    class ItemViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {
        val previewIv: ImageView = containerView.findViewById<ImageView>(R.id.previewIv)
        val progressBar: ProgressBar = containerView.findViewById<ProgressBar>(R.id.progressBar)
    }

    fun updateDataList(newData: List<PhotoDetail>) {
        Timber.d("Got new data " + newData.size)
        //this.photosList.clear()
        this.photosList.addAll(newData)
        Timber.d("Updated data " + photosList.size)
        //Collections.reverse(this.photosList)
        //showLoading(false)
        notifyDataSetChanged()
    }

    private fun saveBitmapToFile(bitmap: Bitmap, filename: String): String {
        var out: FileOutputStream? = null
        try {
            out = context.openFileOutput(filename, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return filename
    }
}
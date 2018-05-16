package com.photos.kilopixels.utils

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.photos.kilopixels.R

/**
 * Created by rahul on 20/1/18.
 */
abstract class LoaderAdapter<T, U>(var photosList: ArrayList<T>, private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEWTYPE_ITEM = 1
    private val VIEWTYPE_LOADER = 2
    protected var showLoader: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEWTYPE_ITEM)
            return getItemViewHolder(parent)
        else return LoaderViewHolder(LayoutInflater.from(context).inflate(R.layout.loader_view_layout, parent, false))
    }

    override fun getItemCount(): Int {
        if (photosList.isEmpty()) return 0

        return photosList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position != 0 && position == itemCount - 1) {
            VIEWTYPE_LOADER
        } else VIEWTYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LoaderViewHolder) {
            if (showLoader) {
                holder.progressBar.visibility = View.VISIBLE;
            } else {
                holder.progressBar.visibility = View.GONE;
            }
            return
        }

        bindItemViewHolder(holder, position);
    }

    class LoaderViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {
        val progressBar: ProgressBar = containerView.findViewById<ProgressBar>(R.id.progressBar)
    }

    fun showLoading(status: Boolean) {
        showLoader = status
    }

    abstract fun getItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun bindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int)
}
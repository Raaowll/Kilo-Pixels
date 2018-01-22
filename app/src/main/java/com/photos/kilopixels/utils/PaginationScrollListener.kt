package com.photos.kilopixels.utils

/**
 * Created by rahul on 20/1/18.
 */

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import timber.log.Timber
import java.util.*

/**
 * Created by rahul on 20/1/18.
 */
abstract class PaginationScrollListener(): RecyclerView.OnScrollListener() {

     override fun onScrolled(recyclerView: RecyclerView , dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

         val gridLayoutManager: GridLayoutManager = layoutManager()

         var visibleItemCount = gridLayoutManager.childCount
         var totalItemCount = gridLayoutManager.itemCount
         //var firstVisibleItemPosition = gridLayoutManager.findFirstCompletelyVisibleItemPositions(null)
         var firstVisibleItemPosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition()

         //var index = firstVisibleItemPosition[0]

         Timber.d("Visible item count $visibleItemCount")
         Timber.d("Total item count $totalItemCount")
         //Timber.d("First visible item position $index")
         Timber.d("First visible item position $firstVisibleItemPosition")

         if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition/*[0]*/) >= totalItemCount
                    && firstVisibleItemPosition/*[0]*/ >= 0
                    && totalItemCount >= getTotalPageCount()) {
                loadMoreItems()
            }
        }

    }

    protected abstract fun loadMoreItems()

    abstract fun getTotalPageCount(): Int

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean

    abstract fun layoutManager(): /*Staggered*/GridLayoutManager

}
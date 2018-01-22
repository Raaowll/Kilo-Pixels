package com.photos.kilopixels.view

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.ViewCompat
import android.support.v7.widget.*
import android.view.*
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.photos.kilopixels.R
import com.photos.kilopixels.model.PhotoDetail
import com.photos.kilopixels.model.events.LoadMoreDataEvent
import com.photos.kilopixels.model.events.NewDataAvailableEvent
import com.photos.kilopixels.model.events.UpdateDataEvent
import com.photos.kilopixels.utils.GridItemClickListener
import com.photos.kilopixels.utils.GridItemDecorator
import com.photos.kilopixels.utils.PaginationScrollListener
import com.photos.kilopixels.view.detail.PhotoDetailActivity
import com.photos.kilopixels.view.search.SearchPhotosLifecycleObserver
import com.photos.kilopixels.view.search.SearchPhotosRecyclerAdapter
import com.photos.kilopixels.view.search.SearchPhotosViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import android.support.v7.view.menu.MenuBuilder
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Method


class MainActivity : AppCompatActivity(), LifecycleOwner, GridItemClickListener {

    private lateinit var searchPhotosViewModel: SearchPhotosViewModel
    private val photosLifecycleObserver by lazy { SearchPhotosLifecycleObserver() }
    private val viewModelFactory by lazy { ViewModelFactory() }

    private lateinit var adapter: SearchPhotosRecyclerAdapter

    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    private val PAGE_START = 1
    private var isLoading = false
    private var isLastPage = false
    private val TOTAL_PAGES = 5
    private var currentPage = PAGE_START
    private var searchQuery: String = ""

    private val MOVE_DEFAULT_TIME: Long = 1000
    private val SLIDE_DEFAULT_TIME: Long = 600
    private val FADE_DEFAULT_TIME: Long = 300

    private var currentGridSpanCount = 1;
    private var layoutManager: /*Staggered*/GridLayoutManager? = null

    private var searchView: SearchView? = null

    private var reenterState: Bundle? = null

    companion object {
        var dataList: ArrayList<PhotoDetail> = ArrayList()

        var liveData: MutableLiveData<List<PhotoDetail>> = MutableLiveData()

        const val EXTRA_STARTING_ALBUM_POSITION = "extra_starting_item_position"
        const val EXTRA_CURRENT_ALBUM_POSITION = "extra_current_item_position"
    }

    private val exitElementCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
            if (reenterState != null) {
                val startingPosition = reenterState!!.getInt(EXTRA_STARTING_ALBUM_POSITION)
                val currentPosition = reenterState!!.getInt(EXTRA_CURRENT_ALBUM_POSITION)
                if (startingPosition != currentPosition) {
                    // Current element has changed, need to override previous exit transitions
                    val newTransitionName = dataList?.get(currentPosition)?.id
                    val newSharedElement = recyclerView.findViewWithTag<ImageView>(newTransitionName)
                    if (newSharedElement != null) {
                        names.clear()
                        names.add(newTransitionName!!)

                        sharedElements.clear()
                        sharedElements.put(newTransitionName, newSharedElement)
                    }
                }
                reenterState = null
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)

        lifecycle.addObserver(photosLifecycleObserver)

        ActivityCompat.setExitSharedElementCallback(this, exitElementCallback)

        searchPhotosViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchPhotosViewModel::class.java)

        adapter = SearchPhotosRecyclerAdapter(photoDetail = ArrayList<PhotoDetail>(), context = this, gridItemClickListener = this)

        searchPhotosViewModel.getLiveData().observe(this, Observer { t -> updateData(t as ArrayList<PhotoDetail>) })

        searchPhotosViewModel.getLiveData().observe(this, Observer { t -> adapter.updateDataList(t as List<PhotoDetail>) })

        initRecyclerView()

        EventBus.getDefault().register(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_fragment_menu, menu)

        menu.add(0, R.string.action_grid_span_1, 1, menuIconWithText(getResources().getDrawable(R.drawable.single_column), "Comfortable View"))
        menu.add(0, R.string.action_grid_span_2, 2, menuIconWithText(getResources().getDrawable(R.drawable.double_column), "2 * 2"))
        menu.add(0, R.string.action_grid_span_3, 3, menuIconWithText(getResources().getDrawable(R.drawable.triple_column), "3 * 3"))

        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView!!.maxWidth = Integer.MAX_VALUE

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchQuery = query

                resetData()

                searchPhotosViewModel.getPhotos(query, currentPage.toString())

                //searchView!!.setQuery("", false)
                searchView!!.clearFocus()
                //searchView!!.isIconified = true

                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun menuIconWithText(r: Drawable, title: String): CharSequence {
        r.setBounds(0, 0, r.intrinsicWidth, r.intrinsicHeight);
        val sb = SpannableString("    " + title);
        val imageSpan = ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    private fun resetData() {
        recyclerView.visibility = View.VISIBLE
        infoTv.visibility = View.GONE
        adapter.photosList.clear()
        adapter.notifyDataSetChanged()
        currentPage = PAGE_START
        isLoading = false
        isLastPage = false
        dataList.clear()
        searchPhotosViewModel.clearData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_search) {
            true
        } else if (id == R.string.action_grid_span_1) {
            layoutManager = setLayoutManager(1)
            //adapter.notifyDataSetChanged()
            true
        } else if (id == R.string.action_grid_span_2) {
            layoutManager = setLayoutManager(2)
            //adapter.notifyDataSetChanged()
            true
        } else if (id == R.string.action_grid_span_3) {
            layoutManager = setLayoutManager(3)
            //adapter.notifyDataSetChanged()
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        if (adapter != null && adapter.photosList != null && !adapter.photosList.isEmpty()) {
            recyclerView.visibility = View.VISIBLE
            infoTv.visibility = View.GONE
        } else {
            recyclerView.visibility = View.GONE
            infoTv.visibility = View.VISIBLE
        }
    }

    private fun initRecyclerView() {
        //val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        //recyclerView.layoutManager = manager
        recyclerView.setHasFixedSize(true)
        val itemAnimator = DefaultItemAnimator()
        //itemAnimator.addDuration = 1000
        recyclerView.itemAnimator = itemAnimator


        layoutManager = setLayoutManager(currentGridSpanCount)
        recyclerView.setHasFixedSize(true)

        val itemDecoration = GridItemDecorator(this, R.dimen.grid_spacing3dp)
        recyclerView.addItemDecoration(itemDecoration)

        recyclerView.isNestedScrollingEnabled = false

        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object: PaginationScrollListener() {
            override fun layoutManager(): /*Staggered*/GridLayoutManager {
                return layoutManager as /*Staggered*/GridLayoutManager
            }

            override fun loadMoreItems() {
                onEvent(LoadMoreDataEvent())
            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                Timber.d("Is Loading: $isLoading")
                return isLoading
            }

        })
    }

    private fun setLayoutManager(spanCount: Int): /*Staggered*/GridLayoutManager {
        currentGridSpanCount = spanCount
        val layoutManager = GridLayoutManager(this, spanCount, GridLayoutManager.VERTICAL
                , false)
        //val layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        //layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recyclerView.layoutManager = layoutManager
        isLoading = false
        return layoutManager
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(updateDataEvent: UpdateDataEvent) {
        EventBus.getDefault().removeStickyEvent(updateDataEvent)

        if (updateDataEvent.photoDetailList != null) {
            searchPhotosViewModel.updateDataLocally(updateDataEvent.photoDetailList, updateDataEvent.position)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(loadMoreDataEvent: LoadMoreDataEvent) {
        EventBus.getDefault().removeStickyEvent(loadMoreDataEvent)

        isLoading = true
        currentPage += 1
        //adapter.showLoading(true)
        //adapter.notifyDataSetChanged()
        searchPhotosViewModel.getPhotos(searchQuery, currentPage.toString())
    }

    override fun onItemClick(photoDetail: PhotoDetail, view: View) {
        val intent = Intent(this, PhotoDetailActivity::class.java)
        intent.putExtra(PhotoDetailActivity.ITEM_ID, photoDetail.id)
        intent.putExtra(PhotoDetailActivity.ITEM_INDEX, dataList.indexOf(photoDetail))

        var bundle: Bundle? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val p1 = android.support.v4.util.Pair.create(view, ViewCompat.getTransitionName(view))
            bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1).toBundle()
        }

        startActivity(intent, bundle)
    }

    override fun onActivityReenter(resultCode: Int, data: Intent) {
        super.onActivityReenter(resultCode, data)
        reenterState = Bundle(data.extras)
        reenterState?.let {
            val startingPosition = it.getInt(EXTRA_STARTING_ALBUM_POSITION)
            val currentPosition = it.getInt(EXTRA_CURRENT_ALBUM_POSITION)
            if (startingPosition != currentPosition) recyclerView.smoothScrollToPosition(currentPosition)
            ActivityCompat.postponeEnterTransition(this)

            recyclerView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    recyclerView.viewTreeObserver.removeOnPreDrawListener(this)
                    ActivityCompat.startPostponedEnterTransition(this@MainActivity)
                    return true
                }
            })
        }
    }

    private fun updateData(detailList: ArrayList<PhotoDetail>) {
        isLoading = false
        dataList.addAll(detailList)
        liveData.value = detailList
    }
}

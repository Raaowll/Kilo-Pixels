package com.photos.kilopixels.view.search

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import butterknife.ButterKnife
import com.photos.kilopixels.R
import com.photos.kilopixels.model.PhotoDetail
import com.photos.kilopixels.view.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_search_photos.*
import butterknife.BindView
import com.photos.kilopixels.model.events.UpdateDataEvent
import com.photos.kilopixels.utils.GridItemDecorator
import com.photos.kilopixels.utils.PaginationScrollListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import android.view.*
import android.graphics.Color
import android.support.v7.widget.*
import android.os.Build
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView


/**
 * Created by rahul on 19/1/18.
 */

/**
 * A simple [Fragment] subclass.
 * Use the [SearchPhotosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class SearchPhotosFragment : Fragment(), LifecycleOwner /*GridItemClickListener*/ {

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentMyRecipes.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): SearchPhotosFragment {
            val fragment = SearchPhotosFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): SearchPhotosFragment {
            return SearchPhotosFragment()
        }
    }

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

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
    private var layoutManager: StaggeredGridLayoutManager? = null

    private var searchView: SearchView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mParam1 = arguments?.let { it.getString(ARG_PARAM1) }
        mParam2 = arguments?.let { it.getString(ARG_PARAM2) }

        setHasOptionsMenu(true)

        lifecycle.addObserver(photosLifecycleObserver)

        searchPhotosViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchPhotosViewModel::class.java)

        //adapter = SearchPhotosRecyclerAdapter(photoDetail = ArrayList<PhotoDetail>(), context = activity!!, gridItemClickListener = this)

        searchPhotosViewModel.getLiveData().observe(this, Observer { t -> adapter.updateDataList(t as List<PhotoDetail>) })

        searchPhotosViewModel.getLiveData().observe(this, Observer { isLoading = false })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater?.let { it.inflate(R.layout.fragment_search_photos, container, false) }

        ButterKnife.bind(this, view)

        initRecyclerView()

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.let { it.inflate(R.menu.search_fragment_menu, menu) }

        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView!!.maxWidth = Integer.MAX_VALUE

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchQuery = query

                resetData()

                searchPhotosViewModel.getPhotos(query, currentPage.toString())

                /*searchView!!.setQuery("", false)
                searchView!!.clearFocus()
                searchView!!.isIconified = true*/

                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })
    }

    private fun resetData() {
        recyclerView.visibility = View.VISIBLE
        infoTv.visibility = View.GONE
        adapter.photosList.clear()
        adapter.notifyDataSetChanged()
        currentPage = PAGE_START
        isLoading = false
        isLastPage = false
        searchPhotosViewModel.clearData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_search) {
            true
        } /*else if (id == R.id.action_grid_span_1) {
            layoutManager = setLayoutManager(1)
            //adapter.notifyDataSetChanged()
            true
        } else if (id == R.id.action_grid_span_2) {
            layoutManager = setLayoutManager(2)
            //adapter.notifyDataSetChanged()
            true
        } else if (id == R.id.action_grid_span_3) {
            layoutManager = setLayoutManager(3)
            //adapter.notifyDataSetChanged()
            true
        }*/ else super.onOptionsItemSelected(item)
    }

    private fun whiteNotificationBar(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
            activity?.window?.statusBarColor = Color.WHITE
        }
    }

    private fun initRecyclerView() {
        //val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        //recyclerView.layoutManager = manager

        val itemAnimator = DefaultItemAnimator()
        //itemAnimator.addDuration = 1000
        recyclerView.itemAnimator = itemAnimator


        layoutManager = setLayoutManager(currentGridSpanCount)
        recyclerView.setHasFixedSize(true)

        val itemDecoration = GridItemDecorator(this!!.activity!!, R.dimen.grid_spacing3dp)
        recyclerView.addItemDecoration(itemDecoration)

        recyclerView.isNestedScrollingEnabled = false

        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object: PaginationScrollListener() {
            override fun layoutManager(): /*Staggered*/GridLayoutManager {
                return layoutManager as /*Staggered*/GridLayoutManager
            }

            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1
                //adapter.showLoading(true)
                //adapter.notifyDataSetChanged()
                searchPhotosViewModel.getPhotos(searchQuery, currentPage.toString())
            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })
    }

    private fun setLayoutManager(spanCount: Int): StaggeredGridLayoutManager {
        currentGridSpanCount = spanCount
        val layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        recyclerView.layoutManager = layoutManager
        return layoutManager
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(updateDataEvent: UpdateDataEvent) {
        EventBus.getDefault().removeStickyEvent(updateDataEvent)

        if (updateDataEvent.photoDetailList != null) {
            searchPhotosViewModel.updateDataLocally(updateDataEvent.photoDetailList, updateDataEvent.position)
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        /*if (adapter != null && adapter.photosList != null && !adapter.photosList.isEmpty()) {
            recyclerView.visibility = View.VISIBLE
            infoTv.visibility = View.GONE
        } else {
            recyclerView.visibility = View.GONE
            infoTv.visibility = View.VISIBLE
        }*/
    }

    /*override fun onItemClick(position: Int, view: View) {
        val fragment = ViewPagerFragment.newInstance(photoDetailList = photoDetailList, current = position)

        val previousFragment = fragmentManager?.findFragmentById(R.id.fragmentContainer)

        // 1. Exit for Previous Fragment
        //val exitSlide = Slide()
        //exitSlide.duration = SLIDE_DEFAULT_TIME
        //exitSlide.slideEdge = Gravity.LEFT
        //previousFragment?.exitTransition = exitSlide

        val exitFade = Fade()
        exitFade.duration = FADE_DEFAULT_TIME
        previousFragment?.exitTransition = exitFade

        // 2. Shared Elements Transition
        //val enterTransitionSet = TransitionSet()
        //enterTransitionSet.addTransition(TransitionInflater.from(this.activity).inflateTransition(android.R.transition.slide_right))
        //enterTransitionSet.duration = MOVE_DEFAULT_TIME
        //enterTransitionSet.startDelay = FADE_DEFAULT_TIME
        //fragment.sharedElementEnterTransition = enterTransitionSet

        // Defines enter transition only for shared element

        val changeBoundsTransition = ChangeBounds()
        //changeBoundsTransition.interpolator = AnticipateOvershootInterpolator()
        //changeBoundsTransition.startDelay = SLIDE_DEFAULT_TIME + FADE_DEFAULT_TIME
        changeBoundsTransition.duration = MOVE_DEFAULT_TIME
        //fragment.sharedElementEnterTransition = changeBoundsTransition;

        val transition = TransitionSet();

        transition.addTransition(ChangeBounds())
        transition.addTransition(ChangeTransform())
        transition.addTransition(ChangeClipBounds())
        transition.addTransition(ChangeImageTransform())

        transition.duration = 400
        //transition.interpolator = FastOutSlowInInterpolator()
        val pathMotion = ArcMotion()
        pathMotion.maximumAngle = 50F
        transition.setPathMotion(pathMotion)
        fragment.sharedElementEnterTransition = transition
        fragment.sharedElementReturnTransition = transition

        // 3. Enter Transition for New Fragment
        val enterFade = Fade()
        enterFade.startDelay = FADE_DEFAULT_TIME
        enterFade.duration = FADE_DEFAULT_TIME
        fragment.enterTransition = enterFade
        fragment.reenterTransition = exitFade

        // Prevent transitions for overlapping
        //fragment.allowEnterTransitionOverlap = false
        //fragment.allowReturnTransitionOverlap = false
        val name = ViewCompat.getTransitionName(imageView)
        Timber.d("Transition name: $name")

        fragmentManager?.beginTransaction()
                ?.addSharedElement(imageView, name)
                ?.addToBackStack("")
                ?.replace(R.id.fragmentContainer, fragment)
                ?.commit()
    }*/
}
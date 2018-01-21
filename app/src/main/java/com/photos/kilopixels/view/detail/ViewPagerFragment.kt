package com.photos.kilopixels.view.detail

import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import com.photos.kilopixels.model.PhotoDetail
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.photos.kilopixels.R
import android.support.v4.view.ViewPager
import kotlinx.android.synthetic.main.fragment_view_pager.*
import java.util.ArrayList


/**
 * Created by rahul on 21/1/18.
 */
class ViewPagerFragment: Fragment() {
    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val EXTRA_INITIAL_POS = "initial_pos"
        private const val EXTRA_IMAGES = "images"


        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentMyRecipes.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): ViewPagerFragment {
            val fragment = ViewPagerFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(photoDetailList: List<PhotoDetail>, current: Int): ViewPagerFragment {
            val fragment = ViewPagerFragment()
            val args = Bundle()
            args.putInt(EXTRA_INITIAL_POS, current)
            args.putParcelableArrayList(EXTRA_IMAGES, photoDetailList as ArrayList<out Parcelable>)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): ViewPagerFragment {
            return ViewPagerFragment()
        }
    }

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_pager, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentItem = arguments!!.getInt(EXTRA_INITIAL_POS)
        val photosList = arguments!!.getParcelableArrayList<PhotoDetail>(EXTRA_IMAGES)

        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, photosList)
        viewPager.adapter = viewPagerAdapter
        viewPager.currentItem = currentItem
    }
}
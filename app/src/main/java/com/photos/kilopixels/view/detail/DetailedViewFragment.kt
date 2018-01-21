package com.photos.kilopixels.view.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.photos.kilopixels.R
import com.photos.kilopixels.model.PhotoDetail
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.transition.TransitionInflater
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.photos.kilopixels.utils.GlideApp
import com.photos.kilopixels.utils.Utility
import kotlinx.android.synthetic.main.fragment_detail_view.*
import timber.log.Timber


/**
 * Created by rahul on 21/1/18.
 */
class DetailedViewFragment: Fragment() {
    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val EXTRA_IMAGE = "image_item"
        private const val EXTRA_TRANSITION_NAME = "transition_name"


        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentMyRecipes.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): DetailedViewFragment {
            val fragment = DetailedViewFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(photoDetail: PhotoDetail, transitionName: String): DetailedViewFragment {
            val fragment = DetailedViewFragment()
            val args = Bundle()
            args.putParcelable(EXTRA_IMAGE, photoDetail)
            args.putString(EXTRA_TRANSITION_NAME, transitionName);
            fragment.arguments = args
            return fragment
        }

        fun newInstance(photoDetail: PhotoDetail): DetailedViewFragment {
            val fragment = DetailedViewFragment()
            val args = Bundle()
            args.putParcelable(EXTRA_IMAGE, photoDetail)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): DetailedViewFragment {
            return DetailedViewFragment()
        }
    }

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mParam1 = arguments?.let { it.getString(DetailedViewFragment.ARG_PARAM1) }
        mParam2 = arguments?.let { it.getString(DetailedViewFragment.ARG_PARAM2) }

        postponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(context)
                    .inflateTransition(android.R.transition.move)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater?.let { it.inflate(R.layout.fragment_detail_view, container, false) }

        ButterKnife.bind(this, view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoDetail = arguments?.getParcelable<PhotoDetail>(EXTRA_IMAGE)
        val transitionName = arguments!!.getString(EXTRA_TRANSITION_NAME)

        Timber.d("Transition name, DetailedViewFragment: $transitionName")

        //startPostponedEnterTransition()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            detailIv.transitionName = transitionName;
        }

        val baseTarget = object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                startPostponedEnterTransition()
                detailIv.setImageDrawable(resource)
            }
        }

        GlideApp.with(activity!!)
                .load(Utility.getUrl(photoDetail!!))
                .dontAnimate()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                })
                .into(baseTarget)
    }
}
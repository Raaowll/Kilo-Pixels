package com.photos.kilopixels.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.photos.kilopixels.R
import com.photos.kilopixels.view.search.SearchPhotosFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
                .beginTransaction()
                .replace(fragmentContainer.id, SearchPhotosFragment.newInstance(), "").commit()
    }
}

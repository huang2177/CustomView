package com.huang.practice1.lyrics

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.huang.practice1.R
import kotlinx.android.synthetic.main.activity_lyric.*
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Des:
 * Created by huang on 2018/10/19 0019 10:31
 */
class LyricActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyric)
    }

    fun start(view: View) {
        lyric.start()
    }

    fun stop(view: View) {
        lyric.stop()
    }
}
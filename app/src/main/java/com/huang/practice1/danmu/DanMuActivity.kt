package com.huang.practice1.danmu

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.huang.practice1.R
import kotlinx.android.synthetic.main.activity_danmu.*

/**
 * Des:
 * Created by huang on 2018/11/29 0029 11:23
 */
class DanMuActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_danmu)
    }

    fun send(view: View) {
        danMu.addDanMu("66666666666666666")
    }
}
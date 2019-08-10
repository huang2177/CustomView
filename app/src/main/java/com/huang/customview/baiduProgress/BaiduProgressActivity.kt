package com.huang.customview.baiduProgress

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.huang.customview.R
import kotlinx.android.synthetic.main.activity_baidu_progress.*

/**
 * Des:
 * Created by huang on 2018/10/19 0019 10:46
 */
class BaiduProgressActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baidu_progress)
    }

    fun start(view: View) {
        baidu.start()
    }

    fun stop(view: View) {
        baidu.stop()
    }
}
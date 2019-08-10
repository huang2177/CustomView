package com.huang.customview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.huang.customview.process.ProcessActivity
import com.huang.customview.baiduProgress.BaiduProgressActivity
import com.huang.customview.fallObject.FallingActivity
import com.huang.customview.gaussianBlur.GaussianBlurActivity
import com.huang.customview.lyrics.LyricActivity
import com.huang.customview.miTime.MiTimeActivity

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun lyric(view: View) {
        startActivity(Intent(this, LyricActivity::class.java))
    }

    fun baiduProgress(view: View) {
        startActivity(Intent(this, BaiduProgressActivity::class.java))
    }

    fun miTime(view: View) {
        startActivity(Intent(this, MiTimeActivity::class.java))
    }

    fun process(view: View) {
        startActivity(Intent(this, ProcessActivity::class.java))
    }

    fun gaussian(view: View) {
        startActivity(Intent(this, GaussianBlurActivity::class.java))
    }

    fun snowDown(view: View) {
        startActivity(Intent(this, FallingActivity::class.java))
    }
}
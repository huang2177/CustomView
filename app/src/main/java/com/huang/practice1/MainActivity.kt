package com.huang.practice1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.huang.practice1.baiduprogress.BaiduProgressActivity
import com.huang.practice1.danmu.DanMuActivity
import com.huang.practice1.gaussian_blur.GaussianBlurActivity
import com.huang.practice1.luckypan.LuckyPanActivity
import com.huang.practice1.lyrics.LyricActivity
import com.huang.practice1.mitime.MiTimeActivity
import com.huang.practice1.process.ProcessActivity
import com.huang.practice1.scaleview.ScaleActivity

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

    fun luckyPan(view: View) {
        startActivity(Intent(this, LuckyPanActivity::class.java))
    }

    fun danMu(view: View) {
        startActivity(Intent(this, DanMuActivity::class.java))
    }

    fun scale(view: View) {
        startActivity(Intent(this, ScaleActivity::class.java))
    }
}
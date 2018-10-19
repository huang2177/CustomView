package com.huang.practice1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import com.huang.practice1.baiduprogress.BaiduProgressActivity
import com.huang.practice1.lyrics.LyricActivity
import com.huang.practice1.lyrics.readLrc
import com.huang.practice1.mitime.MiTimeActivity
import com.huang.practice1.process.ProcessActivity
import kotlinx.android.synthetic.main.activity_main.*

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
}
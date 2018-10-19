package com.huang.practice1.recycle

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.huang.practice1.R
import kotlinx.android.synthetic.main.activity_recyle.*

/**
 * Des:
 * Created by huang on 2018/9/24 0024 14:44
 */
class RecycleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyle)

        var manager = LinearLayoutManager(this)
        recycler.layoutManager = manager

        var adapter = RecycleAdapter(this)
        recycler.adapter = adapter
    }
}

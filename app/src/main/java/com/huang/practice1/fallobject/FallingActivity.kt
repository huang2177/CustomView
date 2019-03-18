package com.huang.practice1.fallobject

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.huang.practice1.R
import com.huang.practice1.gaussian_blur.ImageUtils
import kotlinx.android.synthetic.main.activity_fall_object.*
import kotlinx.android.synthetic.main.gaussian_blur_layout.*


/**
 * Des:
 * Created by huang on 2018/10/19 0019 10:31
 */
class FallingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fall_object)
        //gaussian()
        initSnowDown()
    }

    fun gaussian() {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.bg)
        val gaussianBlur = ImageUtils.gaussianBlur(10, bitmap, this)
        bg.setImageBitmap(gaussianBlur)
    }

    private fun initSnowDown() {
        val drawable = ContextCompat.getDrawable(this, R.mipmap.snow)!!
        val builder = Builder(drawable)
                .setSpeed(7, true)
                .setSize(50, 50, true)
                .setWind(5, true, true)

        //初始化一个雪球样式的fallObject
        snow.addFallObject(builder, 80)//添加50个雪球对象
    }
}
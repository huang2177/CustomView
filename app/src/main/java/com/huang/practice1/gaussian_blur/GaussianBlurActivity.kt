package com.huang.practice1.gaussian_blur

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import com.huang.practice1.R
import kotlinx.android.synthetic.main.gaussian_blur_layout.*

/**
 * Des:
 * Created by huang on 2018/11/7 0007 15:25
 */
class GaussianBlurActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gaussian_blur_layout)
    }

    fun gaussian(view: View) {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.timg)
        val gaussianBlur = ImageUtils.gaussianBlur(10, bitmap, this)
        img.setImageBitmap(gaussianBlur)
    }
}

package com.huang.practice1.fallobject

import android.graphics.Canvas
import android.graphics.Paint

/**
 * Des:
 * Created by huang on 2018/10/19 0019 17:36
 */
class SnowView : BaseFallObject() {

    override fun drawContent(canvas: Canvas, paint: Paint) {
        canvas.drawCircle(50f, 50f, 10f, paint)
    }
}
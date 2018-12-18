package com.huang.practice1.fallobject

import android.graphics.Canvas
import android.graphics.Paint

/**
 * Des:
 * Created by huang on 2018/10/19 0019 17:49
 */
abstract class BaseFallObject {
    abstract fun drawContent(canvas: Canvas, paint: Paint)
}
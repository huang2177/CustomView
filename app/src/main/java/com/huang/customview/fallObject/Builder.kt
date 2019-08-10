package com.huang.customview.fallObject

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

/**
 * Des:
 * Created by huang on 2019/3/18 0018 14:16
 */

class Builder {
    var initSpeed: Int = 0
    private var initWindLevel: Int = 0
    var bitmap: Bitmap? = null

    var isSpeedRandom: Boolean = false
    var isSizeRandom: Boolean = false
    var isWindRandom: Boolean = false
    var isWindChange: Boolean = false

    private val defaultSpeed = 10//默认下降速度
    private val defaultWindLevel = 0//默认风力等级
    val defaultWindSpeed = 10//默认单位风速

    constructor(bitmap: Bitmap) {
        this.initSpeed = defaultSpeed
        this.initWindLevel = defaultWindLevel
        this.bitmap = bitmap

        this.isSpeedRandom = false
        this.isSizeRandom = false
        this.isWindRandom = false
        this.isWindChange = false
    }

    constructor(drawable: Drawable) {
        this.initSpeed = defaultSpeed
        this.initWindLevel = defaultWindLevel
        this.bitmap = drawableToBitmap(drawable)

        this.isSpeedRandom = false
        this.isSizeRandom = false
        this.isWindRandom = false
        this.isWindChange = false
    }

    /**
     * 设置物体的初始下落速度
     * @param speed
     * @return
     */
    fun setSpeed(speed: Int): Builder {
        this.initSpeed = speed
        return this
    }

    /**
     * 设置物体的初始下落速度
     * @param speed
     * @param isRandomSpeed 物体初始下降速度比例是否随机
     * @return
     */
    fun setSpeed(speed: Int, isRandomSpeed: Boolean): Builder {
        this.initSpeed = speed
        this.isSpeedRandom = isRandomSpeed
        return this
    }

    /**
     * 设置物体大小
     * @param w
     * @param h
     * @return
     */
    fun setSize(w: Int, h: Int): Builder {
        this.bitmap = changeBitmapSize(this.bitmap!!, w, h)
        return this
    }

    /**
     * 设置物体大小
     * @param w
     * @param h
     * @param isRandomSize 物体初始大小比例是否随机
     * @return
     */
    fun setSize(w: Int, h: Int, isRandomSize: Boolean): Builder {
        this.bitmap = changeBitmapSize(this.bitmap!!, w, h)
        this.isSizeRandom = isRandomSize
        return this
    }

    /**
     * 设置风力等级、方向以及随机因素
     * @param level 风力等级（绝对值为 5 时效果会比较好），为正时风从左向右吹（物体向X轴正方向偏移），为负时则相反
     * @param isWindRandom 物体初始风向和风力大小比例是否随机
     * @param isWindChange 在物体下落过程中风的风向和风力是否会产生随机变化
     * @return
     */
    fun setWind(level: Int, isWindRandom: Boolean, isWindChange: Boolean): Builder {
        this.initWindLevel = level
        this.isWindRandom = isWindRandom
        this.isWindChange = isWindChange
        return this
    }

    fun build(): FallObject {
        return FallObject(this)
    }

    /**
     * drawable图片资源转bitmap
     * @param drawable
     * @return
     */
    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                if (drawable.opacity != PixelFormat.OPAQUE)
                    Bitmap.Config.ARGB_8888
                else
                    Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }

    /**
     * 改变bitmap的大小
     * @param bitmap 目标bitmap
     * @param newW 目标宽度
     * @param newH 目标高度
     * @return
     */
    fun changeBitmapSize(bitmap: Bitmap, newW: Int, newH: Int): Bitmap {
        var bitmap = bitmap
        val oldW = bitmap.width
        val oldH = bitmap.height
        // 计算缩放比例
        val scaleWidth = newW.toFloat() / oldW
        val scaleHeight = newH.toFloat() / oldH
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, oldW, oldH, matrix, true)
        return bitmap
    }
}
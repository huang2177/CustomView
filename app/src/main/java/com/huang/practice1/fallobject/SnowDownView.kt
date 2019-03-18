package com.huang.practice1.fallobject

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.huang.practice1.R
import java.util.*


/**
 * Des:仿雪花飘落（）
 * Created by huang on 2018/10/19 0019 16:39
 */
class SnowDownView(context: Context, attrs: AttributeSet) : View(context, attrs), Runnable {
    private var color: Int
    private var paint = Paint()
    private val random = Random()

    private var ViewWidth = 0
    private var ViewHeight = 0
    private val defaultWidth = 600//默认宽度
    private val defaultHeight = 1000//默认高度
    private val intervalTime = 40L//重绘间隔时间

    private var snowX = 0f
    private var snowY = 0f

    init {
        val arrays = context.obtainStyledAttributes(attrs, R.styleable.SnowDownView)
        color = arrays.getColor(R.styleable.SnowDownView_color, Color.WHITE)
        arrays.recycle()

        paint.color = color
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureSize(defaultWidth, widthMeasureSpec)
        val height = measureSize(defaultHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }


    private fun measureSize(defaultSize: Int, measureSpec: Int): Int {
        var result = defaultSize
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize)
        }
        return result
    }

    var canvas: Canvas? = null
    override fun onDraw(canvas: Canvas) {
        this.canvas = canvas
        handler.postDelayed(this, intervalTime)
    }

    private fun getRandomX() {
        snowX = random.nextInt(width).toFloat()
        canvas?.drawCircle(snowX, snowY, 25f, paint)
    }

    override fun run() {
        getRandomX()
        snowY += 10f
        if (snowY > height) {
            snowY = 0f
        }
        invalidate()
    }

}

package com.huang.practice1.danmu

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout

/**
 * Des:
 * Created by huang on 2018/11/29 0029 10:50
 */
@SuppressLint("ViewConstructor")
class DanMuView(var msg: String, context: Context?) : View(context) {

    val paint = Paint()
    val bgPaint = Paint()

    var textHeight: Float
    var bgRadius = 10f
    var defaultHeight = 0
    var rectF: RectF = RectF()

    init {
        paint.textSize = 36f
        paint.color = Color.RED
        paint.isAntiAlias = true

        bgPaint.isAntiAlias = true
        bgPaint.color = Color.BLACK
        bgPaint.style = Paint.Style.FILL

        defaultHeight = 90
        textHeight = paint.fontMetrics.descent - paint.fontMetrics.ascent

        val params = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        params.leftMargin = 12
        params.bottomMargin = 12
        params.gravity = Gravity.BOTTOM
        layoutParams = params
    }

    override fun onMeasure(wSpec: Int, hSpec: Int) {
        setMeasuredDimension(View.MeasureSpec.getSize(wSpec), defaultHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        rectF.set(0f, 0f, paint.measureText(msg), height.toFloat())
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawRoundRect(rectF, bgRadius, bgRadius, bgPaint)
        canvas?.drawText(msg, 0f, (defaultHeight / 2 + textHeight / 2 - paint.fontMetrics.descent), paint)
    }

}
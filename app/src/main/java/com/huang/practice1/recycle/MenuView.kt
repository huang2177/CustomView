package com.huang.practice1.recycle

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.huang.practice1.R

/**
 * Des: 底部导航栏View
 * Created by huang on 2018/9/24 0024 16:47
 */
class MenuView(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet), View.OnClickListener {
    override fun onClick(v: View?) {

    }

    constructor(context: Context) : this(context, null)

    private var text = ""
    private var paint = Paint()
    private var bitmap: Bitmap
    private var descent: Float
    private var iconWidth: Int
    private var iconHeight: Int
    private var textHeight: Int
    private var indicatorHeight: Int

    private var mCircleRadius: Int
    private var mCircleDestanse: Int
    private var needCircle = true

    init {
        text = "首页"
        mCircleRadius = 4
        indicatorHeight = 5
        mCircleDestanse = 5
        paint.isAntiAlias = true
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        iconWidth = bitmap.width
        iconHeight = bitmap.height

        paint.textSize = 14f
        descent = paint.fontMetrics.descent

        textHeight = descent.toInt() - paint.fontMetrics.ascent.toInt() + paint.fontMetrics.leading.toInt()

        setOnClickListener(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureChild(0), measureChild(1))
    }

    private fun measureChild(i: Int): Int {
        var newSize = 0
        when (i) {
            0 -> newSize = if (needCircle) (iconWidth + mCircleDestanse + mCircleRadius * 2) else iconWidth
            1 -> newSize = (iconHeight + textHeight)
        }
        return newSize
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawIcon(canvas)
        drawIndicator(canvas)
        drawText(canvas)
        drawCircle(canvas)
    }

    private fun drawText(canvas: Canvas?) {
        paint.color = Color.RED
        paint.textAlign = Paint.Align.CENTER
        val x = (iconWidth / 2f)
        val y = iconHeight + textHeight
        canvas!!.drawText(text, x, (y - descent), paint)
    }

    private fun drawIcon(canvas: Canvas?) {
        canvas!!.drawBitmap(bitmap, 0f, 0f, null)
    }


    private fun drawIndicator(canvas: Canvas?) {
        paint.color = Color.GREEN
        var top = iconHeight + (textHeight - indicatorHeight) / 2
        var rect = Rect(0, top, iconWidth, (top + indicatorHeight))
        canvas!!.drawRect(rect, paint)
    }

    private fun drawCircle(canvas: Canvas?) {
        if (!needCircle) return
        paint.color = Color.RED
        canvas!!.drawCircle(width.toFloat() - mCircleRadius.toFloat(), mCircleRadius.toFloat(), mCircleRadius.toFloat(), paint)
    }

    fun setTitle(title: String) {
        text = title
        invalidate()
    }
}
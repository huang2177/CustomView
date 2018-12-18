package com.huang.practice1.luckypan

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import java.util.*

/**
 * Des: 自定义抽奖转盘
 * Created by huang on 2018/11/28 0028 14:08
 */
class LuckyPanView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var paint = Paint()
    var defaultSize = 0

    var screenWidth = 0
    var screenheight = 0

    //扇形扫过的矩形区域
    var oval: RectF? = null
    //扇形的个数
    val itemSize = 9

    init {
        paint.textSize = 54f
        paint.isAntiAlias = true
        paint.color = Color.DKGRAY
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER

        screenWidth = context!!.resources!!.displayMetrics!!.widthPixels
        screenheight = context.resources!!.displayMetrics!!.heightPixels
        defaultSize = (screenWidth * 0.85).toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureSpec(widthMeasureSpec), measureSpec(heightMeasureSpec))
    }

    @SuppressLint("SwitchIntDef")
    private fun measureSpec(measureSpec: Int): Int {
        val mode = View.MeasureSpec.getMode(measureSpec)
        val size = View.MeasureSpec.getSize(measureSpec)
        return when (mode) {
            View.MeasureSpec.EXACTLY -> size
            View.MeasureSpec.AT_MOST -> defaultSize
            else -> size
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val x = (width / 2).toFloat()
        val y = (height / 2).toFloat()
        oval = RectF(x - defaultSize / 2, y - defaultSize / 2, x + defaultSize / 2, y + defaultSize / 2)
    }

    override fun onDraw(canvas: Canvas?) {
        drawItem(canvas!!)
    }


    /**
     * 画每个item（扇形）
     */
    private fun drawItem(canvas: Canvas) {
        val itemAngle = 360f / itemSize
        for (i in 0 until itemSize) {
            paint.color = if (i % 2 != 0) Color.WHITE else Color.CYAN
            canvas.drawArc(oval, i * itemAngle, itemAngle, true, paint)

            //画文字的圆弧半径
            var radius = oval!!.top * 0.75
            //画文字的x坐标
            var textX = Math.sqrt(radius * radius / 5).toFloat()
            //画文字的y坐标
            var textY = -2 * textX
            paint.color = Color.RED
            var x = (width / 2).toFloat()
            var y = (height / 2).toFloat()


            canvas.translate(x, y)
            canvas.drawText("100积分", textX, textY, paint)
            canvas.translate(-x, -y)
        }
    }
}
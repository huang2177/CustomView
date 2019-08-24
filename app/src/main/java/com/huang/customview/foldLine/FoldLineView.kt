package com.huang.customview.foldLine

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.*
import com.huang.customview.extended.*

class FoldLineView(context: Context, attr: AttributeSet) : View(context, attr) {
    companion object {
        private const val DEFAULT_LEVEL_TEXT = "好"
    }

    private var cellWidth = 0f

    private var cellHeight = 0f
    private var textSize = sp2px(16, context.resources)

    private var levelWidth = 0f
    private var weekTextHeight = 0f
    private var cellTopMargin = dp2px(25, context.resources) // 单元格距离顶部文字的间距
    private var cellLeftMargin = dp2px(10, context.resources) // 单元格距离左边文字的间距
    private val radius = dp2px(5, context.resources)

    private var cellRawNum = 3
    private var cellColumnNum = 7
    private val defaultHeight = dp2px(170, context.resources)

    private var paint = Paint()

    private val levelList = listOf("好", "中", "差")
    private val dotsPosList = mutableListOf<Pair<Float, Float>>()
    // 需要绘制的点，例如<'好'，0> （1代表星期一）
    var data: List<Pair<String, Int>> = listOf()
        set(value) {
            field = value
            invalidate()
        }

    init {
        paint.isAntiAlias = true
        paint.textSize = textSize
        paint.style = Paint.Style.FILL

        levelWidth = paint.getTextWidth(DEFAULT_LEVEL_TEXT)
        weekTextHeight = paint.getTextHeight(DEFAULT_LEVEL_TEXT)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height =
            if (getMode(heightMeasureSpec) == EXACTLY) getSize(heightMeasureSpec) else defaultHeight
        setMeasuredDimension(getSize(widthMeasureSpec), height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        // 计算出单元格的宽高，以便后面绘制圆点
        cellWidth = (width - levelWidth * 2 - cellLeftMargin) / (cellColumnNum - 1)
        cellHeight = (height - weekTextHeight * 2 - cellTopMargin) / (cellRawNum - 1)
    }

    override fun onDraw(canvas: Canvas?) {
        drawCellAndText(canvas!!)
        drawDots(canvas)
        drawFoldLine(canvas)
    }

    /**
     * 绘制点之间的连线
     */
    private fun drawFoldLine(canvas: Canvas) {
        if (dotsPosList.size < 2) return
        paint.color = Color.WHITE
        dotsPosList.sortBy { it.first }

        var firstDotX = dotsPosList[0].first
        var firstDotY = dotsPosList[0].second

        for ((index, dot) in dotsPosList.withIndex()) {
            if (index == 0) continue
            canvas.drawLine(firstDotX, firstDotY, dot.first, dot.second, paint)
            firstDotX = dot.first
            firstDotY = dot.second
        }
    }

    /**
     * 绘制小圆点
     */
    private fun drawDots(canvas: Canvas) {
        dotsPosList.clear()
        paint.color = Color.RED
        data.filter { levelList.contains(it.first) && it.second in 0 until cellColumnNum }
            .forEach {
                val pos = getPosByIndex(it)
                canvas.drawCircle(pos.first, pos.second, radius.toFloat(), paint)
                dotsPosList.add(pos)
            }
    }

    /**
     * 绘制单元格 和 文字
     */
    private fun drawCellAndText(canvas: Canvas) {
        for (i in 0 until cellRawNum) {
            val startX = levelWidth + cellLeftMargin
            val startY = weekTextHeight + cellTopMargin + i * cellHeight

            paint.textAlign = Paint.Align.LEFT
            paint.color = Color.WHITE
            canvas.drawText(levelList[i], 0f, startY + paint.getCenteredY(), paint)
            paint.color = Color.GRAY
            canvas.drawLine(startX, startY, width.toFloat() - levelWidth, startY, paint)
        }

        for (j in 0 until cellColumnNum) {
            val startY = weekTextHeight + cellTopMargin
            val startX = levelWidth + cellLeftMargin + j * cellWidth

            paint.textAlign = Paint.Align.CENTER
            paint.color = Color.WHITE
            canvas.drawText(j.getWeekText(), startX, paint.getToppedY(), paint)
            paint.color = Color.GRAY
            canvas.drawLine(startX, startY, startX, height.toFloat() - weekTextHeight, paint)
        }
    }

    private fun getPosByIndex(index: Pair<String, Int>): Pair<Float, Float> {
        val startX = levelWidth + cellLeftMargin
        val startY = weekTextHeight + cellTopMargin

        val cX = startX + index.second * cellWidth
        val cY = startY + levelList.indexOf(index.first) * cellHeight

        return Pair(cX, cY)
    }
}

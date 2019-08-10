package com.huang.customview.process

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.huang.customview.R

/**
 * Des: 自定义流程图
 * Created by huang on 2018/10/8 0008 11:50
 */
class ProcessView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    //点击事件的时间差
    val TOUCH_TIME = 1000
    //上一次点击的时间
    var preTime = 0L
    //当前点击的时间
    var currTime = 0L

    //垂直方向item的个数
    var verticalNum = 0

    //水平方向item的个数 (与垂直方向一一对应)
    var horizontalNum: List<Int>? = null

    //item的宽度
    var itemWidth = 120

    //item的高度
    var itemHeight = 0

    //divider的高度
    var dividerHeight = 0

    //item的颜色
    var itemColor = Color.BLACK

    //item水平间距
    var horizontalSpace: Int = 30

    var textHeight = 0f

    var paint = Paint()

    var rectBorder: Rect = Rect()

    var lastRectBorder: Rect? = null

    //用来保存每隔item的Rect，用于后面的点击事件判断
    var rects = HashMap<Int, List<Rect>>()

    var texts = getTextShow()

    var listener: OnItemClickListener? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProcessView1)
        itemColor = typedArray.getColor(R.styleable.ProcessView1_itemColor, Color.BLACK)
        verticalNum = typedArray.getInt(R.styleable.ProcessView1_verticalNum, 0)
        itemHeight = typedArray.getDimension(R.styleable.ProcessView1_itemHeight, 30f).toInt()
        dividerHeight = typedArray.getDimension(R.styleable.ProcessView1_itemVerticalSpace, 30f).toInt()
        horizontalNum = typedArray.getString(R.styleable.ProcessView1_horizontalNum).split(",").map { it.toInt() }

        typedArray.recycle()

        paint.textSize = 16f
        paint.color = itemColor
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.textAlign = Paint.Align.CENTER

        textHeight = paint.fontMetrics.descent - paint.fontMetrics.ascent + paint.fontMetrics.leading
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureChild(0), measureChild(1))
    }

    private fun measureChild(i: Int): Int {
        var newSize = 0
        when (i) {
            0 -> newSize = 0
            1 -> newSize = (verticalNum * (itemHeight + dividerHeight) - paddingTop - paddingBottom)
        }
        return newSize
    }

    override fun onDraw(canvas: Canvas?) {
        for (vIndex in 0 until verticalNum) {
            var list = mutableListOf<Rect>()

            val hNum = horizontalNum!![vIndex]
            // item实际宽度
            val tempWidth = (width - horizontalSpace * (hNum - 1)) / hNum

            for (hIndex in 0 until hNum) {
                val str = texts[vIndex]?.get(hIndex)
                rectBorder.top = (vIndex) * itemHeight + (vIndex) * dividerHeight
                rectBorder.bottom = rectBorder.top + itemHeight
                when {
                    tempWidth >= itemWidth -> {
                        // 距离两边的距离
                        val margin = (width - (itemWidth * hNum + (hNum - 1) * horizontalSpace)) / 2
                        rectBorder.left = margin + hIndex * itemWidth + hIndex * horizontalSpace
                        rectBorder.right = itemWidth + rectBorder.left
                    }
                    else -> {
                        rectBorder.left = hIndex * tempWidth + hIndex * horizontalSpace
                        rectBorder.right = tempWidth + rectBorder.left
                    }
                }
                drawContent(canvas, rectBorder, str)
                drawDivider(canvas!!, rectBorder, hNum)
                list.add(rectBorder)
            }
            rects[vIndex] = list
        }
    }

    /**
     * 画文字和背景
     */
    private fun drawContent(canvas: Canvas?, rect: Rect, str: String?) {
        canvas!!.drawRect(rect, paint)
        canvas.drawText(str
                , (rect.right + rect.left) / 2f
                , (rect.bottom + rect.top) / 2f + textHeight / 2 - paint.fontMetrics.descent
                , paint)
    }

    /**
     * 画背景线
     * @param rect 当前Item的Rect
     * @param currHNum 当前的水平方向 有多少个Item
     */
    private fun drawDivider(canvas: Canvas, rect: Rect, currHNum: Int) {
        when {
            currHNum % 2 != 0 -> {
                if (lastRectBorder == null) return
                canvas.drawLine((lastRectBorder!!.left + lastRectBorder!!.right) / 2f
                        , (lastRectBorder!!.top + lastRectBorder!!.bottom) / 2f
                        , (rect.left + rect.right) / 2f
                        , (rect.top + rect.bottom) / 2f
                        , paint)
            }
        }

        lastRectBorder = rect
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> preTime = System.currentTimeMillis()
            MotionEvent.ACTION_UP -> {
                currTime = System.currentTimeMillis()
                val result = isTouchPointInView(event)
                if (currTime - preTime < TOUCH_TIME && result.isTouchInView) {
                    listener!!.onItemClick(result.vPosition, result.hPosition, "")
                }
            }
        }
        return true
    }

    /**
     * 判断点击事件是否在某个上面
     */
    private fun isTouchPointInView(event: MotionEvent?): Result {
        if (rects.size == 0) return Result(false, 0, 0)

        val eventX = event!!.rawX.toInt()
        val eventY = event.y.toInt()

        for (vIndex in 0 until rects.size) {
            val list = rects[vIndex]
            for (hIndex in list!!.indices) {
                val rect = list[hIndex]
                if (rect.contains(eventX, eventY)) {
                    return Result(true, vIndex, hIndex)
                }
            }
        }
        return Result(false, 0, 0)
    }

    data class Result(var isTouchInView: Boolean, var vPosition: Int, var hPosition: Int)

    private fun getTextShow(): HashMap<Int, List<String>> {
        var text1 = listOf("执行通知")
        var text2 = listOf("送达文书")
        var text3 = listOf("强行措施", "财产调查", "解除措施")
        var text4 = listOf("执行通知", "通知", "执行通知", "执行通知")
        var text5 = listOf("执行通知")
        var text6 = listOf("行通", "执通", "通知", "执行", "执行", "执知")
        var text7 = listOf("执行通", "执通知", "执行知")
        var map = HashMap<Int, List<String>>()
        map.put(0, text1)
        map.put(1, text2)
        map.put(2, text3)
        map.put(3, text4)
        map.put(4, text5)
        map.put(5, text6)
        map.put(6, text7)
        return map
    }


    interface OnItemClickListener {
        fun onItemClick(vPosition: Int, hPosition: Int, text: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}
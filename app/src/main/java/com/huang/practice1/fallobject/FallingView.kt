package com.huang.practice1.fallobject

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import com.huang.practice1.R


/**
 * Des:仿雪花飘落（）
 * Created by huang on 2018/10/19 0019 16:39
 */
class FallingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var color: Int
    private var paint = Paint()

    private val defaultWidth = 600//默认宽度
    private val defaultHeight = 1000//默认高度
    private val intervalTime = 40L//重绘间隔时间

    private var fallObjects = mutableListOf<FallObject>()


    init {
        val arrays = context.obtainStyledAttributes(attrs, R.styleable.FallingView)
        color = arrays.getColor(R.styleable.FallingView_color, Color.WHITE)
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

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        if (fallObjects.size <= 0) {
            return
        }
        for (i in 0 until fallObjects.size) {
            fallObjects[i].drawObject(canvas)
        }

        // 隔一段时间重绘一次, 动画效果
        handler.postDelayed({ invalidate() }, intervalTime)

    }


    /**
     * 向View添加下落物体对象
     * @param builder
     * @param num
     */
    fun addFallObject(builder: Builder, num: Int) {
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                viewTreeObserver.removeOnPreDrawListener(this)
                for (i in 0 until num) {
                    val newFallObject = FallObject(builder, width, height)
                    fallObjects.add(newFallObject)
                }
                return true
            }
        })
    }

    fun start() {
        invalidate()
    }

}

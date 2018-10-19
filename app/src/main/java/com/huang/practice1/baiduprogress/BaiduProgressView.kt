package com.huang.practice1.baiduprogress

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.huang.practice1.R
import android.view.animation.LinearInterpolator


/**
 * Des: 仿百度加载view
 * Created by huang on 2018/10/19 0019 10:18
 */
class BaiduProgressView(context: Context, attrs: AttributeSet) : View(context, attrs)
        , ValueAnimator.AnimatorUpdateListener
        , Animator.AnimatorListener {

    private var space = 0f
    private var radius = 0f
    private var maxWidth = 0f
    private var currentX = 0f
    private var currentIndex = 0
    private var colors: MutableList<Int>

    private var paint = Paint()
    private var valueAnimator: ValueAnimator? = null

    init {
        var arrays = context.obtainStyledAttributes(attrs, R.styleable.BaiduProgressView)
        space = arrays.getDimension(R.styleable.BaiduProgressView_space, 50f)
        radius = arrays.getDimension(R.styleable.BaiduProgressView_radius, 15f)
        colors = arrays.getString(R.styleable.BaiduProgressView_colors)
                .split(",")
                .map { Color.parseColor(it) } as MutableList<Int>
        arrays.recycle()

        paint.isAntiAlias = true
        maxWidth = radius * 2 + space
        currentX = radius * 2 + space
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureSize(widthMeasureSpec, 0).toInt()
                , measureSize(heightMeasureSpec, 1).toInt())
    }

    @SuppressLint("SwitchIntDef")
    private fun measureSize(measureSpec: Int, type: Int): Float {
        val mode = View.MeasureSpec.getMode(measureSpec)
        val size = View.MeasureSpec.getSize(measureSpec).toFloat()
        return when (mode) {
            View.MeasureSpec.EXACTLY -> if (type == 1) size else radius * 2 * 3 + 2 * space
            else -> if (type == 1) radius * 2 else radius * 2 * 3 + 2 * space
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var x = width / 2f
        canvas.translate(0f, height / 2f)

        //左边的圆
        paint.color = colors[0]
        canvas.drawCircle(x - currentX, 0f, radius, paint)

        //中间的圆
        paint.color = colors[1]
        canvas.drawCircle(x, 0f, radius, paint)

        //右边的圆
        paint.color = colors[2]
        canvas.drawCircle(x + currentX, 0f, radius, paint)
    }


    override fun onAnimationRepeat(animation: Animator?) {
        switchTemp(currentIndex)
    }

    override fun onAnimationCancel(animation: Animator?) {

    }

    override fun onAnimationEnd(animation: Animator?) {
    }

    override fun onAnimationStart(animation: Animator?) {

    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        currentX = animation.animatedValue as Float
        invalidate()
    }

    fun start() {
        valueAnimator = ValueAnimator
                .ofFloat(0f, maxWidth, 0f)
                .apply {
                    duration = 1000
                    repeatCount = -1
                    repeatMode = ValueAnimator.REVERSE
                    interpolator = LinearInterpolator()

                    addListener(this@BaiduProgressView)
                    addUpdateListener(this@BaiduProgressView)
                    start()
                }
    }

    fun stop() {
        valueAnimator?.cancel()
        currentX = radius * 2 + space
        invalidate()
    }

    /***
     * 交换位置 : 每次交换时，先让正在执行位置 与 中间位置交换
     * 实际上只要考虑两个位置（0,1）即可
     * @param index 正在执行的位置 第一次就是 0
     */
    private fun switchTemp(index: Int) {
        val temp = colors[2]
        colors[2] = colors[index]
        colors[index] = temp

        currentIndex = if (index == 0) 1 else 0
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }


}
package com.huang.customview.netHotClock

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.huang.customview.extended.*
import java.util.Calendar.*
import kotlin.properties.Delegates

/**
 * 仿抖音网红时钟
 */
class NetHotClockView(context: Context, attr: AttributeSet) : View(context, attr) {
    private var width = 0f
    private var height = 0f

    private var hourR = 0f
    private var minuteR = 0f
    private var secondR = 0f

    private var hourDeg = 0f
    private var minuteDeg = 0f
    private var secondDeg = 0f

    private val paint = Paint()

    private var runnable = Runnable { doInvalidate() }
    private var animator: ValueAnimator  by Delegates.notNull()

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#ffffff")

        animator = ValueAnimator.ofFloat(6f, 0f)
        animator.duration = 150
        animator.interpolator = LinearInterpolator()

        doInvalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        width = (measuredWidth - paddingLeft - paddingRight).toFloat()
        height = (measuredHeight - paddingTop - paddingBottom).toFloat()

        //统一用View宽度*系数来处理大小，这样可以联动适配样式
        hourR = width * 0.18f
        minuteR = width * 0.4f
        secondR = width * 0.4f
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        canvas.drawColor(Color.BLACK)//填充背景

        canvas.save()
        canvas.translate(width / 2, height / 2)//原点移动到中心

        //绘制各元件，后文会涉及到
        drawCenterInfo(canvas)
        drawHour(canvas, hourDeg)
        drawMinute(canvas, minuteDeg)
        drawSecond(canvas, secondDeg)

        canvas.restore()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        post(runnable)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(runnable)
    }

    /**
     * 绘制方法
     */
    private fun doInvalidate() {
        getInstance().run {
            val hour = get(HOUR)
            val minute = get(MINUTE)
            val second = get(SECOND)

            //这里将三个角度与实际时间关联起来，当前几点几分几秒，就把相应的圈逆时针旋转多少
            hourDeg = -360 / 12f * (hour - 1)
            minuteDeg = -360 / 60f * (minute - 1)
            secondDeg = -360 / 60f * (second - 1)

            //记录当前角度，然后让秒圈线性的旋转6°
            val hd = hourDeg
            val md = minuteDeg
            val sd = secondDeg

            animator.removeAllUpdateListeners()
            animator.addUpdateListener {
                val av = it.animatedValue as Float
                if (minute == 0 && second == 0) {
                    hourDeg = hd + av * 5//时圈旋转角度是分秒的5倍，线性的旋转30°
                }
                if (second == 0) {
                    minuteDeg = md + av//线性的旋转6°
                }
                secondDeg = av + sd
                invalidate()
            }
            animator.start()
        }
        postDelayed(runnable, 1000)
    }

    private fun drawSecond(canvas: Canvas, deg: Float) {
        paint.textSize = hourR * 0.22f
        canvas.save()
        canvas.rotate(deg)
        for (i in 0 until 60) {
            val iDeg = 360f / 60f * i

            canvas.save()
            canvas.rotate(iDeg)
            paint.textAlign = Paint.Align.LEFT
            paint.isFakeBoldText = isCurrentTime(iDeg, deg)
            paint.alpha = if (isCurrentTime(iDeg, deg)) 255 else (0.6f * 255).toInt()
            canvas.drawText("${(i + 1).addLeadingZero()}秒", secondR, paint.getCenteredY(), paint)
            canvas.restore()
        }
        canvas.restore()
    }

    private fun drawMinute(canvas: Canvas, deg: Float) {
        paint.textSize = hourR * 0.22f
        canvas.save()
        canvas.rotate(deg)
        for (i in 0 until 60) {
            val iDeg = 360f / 60f * i

            canvas.save()
            canvas.rotate(iDeg)
            paint.textAlign = Paint.Align.RIGHT
            paint.isFakeBoldText = isCurrentTime(iDeg, deg)
            paint.alpha = if (isCurrentTime(iDeg, deg)) 255 else (0.6f * 255).toInt()
            canvas.drawText("${(i + 1).addLeadingZero()}分", minuteR, paint.getCenteredY(), paint)
            canvas.restore()
        }
        canvas.restore()
    }

    private fun drawHour(canvas: Canvas, deg: Float) {
        paint.textSize = hourR * 0.22f
        canvas.save()
        canvas.rotate(deg)
        for (i in 0 until 12) {
            val iDeg = 360f / 12f * i

            canvas.save()
            canvas.rotate(iDeg)
            paint.textAlign = Paint.Align.LEFT
            paint.isFakeBoldText = isCurrentTime(iDeg, deg)
            paint.alpha = if (isCurrentTime(iDeg, deg)) 255 else (0.6f * 255).toInt()
            canvas.drawText("${(i + 1).addLeadingZero()}点", hourR, paint.getCenteredY(), paint)
            canvas.restore()
        }
        canvas.restore()
    }

    private fun isCurrentTime(iDeg: Float, deg: Float) =
        iDeg + deg == 0f || iDeg + deg == 360f

    private fun drawCenterInfo(canvas: Canvas) {
        getInstance().run {
            val hour = get(HOUR_OF_DAY).addLeadingZero()
            val minute = get(MINUTE).addLeadingZero()

            paint.alpha = 255
            paint.textSize = hourR * 0.48f//字体大小根据「时圈」半径来计算
            paint.isFakeBoldText = true
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("$hour : $minute", 0f, paint.getBottomedY(), paint)

            val month = (get(MONTH) + 1).addLeadingZero()
            val day = get(DAY_OF_MONTH)
            val week = (get(WEEK_OF_MONTH) - 1).toText()

            paint.textSize = hourR * 0.22f//字体大小根据「时圈」半径来计算
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("$month.$day 星期$week", 0f, paint.getToppedY(), paint)
        }
    }
}



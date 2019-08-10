package com.huang.customview.miTime

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*

/**
 * Des: 仿小米时间控件
 * Created by hs on 2018/7/30 0030 10:25
 */
class MiTimeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var mArcPaint: Paint? = null
    private var mTextPaint: Paint? = null
    private var mNamePaint: Paint? = null
    private var mHourPaint: Paint? = null
    private var mSecondPaint: Paint? = null
    private var mPaintMinute: Paint? = null
    private var mPaintTriangle: Paint? = null
    private var mProgressPaint: Paint? = null
    private var mProgressBgPaint: Paint? = null
    private var mCenterBallPaint: Paint? = null

    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mCenterX: Int = 0
    private var mCenterY: Int = 0
    private val paddingOut = 30
    private var mInnerRadius: Float = 0.toFloat()

    private var mSecondDegress: Float = 0.toFloat()
    private var mMinuteDegress: Float = 0.toFloat()
    private var mHourDegress: Float = 0.toFloat()
    private var mSecondMillsDegress: Float = 0.toFloat()

    private val mRunnable = {
        calculateDegree()
        invalidate()
        startTime()
    }

    init {
        val color = Color.parseColor("#ffffff")

        mArcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mArcPaint!!.color = color
        mArcPaint!!.strokeWidth = 2f
        mArcPaint!!.style = Paint.Style.STROKE

        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint!!.color = color
        mTextPaint!!.textSize = 32f
        mTextPaint!!.typeface = Typeface.DEFAULT_BOLD
        mTextPaint!!.textAlign = Paint.Align.CENTER

        mNamePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mNamePaint!!.color = color
        mNamePaint!!.textSize = 66f
        mNamePaint!!.typeface = Typeface.MONOSPACE
        mNamePaint!!.textAlign = Paint.Align.CENTER

        mProgressBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mProgressBgPaint!!.color = Color.parseColor("#4Cffffff")
        mProgressBgPaint!!.strokeWidth = 4f
        mProgressBgPaint!!.style = Paint.Style.STROKE

        mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mProgressPaint!!.strokeWidth = 4f
        mProgressPaint!!.style = Paint.Style.STROKE

        mInnerRadius = 24f
        mCenterBallPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mCenterBallPaint!!.color = color
        mCenterBallPaint!!.strokeWidth = 8f
        mCenterBallPaint!!.style = Paint.Style.STROKE

        mHourPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mHourPaint!!.color = Color.parseColor("#ffffff")
        mHourPaint!!.style = Paint.Style.FILL

        mPaintMinute = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintMinute!!.color = Color.parseColor("#ffffff")
        mPaintMinute!!.strokeWidth = 6f
        mPaintMinute!!.strokeCap = Paint.Cap.ROUND
        mPaintMinute!!.style = Paint.Style.STROKE

        mSecondPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mSecondPaint!!.color = Color.parseColor("#ffffff")
        mSecondPaint!!.style = Paint.Style.FILL

        mPaintTriangle = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintTriangle!!.color = color
        mPaintTriangle!!.style = Paint.Style.FILL

        calculateDegree()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureSpec(widthMeasureSpec), measureSpec(heightMeasureSpec))
    }

    private fun measureSpec(measureSpec: Int): Int {
        val mode = View.MeasureSpec.getMode(measureSpec)
        return if (mode == View.MeasureSpec.EXACTLY) {
            View.MeasureSpec.getSize(measureSpec)
        } else {
            600
        }
    }

    /**
     * 该方法被调用的时候表示已经测量完成
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = w
        mCenterX = w / 2
        mCenterY = h / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(mCenterX.toFloat(), mCenterY.toFloat())
        drawArcCircle(canvas)
        drawOutText(canvas)
        //drawName(canvas);
        drawCalibrationLine(canvas)
        drawHour(canvas)
        drawMinute(canvas)
        drawSecond(canvas)
        drawCenterBall(canvas)
    }

    /**
     * 画秒针
     *
     * @param canvas
     */
    private fun drawSecond(canvas: Canvas) {
        canvas.save()
        canvas.rotate(mSecondDegress)
        val path = Path()
        path.moveTo(0f, (-(mHeight * 3 / 8) + 20).toFloat())
        path.lineTo(-20f, (-(mHeight * 3 / 8) + 40).toFloat())
        path.lineTo(20f, (-(mHeight * 3 / 8) + 40).toFloat())
        path.close()
        canvas.drawPath(path, mPaintTriangle!!)
        canvas.restore()

        //绘制渐变刻度
        for (i in 0..90) {
            if (i % 6 == 0) {
                canvas.save()
                mProgressPaint!!.setARGB((255 - 2.7 * i).toInt(), 255, 255, 100)
                //这里的先减去90°，是为了旋转到开始角度，因为开始角度是y轴的负方向
                canvas.rotate(mSecondDegress - 90f - i.toFloat())
                val stopX = (mHeight / 2 * 0.75).toFloat() + 20
                canvas.drawLine((mHeight / 2 * 0.75).toFloat(), 0f, stopX, 0f, mProgressPaint!!)
                canvas.restore()
            }
        }
    }

    /**
     * 画分针
     *
     * @param canvas
     */
    private fun drawMinute(canvas: Canvas) {
        canvas.save()
        canvas.rotate(mMinuteDegress)
        canvas.drawLine(0f, -(mInnerRadius / 2 + 4), 0f, (-(mHeight / 3) + 25).toFloat(), mPaintMinute!!)
        canvas.restore()
    }

    /**
     * 画时针
     *
     * @param canvas
     */
    private fun drawHour(canvas: Canvas) {
        val path = Path()
        path.moveTo(-mInnerRadius / 4, -(mInnerRadius / 2 + 8) / 2)
        path.lineTo(mInnerRadius / 4, -(mInnerRadius / 2 + 8) / 2)
        path.lineTo(mInnerRadius / 8, (-(mHeight / 4)).toFloat())
        path.lineTo(-mInnerRadius / 8, (-(mHeight / 4)).toFloat())
        path.close()
        canvas.save()
        canvas.rotate(mHourDegress)
        canvas.drawPath(path, mHourPaint!!)
        canvas.restore()
    }

    /**
     * 画中心小圆
     *
     * @param canvas
     */
    private fun drawCenterBall(canvas: Canvas) {
        canvas.drawCircle(0f, 0f, mInnerRadius / 2, mCenterBallPaint!!)
    }

    /**
     * 绘制刻度线
     * desc:每个2°绘制一个刻度，也就是有180个刻度
     *
     * @param canvas
     */
    private fun drawCalibrationLine(canvas: Canvas) {
        for (i in 0..359) {
            if (i % 6 == 0) {
                canvas.save()
                canvas.rotate(i.toFloat())
                val stopX: Float
                if (i % 30 == 0) {
                    stopX = (mHeight / 2 * 0.75).toFloat() + 30
                    mProgressBgPaint!!.color = Color.parseColor("#ffffff")
                } else {
                    stopX = (mHeight / 2 * 0.75).toFloat() + 20
                    mProgressBgPaint!!.color = Color.parseColor("#4Cffffff")
                }
                canvas.drawLine((mHeight / 2 * 0.75).toFloat(), 0f, stopX, 0f, mProgressBgPaint!!)
                canvas.restore()
            }
        }
    }

    private fun drawName(canvas: Canvas) {
        canvas.drawText("erva", 0f, (-(mHeight / 7)).toFloat(), mNamePaint!!)
    }

    /**
     * 画外层文字（3,6,9,12）
     *
     * @param canvas
     */
    private fun drawOutText(canvas: Canvas) {
        val textRadius = ((mHeight - paddingOut) / 2).toFloat()

        val fm = mTextPaint!!.fontMetrics
        val textHeight = (fm.descent - fm.ascent) / 2 - fm.descent

        canvas.drawText("3", textRadius, textHeight, mTextPaint!!)
        canvas.drawText("9", -textRadius, textHeight, mTextPaint!!)

        canvas.drawText("6", 0f, textRadius + textHeight, mTextPaint!!)
        canvas.drawText("12", 0f, -textRadius + textHeight, mTextPaint!!)
    }


    /**
     * 画外层圆弧（5-85,95-175,185 -265,275-355）
     *
     * @param canvas
     * @return
     */
    private fun drawArcCircle(canvas: Canvas) {
        val rect = RectF((-(mHeight - paddingOut) / 2).toFloat(), (-(mHeight - paddingOut) / 2).toFloat(), ((mHeight - paddingOut) / 2).toFloat(), ((mHeight - paddingOut) / 2).toFloat())
        canvas.drawArc(rect, 5f, 80f, false, mArcPaint!!)
        canvas.drawArc(rect, 95f, 80f, false, mArcPaint!!)
        canvas.drawArc(rect, 185f, 80f, false, mArcPaint!!)
        canvas.drawArc(rect, 275f, 80f, false, mArcPaint!!)
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startTime()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopTime()
    }

    /**
     * 计算转动的角度
     */
    private fun calculateDegree() {
        val mCalendar = Calendar.getInstance()
        mCalendar.timeInMillis = System.currentTimeMillis()
        val minute = mCalendar.get(Calendar.MINUTE)
        val secondMills = mCalendar.get(Calendar.MILLISECOND)
        val second = mCalendar.get(Calendar.SECOND)
        val hour = mCalendar.get(Calendar.HOUR)
        mHourDegress = (hour * 30 + minute / 60 * 360).toFloat()
        mMinuteDegress = (minute * 6).toFloat()
        mSecondMillsDegress = second * 6 + secondMills * 0.006f
        mSecondDegress = (second * 6).toFloat()
        val mills = secondMills * 0.006f

        //因为是每2°旋转一个刻度，所以这里要根据毫秒值来进行计算
        if (mills >= 2 && mills < 4) {
            mSecondDegress += 6f
        } else if (mills >= 4 && mills < 6) {
            mSecondDegress += 12f
        }
    }

    fun startTime() {
        postDelayed(mRunnable, 1000)
    }


    fun stopTime() {
        removeCallbacks(mRunnable)
    }
}

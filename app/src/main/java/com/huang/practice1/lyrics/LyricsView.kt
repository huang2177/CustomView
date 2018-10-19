package com.huang.practice1.lyrics

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Scroller
import com.huang.practice1.R

/**
 * Des: 歌词View
 * Created by huang on 2018/10/12 0012 11:46
 */
class LyricsView(context: Context?, attrs: AttributeSet?) : View(context, attrs), Runnable {
    private val SNAP_VELOCITY = 0  //最小的滑动速率,小于这个速率不滑行

    private var paint: Paint = Paint()
    private var lyrics: List<LyricsItem>? = null
    private var lineTextColor: Int

    private var lineTextSize: Float
    private var defLineHeight: Int    //默认行高
    private var maxHeight: Int
    private var centerPosition: Int = 0
    private var currLineHeight: Int   //当前行的高度
    private var textHeight: Float
    private var scroller = Scroller(context, LinearInterpolator())

    private var mVelocityTracker: VelocityTracker
    private var rects: MutableList<Rect> = ArrayList()
    private var mHandler: Handler

    init {
        val typedArray = context!!.obtainStyledAttributes(attrs, R.styleable.LyricsView)
        lineTextColor = typedArray.getColor(R.styleable.LyricsView_lineTextColor, Color.BLACK)
        lineTextSize = typedArray.getDimension(R.styleable.LyricsView_lineTextSize, 26f)
        defLineHeight = typedArray.getDimension(R.styleable.LyricsView_lineHeight, 45f).toInt()
        val isTextBold = typedArray.getBoolean(R.styleable.LyricsView_isTextBold, false)
        currLineHeight = 0
        typedArray.recycle()

        paint.alpha = 20
        paint.isAntiAlias = true
        paint.color = lineTextColor
        paint.textSize = lineTextSize
        paint.isFakeBoldText = isTextBold
        paint.textAlign = Paint.Align.CENTER

        lyrics = readLrc(context.assets)
        maxHeight = defLineHeight * lyrics!!.size
        mVelocityTracker = VelocityTracker.obtain()
        textHeight = paint.fontMetrics.descent - paint.fontMetrics.ascent + paint.fontMetrics.leading

        mHandler = Handler()
    }

    override fun onDraw(canvas: Canvas) {
        for ((index, it) in lyrics!!.withIndex()) {
            currLineHeight = index * defLineHeight + height / 2 - defLineHeight
            drawContent(canvas, getLyricContent(it))
        }
        drawLyricsHighLight(canvas)
    }

    /**
     * 画歌词内容（歌名，演唱者，歌词）
     */
    private fun drawContent(canvas: Canvas, ti: String) {
        val rect = Rect(0, (currLineHeight), width, (currLineHeight + defLineHeight))
        canvas.drawText(ti, (width / 2f), getY(rect), paint)
        rects.add(rect)
    }

    /**
     * 画歌词高亮
     * @param index 高亮的位置
     */
    private fun drawLyricsHighLight(canvas: Canvas) {
        if (centerPosition >= lyrics!!.size) return
        val rect: Rect = rects[centerPosition]

        if (!isPalying || isTouched) {
            canvas.translate(0f, scrollY.toFloat())
            paint.strokeWidth = 1.4f
            paint.style = Paint.Style.FILL
            canvas.drawLine(20f, height / 2f, width.toFloat() - 20f, height / 2f, paint)
            canvas.translate(0f, -scrollY.toFloat())
        }
        paint.color = Color.parseColor("#FFFFFF")
        canvas.drawText(getLyricContent(lyrics!![centerPosition]), (width / 2f), getY(rect), paint)
        paint.color = lineTextColor
    }

    /**
     * 获取画文字的Y坐标
     */
    private fun getY(rect: Rect): Float {
        return (rect.bottom + rect.top) / 2f + textHeight / 2 - paint.fontMetrics.descent
    }


    var lastY = 0
    var currentY = 0
    var distanceY = 0
    var lastClickY = 0
    var lastScrollY = 0

    var isFling = false
    var isPalying = false
    var isTouched = false
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mVelocityTracker.addMovement(event)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                pause()
                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                }
                lastY = event.y.toInt()
                lastClickY = lastY
            }

            MotionEvent.ACTION_MOVE -> {
                //计算出两次动作间的滑动距离
                currentY = event.y.toInt()
                distanceY = -(currentY - lastY)
                lastY = currentY;
                smoothScrollBy(0, distanceY)
            }

            MotionEvent.ACTION_UP -> {
                resume()
                //根据触摸位置计算每像素的移动速率。
                mVelocityTracker.computeCurrentVelocity(1000)
                //计算速率
                val velocityY = -mVelocityTracker.yVelocity

                //计算出两次动作间的滑动距离
                currentY = event.y.toInt()
                distanceY = -(currentY - lastY)
                lastY = currentY
                //如果速率大于最小速率要求，执行滑行，否则拖动到位置
                if (Math.abs(velocityY) > SNAP_VELOCITY) {
                    if (!scroller.isFinished) {
                        scroller.abortAnimation()
                    }
                    fling(0, velocityY.toInt())
                } else {
                    smoothScrollBy(0, distanceY)
                }
            }
        }
        return true
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {//先判断mScroller滚动是否完成
            scrollTo(0, scroller.currY)
            postInvalidate()
        } else if (isFling && !isPalying) {
            scrollToCenter()
        }
        super.computeScroll()
    }

    private fun fling(velocityX: Int, velocityY: Int) {
        isFling = true
        scroller.fling(0, scrollY, velocityX, velocityY, 0, 0, 0, maxHeight - defLineHeight)
        invalidate()
    }


    private fun smoothScrollBy(dx: Int, dy: Int) {
        val y = when {
            scrollY + dy < 0 -> -1 * scrollY
            scrollY + dy > maxHeight - defLineHeight -> maxHeight - defLineHeight - scrollY//本应该减掉height ，这里是为了最后一行能滚到中间位置
            else -> dy
        }
        scrollBy(0, y)
        invalidate()
    }

    /**
     * 滑动结束后，让距离中线最近的一行歌词滑动到中线
     */
    private fun scrollToCenter() {
        mHandler.removeCallbacks(this)
        mHandler.postDelayed(this, 500L)
    }

    override fun run() {
        if ((!isPalying || scrollY <= maxHeight - 2 * defLineHeight)) {
            if (!isTouched) {
                isFling = false
                if (isPalying) scrollY += defLineHeight * 2 / 3

                val centerY = scrollY + defLineHeight //中线的实际位置
                centerPosition = centerY / defLineHeight
                var offset = centerY - (centerPosition * defLineHeight)

                offset = if ((offset < 0) || centerPosition == lyrics!!.size) { // 向上
                    -(offset + defLineHeight / 2)
                } else { //向下
                    (defLineHeight / 2 - offset)
                }
                scroller.startScroll(0, scrollY, 0, offset, 800)
                invalidate()

                centerPosition = if (centerPosition < 0) 0 else centerPosition
                centerPosition = if (centerPosition < lyrics!!.size) centerPosition else lyrics!!.size - 1
            }
            if (isPalying) mHandler.postDelayed(this, 2000)
        } else {
            stop()
        }
    }

    fun start() {
        if (!isPalying) {
            isPalying = true
            mHandler.post(this)
        }
    }

    private fun pause() {
        isTouched = true
        lastScrollY = scrollY
    }

    private fun resume() {
        if (!isPalying || Math.abs(lastClickY - currentY) < defLineHeight / 2) {
            isTouched = false
        } else {
            mHandler.removeCallbacks(this)
            mHandler.postDelayed({
                isTouched = false
                scrollY = lastScrollY - defLineHeight
                mHandler.post(this)
            }, 3000L)
        }

    }

    fun stop() {
        isPalying = false
        mHandler.removeCallbacks(this)
    }
}


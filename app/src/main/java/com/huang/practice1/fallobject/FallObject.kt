package com.huang.practice1.fallobject

import android.graphics.Bitmap
import android.graphics.Canvas
import java.util.*


/**
 * Des:
 * Created by huang on 2019/3/18 0018 12:59
 */

class FallObject(builder: Builder?, private var parentWidth: Int, private var parentHeight: Int) {
    private var initX: Int = 0
    private var initY: Int = 0
    private var random = Random()
    private var objectWidth: Float = 0.toFloat()//下落物体宽度
    private var objectHeight: Float = 0.toFloat()//下落物体高度

    var initSpeed: Int = 0//初始下降速度
    var initWindLevel: Int = 0//初始风力等级

    var presentX: Float = 0.toFloat()//当前位置X坐标
    var presentY: Float = 0.toFloat()//当前位置Y坐标
    var presentSpeed: Float = 0.toFloat()//当前下降速度
    private var angle: Float = 0.toFloat()//物体下落角度

    private var builder: Builder? = null
    private var bitmap: Bitmap? = null

    private var isSpeedRandom: Boolean = false//物体初始下降速度比例是否随机
    private var isSizeRandom: Boolean = false//物体初始大小比例是否随机
    private var isWindRandom: Boolean = false//物体初始风向和风力大小比例是否随机
    private var isWindChange: Boolean = false//物体下落过程中风向和风力是否产生随机变化

    private val HALF_PI = Math.PI.toFloat() / 2//π/2

    init {
        random = Random()
        initX = random.nextInt(parentWidth)
        initY = random.nextInt(parentHeight) - parentHeight
        presentX = initX.toFloat()
        presentY = initY.toFloat()

        this.builder = builder
        isSpeedRandom = builder!!.isSpeedRandom
        isSizeRandom = builder.isSizeRandom
        isWindRandom = builder.isWindRandom
        isWindChange = builder.isWindChange

        initSpeed = builder.initSpeed
        randomSpeed()
        randomSize()
        randomWind()
    }

    constructor(builder: Builder) : this(builder, 0, 0) {
        this.builder = builder
        initSpeed = builder.initSpeed
        bitmap = builder.bitmap

        isSpeedRandom = builder.isSpeedRandom
        isSizeRandom = builder.isSizeRandom
        isWindRandom = builder.isWindRandom
        isWindChange = builder.isWindChange
    }

    /**
     * 绘制物体对象
     * @param canvas
     */
    fun drawObject(canvas: Canvas) {
        moveObject()
        canvas.drawBitmap(bitmap!!, presentX, presentY, null)
    }

    /**
     * 移动物体对象
     */
    private fun moveObject() {
        moveX()
        moveY()
        if (presentY > parentHeight || presentX < -bitmap!!.width || presentX > parentWidth + bitmap!!.width) {
            reset()
        }
    }

    /**
     * X轴上的移动逻辑
     */
    private fun moveX() {
        presentX += (builder!!.defaultWindSpeed * Math.sin(angle.toDouble())).toFloat()
        if (isWindChange) {
            angle += ((if (random.nextBoolean()) -1 else 1).toFloat().toDouble() * Math.random() * 0.0025).toFloat()
        }
    }

    /**
     * Y轴上的移动逻辑
     */
    private fun moveY() {
        presentY += presentSpeed
    }

    /**
     * 重置object位置
     */
    private fun reset() {
        presentY = -objectHeight
        randomSpeed()//记得重置时速度也一起重置，这样效果会好很多
        randomWind()//记得重置一下初始角度，不然雪花会越下越少（因为角度累加会让雪花越下越偏）
    }

    /**
     * 随机物体初始下落速度
     */
    private fun randomSpeed() {
        if (isSpeedRandom) {
            presentSpeed = ((random.nextInt(3) + 1) * 0.1 + 1).toFloat() * initSpeed//这些随机数大家可以按自己的需要进行调整
        } else {
            presentSpeed = initSpeed.toFloat()
        }
    }

    /**
     * 随机物体初始大小比例
     */
    private fun randomSize() {
        if (isSizeRandom) {
            val r = (random.nextInt(10) + 1) * 0.1f
            val rW = r * builder!!.bitmap!!.width
            val rH = r * builder!!.bitmap!!.height
            bitmap = builder!!.changeBitmapSize(builder!!.bitmap!!, rW.toInt(), rH.toInt())
        } else {
            bitmap = builder!!.bitmap
        }
        objectWidth = bitmap!!.width.toFloat()
        objectHeight = bitmap!!.height.toFloat()
    }

    /**
     * 随机风的风向和风力大小比例，即随机物体初始下落角度
     */
    private fun randomWind() {
        angle = if (isWindRandom) {
            ((if (random.nextBoolean()) -1 else 1).toDouble() * Math.random() * initWindLevel.toDouble() / 50).toFloat()
        } else {
            initWindLevel.toFloat() / 50
        }

        //限制angle的最大最小值
        if (angle > HALF_PI) {
            angle = HALF_PI
        } else if (angle < -HALF_PI) {
            angle = -HALF_PI
        }
    }
}
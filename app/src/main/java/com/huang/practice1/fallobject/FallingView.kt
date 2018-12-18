package com.huang.practice1.fallobject

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.huang.practice1.R

/**
 * Des:仿雪花飘落（）
 * Created by huang on 2018/10/19 0019 16:39
 */
class FallingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var type: Int
    private var color: Int

    private var paint = Paint()
    private var fallView: BaseFallObject

    init {
        val arrays = context.obtainStyledAttributes(attrs, R.styleable.FallingView)
        type = arrays.getInt(R.styleable.FallingView_type, 0)
        color = arrays.getColor(R.styleable.FallingView_color, Color.WHITE)
        arrays.recycle()

        paint.color = color
        paint.isAntiAlias = true

        fallView = findFallView()
    }

    override fun onDraw(canvas: Canvas) {
        fallView.drawContent(canvas, paint)
    }

    private fun findFallView(): BaseFallObject {
        return when (type) {
            0 -> SnowView()
            else -> SnowView()
        }
    }
}

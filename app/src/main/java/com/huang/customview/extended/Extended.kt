package com.huang.customview.extended

import android.content.res.Resources
import android.graphics.Paint
import android.graphics.Rect
import com.huang.customview.netHotClock.Config

/**
 * 扩展获取绘制文字时在x轴上 垂直居中的y坐标
 */
fun Paint.getCenteredY(): Float {
    return this.fontSpacing / 2 - this.fontMetrics.bottom
}

/**
 * 扩展获取绘制文字时在x轴上 贴紧x轴的上边缘的y坐标
 */
fun Paint.getBottomedY(): Float {
    return -this.fontMetrics.bottom
}

/**
 * 扩展获取绘制文字时在x轴上 贴近x轴的下边缘的y坐标
 */
fun Paint.getToppedY(): Float {
    return -this.fontMetrics.ascent
}

fun Paint.getTextWidth(text: String): Float {
    if (text.isEmpty()) return 0f
    val rect = Rect()
    getTextBounds(text, 0, text.length, rect)
    return (rect.right - rect.left).toFloat()
}

fun Paint.getTextHeight(text: String): Float {
    if (text.isEmpty()) return 0f
    val rect = Rect()
    getTextBounds(text, 0, text.length, rect)
    return (rect.bottom - rect.top).toFloat()
}

fun dp2px(dpValue: Int, resources: Resources): Int {
    return (dpValue * resources.displayMetrics.density + 0.5).toInt()
}

fun sp2px(spValue: Int, resources: Resources): Float {
    return spValue * resources.displayMetrics.scaledDensity + 0.5f
}

fun Int.addLeadingZero(): String {
    return if (this < 10) "0$this" else this.toString()
}

/**
 * 数字转换文字
 */
fun Int.toText(): String {
    var result = ""
    val iArr = "$this".toCharArray().map { it.toString().toInt() }

    //处理 10，11，12.. 20，21，22.. 等情况
    if (iArr.size > 1) {
        if (iArr[0] != 1) {
            result += Config.NUMBER_TEXT_LIST[iArr[0]]
        }
        result += "十"
        if (iArr[1] > 0) {
            result += Config.NUMBER_TEXT_LIST[iArr[1]]
        }
    } else {
        result = Config.NUMBER_TEXT_LIST[iArr[0]]
    }

    return result
}

fun Int.getWeekText(): String {
    return when (this) {
        0 -> "周一"
        1 -> "周二"
        2 -> "周三"
        3 -> "周四"
        4 -> "周五"
        5 -> "周六"
        6 -> "周天"
        else -> "周一"
    }
}

fun List<String>.level(index: Int): String {
    return this[index]
}
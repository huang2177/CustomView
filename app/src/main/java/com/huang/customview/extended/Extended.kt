package com.huang.customview.extended

import android.content.res.Resources
import android.graphics.Paint
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

fun dp2px(dpValue: Float, resources: Resources): Float {
    val scale = resources.displayMetrics.density
    return dpValue * scale + 0.5f
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
package com.huang.customview.gaussianBlur

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

/**
 * Des:
 * Created by huang on 2018/11/7 0007 15:19
 */
class ImageUtils {

    companion object {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        fun gaussianBlur(radius: Int, original: Bitmap, context: Context): Bitmap {
            var renderScript: RenderScript? = RenderScript.create(context)
            val input = Allocation.createFromBitmap(renderScript, original)
            val output = Allocation.createTyped(renderScript, input.type)
            val scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
            scriptIntrinsicBlur.setRadius(radius.toFloat())
            scriptIntrinsicBlur.setInput(input)
            scriptIntrinsicBlur.forEach(output)
            output.copyTo(original)
            return original
        }
    }
}

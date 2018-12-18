package com.huang.practice1.danmu

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout

/**
 * Des:
 * Created by huang on 2018/11/29 0029 10:52
 */
class DanMuLayout(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs), Animation.AnimationListener {
    var views = ArrayList<DanMuView>()

    fun addDanMu(text: String) {
        val danMuView = DanMuView(text, context)
        addView(danMuView)
        moveUp(danMuView)
    }


    private fun moveUp(danMuView: DanMuView) {
        val anim = TranslateAnimation((0f), (0f), (0f), -height.toFloat())//平移动画  从0,0,平移到100,100
        anim.duration = 7000
        anim.fillAfter = true
        anim.isFillEnabled = true
        anim.setAnimationListener(this)

        danMuView.animation = anim
        anim.startNow()

        views.add(danMuView)
    }

    override fun onAnimationEnd(animation: Animation?) {
//        removeView(views[])
    }

    override fun onAnimationStart(animation: Animation?) {

    }

    override fun onAnimationRepeat(animation: Animation?) {
    }
}

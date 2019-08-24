package com.huang.customview.foldLine

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import com.huang.customview.R
import kotlinx.android.synthetic.main.fold_line_layout.*
import kotlinx.android.synthetic.main.gaussian_blur_layout.*

/**
 * Des:
 * Created by huang on 2018/11/7 0007 15:25
 */
class FoldLineActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fold_line_layout)

        foldLine.data = listOf(
            Pair("好", 0),
            Pair("好", 2),
            Pair("好", 4),
            Pair("中", 1),
            Pair("差", 3),
            Pair("差", 5),
            Pair("差", 6)
        )
    }
}

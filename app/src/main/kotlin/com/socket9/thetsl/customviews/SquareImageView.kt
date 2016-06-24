package com.socket9.thetsl.customviews

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by Euro (ripzery@gmail.com) on 4/27/16 AD.
 */

class SquareImageView : ImageView {

    constructor(context: Context) : super(context) {

    }


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec)
//        val height = measuredHeight
//        setMeasuredDimension(height, height)
    }
}
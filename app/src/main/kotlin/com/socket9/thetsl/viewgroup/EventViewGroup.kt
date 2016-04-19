package com.socket9.thetsl.viewgroup

import android.annotation.TargetApi
import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.socket9.thetsl.R
import com.socket9.thetsl.model.Model

class EventViewGroup : BaseCustomViewGroup {

    /** Variable zone **/
    lateinit private var viewContainer:View
    lateinit private var tvTag: TextView
    lateinit private var tvTitle: TextView
    lateinit private var ivPhoto: ImageView


    /** Override method zone **/
    constructor(context: Context) : super(context) {
        initInflate()
        initInstances()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initInflate()
        initInstances()
        initWithAttrs(attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initInflate()
        initInstances()
        initWithAttrs(attrs, defStyleAttr, 0)
    }

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initInflate()
        initInstances()
        initWithAttrs(attrs, defStyleAttr, defStyleRes)
    }

    private fun initInflate() {
        viewContainer = inflate(context, R.layout.viewgroup_news_event, this)
    }

    private fun initInstances() {
        // findViewById here
        tvTitle = viewContainer.findViewById(R.id.tvTitle) as TextView
        tvTag = viewContainer.findViewById(R.id.tvTag) as TextView
        ivPhoto = viewContainer.findViewById(R.id.ivPhoto) as ImageView

    }

    private fun initWithAttrs(attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    /** Method zone **/

    fun setModel(model: Model.NewsEventEntity){
        with(model){
            val isBlue = type.equals("service")
            tvTitle.text = titleEn
            tvTag.setBackgroundColor(ContextCompat.getColor(tvTitle.context, if(isBlue) R.color.colorPrimary else R.color.colorTextSecondary))
            tvTag.text = type
            Glide.with(ivPhoto.context).load(pic).into(ivPhoto)

        }
    }
}

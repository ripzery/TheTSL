package com.socket9.thetsl.viewgroup

import android.annotation.TargetApi
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.socket9.thetsl.R
import com.socket9.thetsl.model.Model
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.PublishSubject

class ContactViewGroup : BaseCustomViewGroup, AnkoLogger {

    /** Variable zone **/
    private val BASE_ID = 1000
    lateinit private var viewContainer: View
    lateinit private var tvContent: TextView
    lateinit private var tvTitle: TextView
    lateinit private var ivIcon: ImageView
    lateinit private var row: FrameLayout
    private var rowClickedObservable: PublishSubject<Int> = PublishSubject.create()


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
        viewContainer = inflate(context, R.layout.viewgroup_contact, this)
    }

    private fun initInstances() {
        // findViewById here
        tvTitle = viewContainer.findViewById(R.id.tvTitle) as TextView
        tvContent = viewContainer.findViewById(R.id.tvContent) as TextView
        ivIcon = viewContainer.findViewById(R.id.ivIcon) as ImageView
        row = viewContainer.findViewById(R.id.rootView) as FrameLayout

        row.setOnClickListener {
            rowClickedObservable.onNext(1)
        }

    }

    fun getRowClickedObservable(): Observable<Int> {
        return rowClickedObservable.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
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

    fun setModel(model: Model.ContactEntity) {
        with(model) {
            tvTitle.text = titleEn
            if(subTitle != null && id > BASE_ID){
                tvContent.text = subTitle
                tvContent.visibility = View.VISIBLE
            }
            if(icon != 0) ivIcon.setImageDrawable(ContextCompat.getDrawable(ivIcon.context, icon))
        }
    }
}

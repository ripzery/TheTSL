package com.socket9.thetsl.viewgroups

import android.annotation.TargetApi
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.socket9.thetsl.R
import com.socket9.thetsl.models.Model
import com.socket9.thetsl.utils.SharePref
import com.socket9.thetsl.views.SquareImageView
import org.jetbrains.anko.AnkoLogger
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.PublishSubject

class ServiceDetailViewGroup : BaseCustomViewGroup, AnkoLogger {

    /** Variable zone **/
    lateinit private var viewContainer: View
    lateinit private var tvDateTime: TextView
    lateinit private var tvStatus: TextView
    lateinit private var ivLogo: SquareImageView
    private var cardClickedObservable: PublishSubject<Int>? = null


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
        viewContainer = inflate(context, R.layout.viewgroup_service_detail, this)
    }

    private fun initInstances() {
        if (!isInEditMode) {
            cardClickedObservable = PublishSubject.create()
        }
        // findViewById here
        tvStatus = viewContainer.findViewById(R.id.tvStatus) as TextView
        tvDateTime = viewContainer.findViewById(R.id.tvDateTime) as TextView
        ivLogo = viewContainer.findViewById(R.id.ivIcon) as SquareImageView

    }

    fun getCardClickedObservable(): Observable<Int> {
        return cardClickedObservable?.subscribeOn(AndroidSchedulers.mainThread())!!.observeOn(AndroidSchedulers.mainThread())
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

    fun setModel(model: Model.ServiceTrackingStatus) {

        //  TODO: SetModel for service view group
        tvStatus.text = model.getStatus()
        tvDateTime.text = "${ if(SharePref.isEnglish()) "Update : "  else "อัพเดทล่าสุด : "}${model.dateFinish.substring(0, model.dateFinish.length - 3)} "
    }

}

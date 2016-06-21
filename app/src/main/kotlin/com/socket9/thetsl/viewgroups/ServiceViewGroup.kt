package com.socket9.thetsl.viewgroups

import android.annotation.TargetApi
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.socket9.thetsl.R
import com.socket9.thetsl.models.Model
import com.socket9.thetsl.utils.SharePref
import kotlinx.android.synthetic.main.viewgroup_service.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.PublishSubject

class ServiceViewGroup : BaseCustomViewGroup, AnkoLogger {

    /** Variable zone **/
    lateinit private var viewContainer: View
    lateinit private var tvServiceName: TextView
    lateinit private var tvStatus: TextView
    lateinit private var tvLastUpdate: TextView
    lateinit private var ivLogo: ImageView
    lateinit private var cardService: CardView
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
        viewContainer = inflate(context, R.layout.viewgroup_service, this)
    }

    private fun initInstances() {
        if (!isInEditMode) {
            cardClickedObservable = PublishSubject.create()
        }
            // findViewById here
        tvServiceName = viewContainer.findViewById(R.id.tvServiceName) as TextView
        tvStatus = viewContainer.findViewById(R.id.tvStatus) as TextView
        tvLastUpdate = viewContainer.findViewById(R.id.tvLastUpdate) as TextView
        ivLogo = viewContainer.findViewById(R.id.ivLogo) as ImageView
        cardService = viewContainer.findViewById(R.id.cardService) as CardView

        cardService.setOnClickListener {
            cardClickedObservable?.onNext(1)
        }

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

    fun setModel(model: Model.ServiceBookingEntity) {

    //  TODO: SetModel for service view group
        tvServiceName.text = model.getService()
        tvStatus.text = if(model.dateConfirm.isNullOrEmpty()) context.getString(R.string.fragment_new_booking_status_booking_pending) else context.getString(R.string.fragment_new_booking_status_booking_confirmed)
        tvLastUpdate.text = "${if(SharePref.isEnglish()) "Update : " else "อัพเดทล่าสุด : "}${model.dateBooking.substring(0, model.dateBooking.length - 3)}"
        tvLicensePlate.text = model.licensePlate
        ivArrow.visibility = View.GONE
    }

    fun setModel(model: Model.ServiceTrackingEntity) {

    //  TODO: SetModel for service view group
        info { model }
        val lastStatus = model.detail[model.detail.size - 1]
        tvServiceName.text = if (model.getServiceType().isNullOrBlank()) "Untitled service" else model.getServiceType()
        tvStatus.text = lastStatus.getStatus()
        tvLastUpdate.text = "${if(SharePref.isEnglish()) "Update : " else "อัพเดทล่าสุด : "} ${lastStatus.dateFinish.substring(0, lastStatus.dateFinish.length - 3)}"
        tvLicensePlate.text = model.licensePlate
        ivArrow.visibility = View.VISIBLE

    }
}

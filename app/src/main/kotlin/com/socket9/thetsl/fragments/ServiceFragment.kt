package com.socket9.thetsl.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.socket9.thetsl.R
import com.socket9.thetsl.activities.NewBookingActivity
import com.socket9.thetsl.activities.ServiceDetailActivity
import com.socket9.thetsl.adapter.ServiceAdapter
import com.socket9.thetsl.extensions.applyTransition
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.models.Model
import com.socket9.thetsl.utils.DialogUtil
import com.trello.rxlifecycle.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_service.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import rx.Subscription

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */
class ServiceFragment : RxFragment(), AnkoLogger, ServiceAdapter.ServiceInteractionListener {
    /** Variable zone **/
    lateinit var param1: String
    private var serviceAddedType = listOf("")
    //    private var orderType = "first"
    private var loadDataSubscription: Subscription? = null
    private var dialog: ProgressDialog? = null
    private var serviceAdapter: ServiceAdapter? = null
    private var serviceList: Model.ServiceBookingList? = null
    private var trackingList: Model.ServiceTrackingList? = null
    private var isServiceHistory: Boolean = false
    private var progressDialog: ProgressDialog? = null
    private var loadNewBookingSubscription: Subscription? = null

    /** Static method zone **/
    companion object {
        val ARG_1 = "ARG_1"

        fun newInstance(param1: String): ServiceFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val serviceFragment: ServiceFragment = ServiceFragment()
            serviceFragment.arguments = bundle
            return serviceFragment
        }

    }

    /** Activity method zone  **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            /* if newly created */
            param1 = arguments.getString(ARG_1)
        }

        serviceAddedType = listOf(getString(R.string.booking_service_type_new_booking_service), getString(R.string.booking_service_type_known_service_number))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_service, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    override fun onPause() {
        super.onPause()
        loadDataSubscription?.unsubscribe()
        loadNewBookingSubscription?.unsubscribe()
        dialog?.dismiss()
        progressDialog?.dismiss()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            NewBookingActivity.NEW_BOOKING_ACTIVITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (isServiceHistory) {
                        loadHistoryData("first")
                    } else {
                        loadData("first")
                    }
                }
            }
            NewBookingActivity.NEW_BOOKING_KNOWN_SERVICE_ACTIVITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (isServiceHistory) {
                        loadHistoryData("first")
                    } else {
                        loadData("first")
                    }
                }
            }
        }
    }

    /** Method zone **/

    private fun initInstance() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        initListener()

        /* Loading data */
        loadData(if (isServiceHistory) "last" else "first")
    }

    private fun initListener() {
        btnAddService.setOnClickListener {
            selector(getString(R.string.booking_service_type_title), serviceAddedType) { position ->
                when (position) {
                    0 -> {
                        loadNewBookingDataThen {
                            val intent = Intent(activity, NewBookingActivity::class.java)
                            intent.putExtra(NewBookingActivity.EXTRA_IS_NEW_BOOKING, true)
                            intent.putExtra(NewBookingActivity.EXTRA_NEW_BOOKING_DATA, it)
                            startActivityForResult(intent, NewBookingActivity.NEW_BOOKING_ACTIVITY)
                            applyTransition(R.anim.activity_forward_enter, R.anim.activity_forward_exit)
                        }
                    }
                    1 -> {
                        val intent = Intent(activity, NewBookingActivity::class.java)
                        intent.putExtra(NewBookingActivity.EXTRA_IS_NEW_BOOKING, false)
                        startActivityForResult(intent, NewBookingActivity.NEW_BOOKING_KNOWN_SERVICE_ACTIVITY)
                        applyTransition(R.anim.activity_forward_enter, R.anim.activity_forward_exit)
                    }
                }
            }
        }

        btnLeft.setOnClickListener {
            btnLeft.setTextColor(ContextCompat.getColor(activity, R.color.colorTextPrimary))
            btnLeft.background = ContextCompat.getDrawable(activity, R.drawable.button_corner_left)
            btnRight.setTextColor(ContextCompat.getColor(activity, R.color.colorTextSecondary))
            btnRight.background = ContextCompat.getDrawable(activity, R.drawable.button_corner_right_white)

            /* Loading new booking by first order */
            loadData("first")

            isServiceHistory = false
        }

        btnRight.setOnClickListener {
            btnLeft.setTextColor(ContextCompat.getColor(activity, R.color.colorTextSecondary))
            btnLeft.background = ContextCompat.getDrawable(activity, R.drawable.button_corner_left_white)
            btnRight.setTextColor(ContextCompat.getColor(activity, R.color.colorTextPrimary))
            btnRight.background = ContextCompat.getDrawable(activity, R.drawable.button_corner_right)

            /* Loading new booking by last order */
            loadHistoryData("first")

            isServiceHistory = true
        }


    }

    /* Load booking service data, then load service tracking data, and push to the stack. */
    private fun loadData(orderType: String) {
        dialog = indeterminateProgressDialog(R.string.dialog_progress_service_list_content, R.string.dialog_progress_title)
        dialog?.setCancelable(false)
        dialog?.show()

        // TODO: Load data from tracking and booking

        loadDataSubscription = HttpManager.getServiceBookingList(orderType)
//                .bindToLifecycle(this)
                .flatMap {
                    serviceList = it
                    HttpManager.getServiceTrackingList(orderType)
                }
//                .bindToLifecycle(this)
                .subscribe ({

                    dialog?.dismiss()
                    trackingList = it

                    if (serviceAdapter == null) {
                        serviceAdapter = ServiceAdapter(serviceList!!.data, trackingList!!.data)
                    } else {
                        serviceAdapter!!.serviceBookingList = serviceList!!.data
                        serviceAdapter!!.serviceTrackingList = trackingList!!.data
                        serviceAdapter!!.notifyDataSetChanged()
                    }

                    tvEmpty.visibility = if (serviceList!!.data.size + it.data.size > 0) View.GONE else View.VISIBLE

                    recyclerView.adapter = serviceAdapter

                    serviceAdapter?.setListener(this)

                }, { error ->
                    error.printStackTrace()
                    dialog?.dismiss()
                    if (error.message != null && error.message!!.contains("Internal Server")) {
                        toast(error.message!!)
                    } else {
                        toast(getString(R.string.toast_internet_connection_problem))
                    }
                })
    }

    /* Load booking service history data, then load service tracking history data, and push to the stack. */
    private fun loadHistoryData(orderType: String) {
        dialog = indeterminateProgressDialog(R.string.dialog_progress_service_list_history_content, R.string.dialog_progress_title)
        dialog?.setCancelable(false)
        dialog?.show()

        // TODO: Load data from tracking and booking

        loadDataSubscription = HttpManager.getServiceBookingHistoryList(orderType)
//                .bindToLifecycle(this)
                .flatMap {
                    serviceList = it
                    HttpManager.getServiceTrackingHistoryList(orderType)
                }
//                .bindToLifecycle(this)
                .subscribe ({

                    dialog?.dismiss()
                    trackingList = it

                    if (serviceAdapter == null) {
                        serviceAdapter = ServiceAdapter(serviceList!!.data, trackingList!!.data)
                    } else {
                        serviceAdapter!!.serviceBookingList = serviceList!!.data
                        serviceAdapter!!.serviceTrackingList = trackingList!!.data
                        serviceAdapter!!.notifyDataSetChanged()
                    }

                    tvEmpty.visibility = if (serviceList!!.data.size + it.data.size > 0) View.GONE else View.VISIBLE

                    recyclerView.adapter = serviceAdapter

                    serviceAdapter?.setListener(this)

                }, { error ->
                    error.printStackTrace()
                    dialog?.dismiss()
                    if (error.message!!.contains("Internal Server")) {
                        toast(error.message!!)
                    } else {
                        toast(getString(R.string.toast_internet_connection_problem))
                    }
                })
    }

    private fun loadNewBookingDataThen(action: (Model.ServiceBasicData) -> Unit) {
        progressDialog = indeterminateProgressDialog(R.string.dialog_progress_service_content, R.string.dialog_progress_title)
        progressDialog?.setCancelable(false)
        progressDialog?.show()

        /* Loading spinner data */
        loadNewBookingSubscription = HttpManager.getServiceBasicData()
//                .bindToLifecycle(this)
                .subscribe ({
                    action(it)
                    progressDialog?.dismiss()
                    //            setSpinnerData(it)
                }, { error ->
                    progressDialog?.dismiss()
                    info { error }
                    toast(getString(R.string.toast_internet_connection_problem))
                })
    }

    /* show dialog if coming from gcm */
    fun showConfirmationDialog() {
        val dialog = DialogUtil.getServiceBookingConfirmationDialog(activity)
        val view = dialog.customView!!

        with(view) {
            val tvType = find<TextView>(R.id.tvType)
            val tvDate = find<TextView>(R.id.tvDate)
            val tvTime = find<TextView>(R.id.tvTime)
            val tvBranch = find<TextView>(R.id.tvBranch)
        }

        dialog.show()

    }

    /** Listener zone **/
    override fun onCardClicked(position: Int) {

        /* click on tracking list card */
        if (position >= serviceList!!.data.size) {
            startActivity<ServiceDetailActivity>(ServiceDetailActivity.ARG_1 to trackingList!!.data[position - serviceList!!.data.size])
        } else {
            toast(getString(R.string.toast_waiting_confirmation))
        }
    }
}
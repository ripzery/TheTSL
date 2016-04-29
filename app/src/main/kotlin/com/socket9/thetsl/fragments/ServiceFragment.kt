package com.socket9.thetsl.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.thetsl.R
import com.socket9.thetsl.activities.NewBookingActivity
import com.socket9.thetsl.adapter.ServiceAdapter
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.models.Model
import kotlinx.android.synthetic.main.fragment_service.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import rx.Subscription

/**
 * Created by Euro on 3/10/16 AD.
 */
class ServiceFragment : Fragment(), AnkoLogger, ServiceAdapter.ServiceInteractionListener {
    /** Variable zone **/
    lateinit var param1: String
    private val serviceAddedType = listOf("New booking service", "Known service number")
    //    private var orderType = "first"
    private var loadDataSubscription: Subscription? = null
    private var dialog: ProgressDialog? = null
    private var serviceAdapter: ServiceAdapter? = null
    private var serviceList: Model.ServiceBookingList? = null
    private var trackingList: Model.ServiceTrackingList? = null

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
        dialog?.dismiss()

    }

    /** Method zone **/

    private fun initInstance() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        initListener()

        /* Loading data */
        loadData("first")
    }

    private fun initListener() {
        btnAddService.setOnClickListener {
            selector("Which one do you want to add?", serviceAddedType) { position ->
                when (position) {
                    0 -> startActivity<NewBookingActivity>("isNewBooking" to true)
                    1 -> startActivity<NewBookingActivity>("isNewBooking" to false)
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
        }

        btnRight.setOnClickListener {
            btnLeft.setTextColor(ContextCompat.getColor(activity, R.color.colorTextSecondary))
            btnLeft.background = ContextCompat.getDrawable(activity, R.drawable.button_corner_left_white)
            btnRight.setTextColor(ContextCompat.getColor(activity, R.color.colorTextPrimary))
            btnRight.background = ContextCompat.getDrawable(activity, R.drawable.button_corner_right)

            /* Loading new booking by last order */
            loadData("last")
        }


    }

    private fun loadData(orderType: String) {
        dialog = indeterminateProgressDialog(R.string.dialog_progress_service_list_content, R.string.dialog_progress_title)
        dialog?.setCancelable(false)
        dialog?.show()

        // TODO: Load data from tracking and booking

        loadDataSubscription = HttpManager.getServiceBookingList(orderType)
            .flatMap {
                serviceList = it
                HttpManager.getServiceTrackingList(orderType)
            }.subscribe ({
                dialog?.dismiss()
                trackingList = it

                if (serviceAdapter == null) {
                    serviceAdapter = ServiceAdapter(serviceList!!.data, trackingList!!.data)
                } else {
                    serviceAdapter!!.serviceBookingList = serviceList!!.data
                    serviceAdapter!!.serviceTrackingList = trackingList!!.data
                    serviceAdapter!!.notifyDataSetChanged()
                }

                recyclerView.adapter = serviceAdapter

                serviceAdapter?.setListener(this)

            }, { error ->
                error.printStackTrace()
                dialog?.dismiss()
            })
    }

    /** Listener zone **/
    override fun onCardClicked(position: Int) {
        toast("$position")
    }
}
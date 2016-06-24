package com.socket9.thetsl.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.thetsl.R
import com.socket9.thetsl.activities.CarDetailActivity
import com.socket9.thetsl.activities.NewCarTrackingActivity
import com.socket9.thetsl.activities.ServiceDetailActivity
import com.socket9.thetsl.adapter.CarTrackingAdapter
import com.socket9.thetsl.extensions.applyTransition
import com.socket9.thetsl.extensions.toast
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.models.Model
import com.trello.rxlifecycle.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_service.*
import kotlinx.android.synthetic.main.layout_empty_states.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.startActivity
import rx.Subscription

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */
class CarTrackingFragment : RxFragment(), AnkoLogger, CarTrackingAdapter.CarTrackingInteractionListener {


    /** Variable zone **/
    lateinit var param1: String
    private var loadDataSubscription: Subscription? = null
    private var dialog: ProgressDialog? = null
    private var carAdapter: CarTrackingAdapter? = null
    private var carList: Model.CarTrackingList? = null


    /** Static method zone **/
    companion object {
        val ARG_1 = "ARG_1"

        fun newInstance(param1: String): CarTrackingFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val carTrackingFragment: CarTrackingFragment = CarTrackingFragment()
            carTrackingFragment.arguments = bundle
            return carTrackingFragment
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
        val rootView: View = inflater!!.inflate(R.layout.fragment_car_tracking, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    override fun onPause() {
        super.onPause()
        loadDataSubscription?.unsubscribe()
    }

    override fun onResume() {
        super.onResume()
    }

    /** Method zone **/

    private fun initInstance() {
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        loadData("first")

        initListener()
    }

    private fun initListener() {
        btnAddService.setOnClickListener {
            startActivity<NewCarTrackingActivity>()
            applyTransition(R.anim.activity_forward_enter, R.anim.activity_forward_exit)
        }

//        btnLeft.setOnClickListener {
//            btnLeft.setTextColor(ContextCompat.getColor(activity, R.color.colorTextPrimary))
//            btnLeft.background = ContextCompat.getDrawable(activity, R.drawable.button_corner_left)
//            btnRight.setTextColor(ContextCompat.getColor(activity, R.color.colorTextSecondary))
//            btnRight.background = ContextCompat.getDrawable(activity, R.drawable.button_corner_right_white)
//
//            /* Loading new booking by first order */
//            loadData("first")
//        }
//
//        btnRight.setOnClickListener {
//            btnLeft.setTextColor(ContextCompat.getColor(activity, R.color.colorTextSecondary))
//            btnLeft.background = ContextCompat.getDrawable(activity, R.drawable.button_corner_left_white)
//            btnRight.setTextColor(ContextCompat.getColor(activity, R.color.colorTextPrimary))
//            btnRight.background = ContextCompat.getDrawable(activity, R.drawable.button_corner_right)
//
//            /* Loading new booking by last order */
//            loadData("last")
//        }
    }

    private fun loadData(orderType: String) {
        dialog = indeterminateProgressDialog(R.string.dialog_progress_service_list_content, R.string.dialog_progress_title)
        dialog?.setCancelable(false)
        dialog?.show()

        loadDataSubscription = HttpManager.getCarTrackingList()
//                .bindToLifecycle(this)
                .subscribe({
                    carList = it

                    dialog?.dismiss()
                    info { it.data }
                    tvEmpty.visibility = if (it.data.size == 0) View.VISIBLE else View.GONE

                    if (carAdapter == null) {
                        carAdapter = CarTrackingAdapter(it.data)
                    } else {
                        carAdapter!!.carTrackingList = it.data
                        carAdapter!!.notifyDataSetChanged()
                    }

                    recyclerView.adapter = carAdapter

                    carAdapter?.setListener(this)

                }, { error ->
                    dialog?.dismiss()
                    toast(getString(R.string.toast_internet_connection_problem))
                    error.printStackTrace()
                })
    }

    /** Listener zone **/

    override fun onCardClicked(position: Int, sharedView: View) {
        val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedView, sharedView.transitionName)
        val intent: Intent = Intent(context, CarDetailActivity::class.java)
        intent.putExtra(ServiceDetailActivity.ARG_1, carList!!.data[position])
        startActivity(intent, options.toBundle())
    }
}
package com.socket9.thetsl.ui.main.tracking.sale

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.applyTransition
import com.socket9.thetsl.extensions.toast
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.models.Model
import com.trello.rxlifecycle.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_new_car_tracking.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.warn

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */
class NewCarTrackingFragment : RxFragment(), AnkoLogger {

    /** Variable zone **/
    lateinit var param1: String
    var progressDialog: ProgressDialog? = null


    /** Static method zone **/
    companion object {
        val ARG_1 = "ARG_1"

        fun newInstance(param1: String): NewCarTrackingFragment {
            val bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val newCarTrackingFragment: NewCarTrackingFragment = NewCarTrackingFragment()
            newCarTrackingFragment.arguments = bundle
            return newCarTrackingFragment
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
        val rootView: View = inflater!!.inflate(R.layout.fragment_new_car_tracking, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    override fun onPause() {
        super.onPause()
        progressDialog?.dismiss()
    }

    /** Method zone **/

    private fun initInstance() {

        btnSubmit.setOnClickListener {
            val preemptionNumber = etPreemptionNumber.text.toString()
            val citizenId = etCitizenId.text.toString()
            val valid: Boolean = preemptionNumber.length > 0 && citizenId.length == 13

            if (valid) {
                progressDialog = indeterminateProgressDialog(R.string.dialog_progress_save_car_tracking_content, R.string.dialog_progress_title)
                progressDialog?.setCancelable(false)

                HttpManager.newCarTracking(preemptionNumber, citizenId)
                        .compose(bindToLifecycle<Model.CarTrackingSaveList>())
                        .subscribe ({
                            progressDialog?.dismiss()
                            if (it.result) {
                                toast(getString(R.string.toast_save_car_tracking_successful))
                                activity.setResult(Activity.RESULT_OK)
                                activity.finish()
                                applyTransition(R.anim.activity_backward_enter, R.anim.activity_backward_exit)
                            } else {
                                toast(it.message!!)
                            }
                        }, { error ->
                            progressDialog?.dismiss()
                            warn { error }
                            toast(getString(R.string.toast_internet_connection_problem))
                        })
            } else {
                toast(getString(R.string.fragment_new_car_tracking_toast_error))
            }
        }
    }

    private fun testCitizenId(citizenID: String): Boolean {
//        for(i=0, sum=0; i < 12; i++)
//        sum += parseFloat(id.charAt(i))*(13-i); if((11-sum%11)%10!=parseFloat(id.charAt(12)))
//            return false; return true;
        var sum: Float = 0f
        for (i in 0..11) {
            sum += citizenID[i].toFloat() * (13 - i)

        }

        if ((11 - sum % 11) % 10 != citizenID[12].toFloat()) {
            return false
        }

        return true

    }

    private fun validateCitizenID(citizen: String): Boolean {
        var citizenID: Long = citizen.toLong()
        var base = 100000000000L
        var currentBase: Int
        var sum = 0
        for (i in 12 downTo 1) {
            currentBase = Math.floor((citizenID / base).toDouble()).toInt()
            citizenID -= currentBase * base
            sum += currentBase * i
            base /= 10
        }
        val checkBit = (11 - sum % 11) % 10
        return checkBit == citizen[12].toInt()
    }
}
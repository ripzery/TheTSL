package com.socket9.thetsl.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.thetsl.R
import com.socket9.thetsl.managers.HttpManager
import kotlinx.android.synthetic.main.fragment_known_service_number.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.toast

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */
class KnownServiceNumberFragment : Fragment(), AnkoLogger {

    /** Variable zone **/
    lateinit var param1: String
    private var progressDialog: ProgressDialog? = null


    /** Static method zone **/
    companion object{
        val ARG_1 = "ARG_1"

        fun newInstance(param1:String) : KnownServiceNumberFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val knownServiceNumberFragment: KnownServiceNumberFragment = KnownServiceNumberFragment()
            knownServiceNumberFragment.arguments = bundle
            return knownServiceNumberFragment
        }

    }

    /** Activity method zone  **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            /* if newly created */
            param1 = arguments.getString(ARG_1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_known_service_number, container, false)

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

    private fun initInstance(){
        btnSubmit.setOnClickListener {
            progressDialog = indeterminateProgressDialog(R.string.dialog_progress_service_car_tracking_content, R.string.dialog_progress_title)
            progressDialog?.setCancelable(false)

            HttpManager.serviceCarTracking(etServiceNumber.text.toString(), etTrackingId.text.toString())
                    .subscribe({
                        progressDialog?.dismiss()
                        info { it }
                        if (it.result) {
                            toast("Add successfully")
                            activity.finish()
                        } else {
                            toast(it.message!!)
                        }
                    }, {
                        it.printStackTrace()
                        progressDialog?.dismiss()

                    })
        }
    }
}
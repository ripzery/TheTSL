package com.socket9.thetsl.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.thetsl.R

/**
 * Created by Euro on 3/10/16 AD.
 */
class NewCarTrackingFragment : Fragment(){

    /** Variable zone **/
    lateinit var param1: String


    /** Static method zone **/
    companion object{
        val ARG_1 = "ARG_1"

        fun newInstance(param1:String) : NewCarTrackingFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val newCarTrackingFragment: NewCarTrackingFragment = NewCarTrackingFragment()
            newCarTrackingFragment.arguments = bundle
            return newCarTrackingFragment
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
        val rootView: View = inflater!!.inflate(R.layout.fragment_new_car_tracking, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    /** Method zone **/

    private fun initInstance(){

    }
}
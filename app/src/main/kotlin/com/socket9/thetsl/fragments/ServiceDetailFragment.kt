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
class ServiceDetailFragment : Fragment(){

    /** Variable zone **/
    lateinit var param1: String


    /** Static method zone **/
    companion object{
        val ARG_1 = "ARG_1"

        fun newInstance(param1:String) : ServiceDetailFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val serviceDetailFragment: ServiceDetailFragment = ServiceDetailFragment()
            serviceDetailFragment.arguments = bundle
            return serviceDetailFragment
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
        val rootView: View = inflater!!.inflate(R.layout.fragment_service_detail, container, false)

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
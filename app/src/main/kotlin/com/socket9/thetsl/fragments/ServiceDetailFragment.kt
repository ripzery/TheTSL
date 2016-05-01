package com.socket9.thetsl.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.thetsl.R
import com.socket9.thetsl.adapter.ServiceDetailAdapter
import com.socket9.thetsl.models.Model
import kotlinx.android.synthetic.main.fragment_service_detail.*

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */
class ServiceDetailFragment : Fragment(){

    /** Variable zone **/
    lateinit var serviceTrackingEntity: Model.ServiceTrackingEntity


    /** Static method zone **/
    companion object{
        val ARG_1 = "ARG_1"

        fun newInstance(param1: Model.ServiceTrackingEntity) : ServiceDetailFragment {
            var bundle: Bundle = Bundle()
            bundle.putParcelable(ARG_1, param1)
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
            serviceTrackingEntity = arguments.getParcelable(ARG_1)
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
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        val adapter = ServiceDetailAdapter(serviceTrackingEntity.detail)
        recyclerView.adapter = adapter

        serviceViewGroup.setModel(serviceTrackingEntity)
    }
}
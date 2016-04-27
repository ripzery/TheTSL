package com.socket9.thetsl.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.thetsl.R
import com.socket9.thetsl.activities.NewBookingActivity
import com.socket9.thetsl.adapter.ServiceAdapter
import kotlinx.android.synthetic.main.fragment_service.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

/**
 * Created by Euro on 3/10/16 AD.
 */
class ServiceFragment : Fragment(), AnkoLogger{

    /** Variable zone **/
    lateinit var param1: String
    private val serviceAddedType = listOf("New booking service", "Known service number")


    /** Static method zone **/
    companion object{
        val ARG_1 = "ARG_1"

        fun newInstance(param1:String) : ServiceFragment {
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
        if(savedInstanceState == null){
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

    /** Method zone **/

    private fun initInstance(){
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager


        btnAddService.setOnClickListener {
            selector("Which one do you want to add?", serviceAddedType) { position ->
                when(position){
                    0 -> startActivity<NewBookingActivity>("isNewBooking" to true)
                    1 -> startActivity<NewBookingActivity>("isNewBooking" to false)
                }
//                toast("Selected ${serviceAddedType[position]}")
            }
        }

        // TODO : fetch data and init adapter
//        recyclerView.adapter = ServiceAdapter()
    }
}
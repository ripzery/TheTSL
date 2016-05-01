package com.socket9.thetsl.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.thetsl.R
import kotlinx.android.synthetic.main.fragment_event.*
import org.jetbrains.anko.AnkoLogger

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */
class NewsFragment : Fragment(), AnkoLogger{

    /** Variable zone **/
    lateinit var param1: String


    /** Static method zone **/
    companion object{
        val ARG_1 = "ARG_1"

        fun newInstance(param1:String) : NewsFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val newsFragment: NewsFragment = NewsFragment()
            newsFragment.arguments = bundle
            return newsFragment
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
        val rootView: View = inflater!!.inflate(R.layout.fragment_news, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()

    }

    /** Method zone **/

    private fun initInstance(){
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
//        newsAdapter = EventAdapter(mutableListOf())
//        recyclerView.adapter = eventAdapter
        getNews()
    }

    private fun getNews() {
//        getListEventSubscription = HttpManager.getListEvent()
//                .subscribe {
//                    info{ "Hello ${it.toString()}" }
//                    newsAdapter?.eventList = it.data
//                }
    }
}
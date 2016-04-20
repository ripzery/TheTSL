package com.socket9.thetsl.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.thetsl.R
import com.socket9.thetsl.activities.NewsEventActivity
import com.socket9.thetsl.adapter.EventAdapter
import com.socket9.thetsl.extensions.toast
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.model.Model
import kotlinx.android.synthetic.main.fragment_event.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.startActivity
import rx.Subscription

/**
 * Created by Euro on 3/10/16 AD.
 */
class NewsEventFragment : Fragment(), AnkoLogger, EventAdapter.EventInteractionListener {

    /** Variable zone **/
    private var getListNewsEventSubscription: Subscription? = null
    private var eventNewsAdapter: EventAdapter? = null
    var isNews: Boolean = true


    /** Static method zone **/
    companion object {
        val MODE_IS_NEWS = "IS_NEWS_MODE"

        fun newInstance(isModeNews: Boolean): NewsEventFragment {
            var bundle: Bundle = Bundle()
            bundle.putBoolean(MODE_IS_NEWS, isModeNews)
            val newsEventFragment: NewsEventFragment = NewsEventFragment()
            newsEventFragment.arguments = bundle
            return newsEventFragment
        }

    }

    /** Activity method zone  **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            /* if newly created */
            isNews = arguments.getBoolean(MODE_IS_NEWS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_event, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()

    }

    override fun onPause() {
        super.onPause()
        getListNewsEventSubscription?.unsubscribe()
    }

    /** Method zone **/

    private fun initInstance() {
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        eventNewsAdapter = EventAdapter(mutableListOf())
        recyclerView.adapter = eventNewsAdapter
        eventNewsAdapter?.setListener(this)
        getListNewsEvent()
    }

    private fun getListNewsEvent() {
        when (isNews) {
            true -> {
                getListNewsEventSubscription = HttpManager.getListNews()
                        .subscribe {
                            eventNewsAdapter?.setList(it.data)
                        }
            }
            false -> {
                getListNewsEventSubscription = HttpManager.getListEvent()
                        .subscribe {
                            eventNewsAdapter?.setList(it.data)
                        }
            }
        }

    }

    /** Listener zone **/
    override fun onClickedEvent(index: Int, model: Model.NewsEventEntity) {
        startActivity<NewsEventActivity>("id" to model.id, "isNews" to isNews)
    }
}
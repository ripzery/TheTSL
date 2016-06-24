package com.socket9.thetsl.ui.main.tracking.service


import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.socket9.thetsl.R
import com.socket9.thetsl.activities.ToolbarActivity
import com.socket9.thetsl.extensions.replaceFragment
import com.socket9.thetsl.models.Model
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
* Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
*/

class ServiceDetailActivity : ToolbarActivity() {

    /** Variable zone **/
    private var serviceDetailFragment: ServiceDetailFragment? = null
    private var serviceTrackingEntity: Model.ServiceTrackingEntity? = null

    /** Static method zone **/
    companion object {
        val ARG_1 = "trackingEntity"
    }

    /** Lifecycle  zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_detail)
        initInstance()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    /** Method zone **/

    private fun initInstance() {
        //        setToolbar()
        setupToolbar("Service Detail")

        /* get intent serviceTrackingEntity */
        serviceTrackingEntity = intent.getParcelableExtra<Model.ServiceTrackingEntity>(ARG_1)

        serviceDetailFragment = ServiceDetailFragment.newInstance(serviceTrackingEntity!!)

        replaceFragment(fragment = serviceDetailFragment!!)
    }

    /** Listener zone **/

}
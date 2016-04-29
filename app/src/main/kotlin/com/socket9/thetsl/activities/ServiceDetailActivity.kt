package com.socket9.thetsl.activities


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.replaceFragment
import com.socket9.thetsl.fragments.ServiceDetailFragment

/**
 * Created by Euro on 3/10/16 AD.
 */

class ServiceDetailActivity : AppCompatActivity() {

    /** Variable zone **/
    private var serviceDetailFragment: ServiceDetailFragment? = null

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

    /** Method zone **/

    private fun initInstance() {
        setToolbar()
        serviceDetailFragment = ServiceDetailFragment.newInstance("ServiceDetailFragment")

        replaceFragment(fragment = serviceDetailFragment!!)
    }

    private fun setToolbar() {
        supportActionBar?.title = "Service detail"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    /** Listener zone **/

}
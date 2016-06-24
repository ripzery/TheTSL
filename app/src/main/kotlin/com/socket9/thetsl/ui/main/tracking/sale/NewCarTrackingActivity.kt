package com.socket9.thetsl.ui.main.tracking.sale


import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.socket9.thetsl.R
import com.socket9.thetsl.activities.ToolbarActivity
import com.socket9.thetsl.extensions.applyTransition
import com.socket9.thetsl.extensions.replaceFragment
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
* Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
*/

class NewCarTrackingActivity : ToolbarActivity() {

    /** Variable zone **/
    var newCarTrackingFragment: NewCarTrackingFragment? = null

    /** Lifecycle  zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_car_tracking)
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
        when (item!!.itemId) {
            android.R.id.home -> {
                finish()
                applyTransition(R.anim.activity_backward_enter, R.anim.activity_backward_exit)
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        applyTransition(R.anim.activity_backward_enter, R.anim.activity_backward_exit)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    /** Method zone **/

    private fun initInstance() {
        setupToolbar(getString(R.string.fragment_car_tracking_title))

        newCarTrackingFragment = NewCarTrackingFragment.newInstance("NewCarTracking")
        replaceFragment(fragment = newCarTrackingFragment!!)
        //        replaceFragment()
    }

    /** Listener zone **/

}
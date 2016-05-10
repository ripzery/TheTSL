package com.socket9.thetsl.activities


import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.replaceFragment
import com.socket9.thetsl.fragments.NewCarTrackingFragment
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */

class NewCarTrackingActivity : AppCompatActivity() {

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
                return true
            }
        }
        return false
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    /** Method zone **/

    private fun initInstance() {
        setToolbar()

        newCarTrackingFragment = NewCarTrackingFragment.newInstance("NewCarTracking")
        replaceFragment(fragment = newCarTrackingFragment!!)
        //        replaceFragment()
    }

    private fun setToolbar() {
        supportActionBar?.title = "Car Tracking"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /** Listener zone **/

}
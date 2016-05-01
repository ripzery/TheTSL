package com.socket9.thetsl.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.replaceFragment
import com.socket9.thetsl.fragments.KnownServiceNumberFragment
import com.socket9.thetsl.fragments.NewBookingFragment

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */

class NewBookingActivity : AppCompatActivity(){

    /** Variable zone **/
    lateinit var knownServiceNumberFragment: KnownServiceNumberFragment
    lateinit var newBookingFragment: NewBookingFragment


    /** Lifecycle  zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_booking)
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
        when(item?.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
        }

        return false
    }

    /** Method zone **/

    private fun initInstance() {
        val isNewBooking: Boolean = intent.getBooleanExtra("isNewBooking", true)

        initToolbar(isNewBooking)

        newBookingFragment = NewBookingFragment.newInstance("NewBookingFragment")
        knownServiceNumberFragment = KnownServiceNumberFragment.newInstance("KnownServiceNumberFragment")


        replaceFragment(fragment = if(isNewBooking) newBookingFragment else knownServiceNumberFragment)
    }

    private fun initToolbar(newBooking: Boolean) {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if(newBooking) "New Booking" else "Service Tracking"
    }

    /** Listener zone **/

}
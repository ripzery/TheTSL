package com.socket9.thetsl.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.replaceFragment
import com.socket9.thetsl.fragments.KnownServiceNumberFragment
import com.socket9.thetsl.fragments.NewBookingFragment
import com.socket9.thetsl.utils.DialogUtil

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */

class NewBookingActivity : AppCompatActivity(){

    /** Variable zone **/
    lateinit private var knownServiceNumberFragment: KnownServiceNumberFragment
    lateinit private var newBookingFragment: NewBookingFragment

    /** Static method zone **/
    companion object {
        val NEW_BOOKING_ACTIVITY = 1000
    }

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
                if (newBookingFragment.isModified()) {
                    DialogUtil.getUpdateProfileDialog(this, MaterialDialog.SingleButtonCallback { dialog, which -> finish() }).show()
                    return true
                } else {
                    finish()
                    return true
                }
            }
        }

        return false
    }

    override fun onBackPressed() {
        if (newBookingFragment.isModified()) {
            DialogUtil.getUpdateProfileDialog(this, MaterialDialog.SingleButtonCallback { dialog, which -> finish() }).show()
        } else {
            finish()
        }
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
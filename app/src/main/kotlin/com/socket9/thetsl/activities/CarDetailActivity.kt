package com.socket9.thetsl.activities


import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.replaceFragment
import com.socket9.thetsl.fragments.CarDetailFragment
import com.socket9.thetsl.fragments.ServiceDetailFragment
import com.socket9.thetsl.models.Model
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */

class CarDetailActivity : ToolbarActivity() {

    /** Variable zone **/
    private var carDetailFragment: CarDetailFragment? = null
    private var carTrackingEntity: Model.CarTrackingEntity? = null

    /** Static method zone **/
    companion object{
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
                supportFinishAfterTransition()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
    
    /** Method zone **/

    private fun initInstance() {
        //        setToolbar()
        setupToolbar(getString(R.string.fragment_car_tracking_detail_title))

        /* get intent serviceTrackingEntity */
        carTrackingEntity = intent.getParcelableExtra<Model.CarTrackingEntity>(ARG_1)

        carDetailFragment = CarDetailFragment.newInstance(carTrackingEntity!!)

        replaceFragment(fragment = carDetailFragment!!)
    }

    /** Listener zone **/

}
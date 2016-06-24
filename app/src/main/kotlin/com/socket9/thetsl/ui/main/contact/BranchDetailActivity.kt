package com.socket9.thetsl.ui.main.contact


import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.socket9.thetsl.R
import com.socket9.thetsl.activities.ToolbarActivity
import com.socket9.thetsl.extensions.applyTransition
import com.socket9.thetsl.extensions.plainText
import com.socket9.thetsl.models.Model
import kotlinx.android.synthetic.main.activity_branch_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.email
import org.jetbrains.anko.makeCall
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */

class BranchDetailActivity : ToolbarActivity(), OnMapReadyCallback, AnkoLogger {
    /** Variable zone **/
    lateinit private var supportMapsFragment: SupportMapFragment
    lateinit private var mMap: GoogleMap
    lateinit private var contact: Model.ContactEntity
    private var contactLatLng: LatLng? = null

    /** Lifecycle  zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_branch_detail)
        initInstance()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_place_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                applyTransition(R.anim.activity_backward_enter, R.anim.activity_backward_exit)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        applyTransition(R.anim.activity_backward_enter, R.anim.activity_backward_exit)
    }

    override fun onMapReady(map: GoogleMap?) {
        mMap = map!!

        ivTransparent.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_MOVE -> {
                        scrollView.requestDisallowInterceptTouchEvent(true)
                        return false
                    }
                    MotionEvent.ACTION_DOWN -> {
                        scrollView.requestDisallowInterceptTouchEvent(true)
                        return false
                    }
                    MotionEvent.ACTION_UP -> {
                        scrollView.requestDisallowInterceptTouchEvent(false)
                        return true
                    }
                    else -> return false
                }
            }

        })

        if (contactLatLng != null) {
            mMap.addMarker(MarkerOptions().position(contactLatLng).title(contact.getTitle()))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(contactLatLng, 15f))
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    /** Method zone **/

    private fun initInstance() {
        contact = intent.getParcelableExtra<Model.ContactEntity>("contact")
        if (contact.lat != null && contact.lng != null) contactLatLng = LatLng(contact.lat!!, contact.lng!!)

        //        setToolbar()
        setupToolbar(contact.getTitle())
        setData()

        supportMapsFragment = mapFragment as SupportMapFragment
        supportMapsFragment.getMapAsync(this)

        tvPhone.setOnClickListener { makeCall(contact.phone!!.plainText().split(" ")[0]) }
        tvEmail.setOnClickListener { email(contact.email!!.plainText()) }
        ivPhone.setOnClickListener {
            makeCall(contact.phone!!.plainText().split(" ")[0])
        }
        ivEmail.setOnClickListener {
            email(contact.email!!.plainText())
        }
    }

    private fun setData() {
        tvPhone.text = contact.phone?.plainText()
        tvEmail.text = contact.email?.plainText()?.replace("\r", "")?.replace("\n", "")?.replace("\t", "")

        if (contact.businessHours != null) tvHours.text = Html.fromHtml(contact.businessHours?.replace("\r", "")?.replace("\n", "")?.replace("\t", ""))
        tvAddress.text = contact.address?.plainText()?.replace("\r", "")?.replace("\n", "")?.replace("\t", "")
    }
    /** Listener zone **/

}
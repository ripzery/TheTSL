package com.socket9.thetsl.ui.main.contact


import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jakewharton.rxbinding.view.RxView
import com.socket9.thetsl.R
import com.socket9.thetsl.activities.ToolbarActivity
import com.socket9.thetsl.extensions.applyTransition
import com.socket9.thetsl.extensions.plainText
import com.socket9.thetsl.extensions.toast
import com.socket9.thetsl.models.Model
import com.tbruyelle.rxpermissions.RxPermissions
import kotlinx.android.synthetic.main.activity_branch_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.email
import org.jetbrains.anko.info
import org.jetbrains.anko.makeCall
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */

class BranchDetailActivity : ToolbarActivity(), OnMapReadyCallback, AnkoLogger {
    /** Variable zone **/
    private val ID_CHAENGWATTANA = 1
    private val ID_THONGLOR = 3
    private val ID_PHUKET = 4
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

//        ivTransparent.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                when (event?.action) {
//                    MotionEvent.ACTION_MOVE -> {
//                        scrollView.requestDisallowInterceptTouchEvent(true)
//                        return false
//                    }
//                    MotionEvent.ACTION_DOWN -> {
//                        scrollView.requestDisallowInterceptTouchEvent(true)
//                        return false
//                    }
//                    MotionEvent.ACTION_UP -> {
//                        scrollView.requestDisallowInterceptTouchEvent(false)
//                        return true
//                    }
//                    else -> return false
//                }
//            }
//
//        })

        if (contactLatLng != null) {
            mMap.addMarker(MarkerOptions().position(contactLatLng!!).title(contact.getTitle()))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(contactLatLng, 15f))
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    /** Method zone **/

    private fun initInstance() {
        contact = intent.getParcelableExtra<Model.ContactEntity>("contact")
        info { contact }
        if (contact.lat != null && contact.lng != null) contactLatLng = LatLng(contact.lat!!, contact.lng!!)

        setupToolbar(contact.getTitle())
        setData()

//        supportMapsFragment = mapFragment as SupportMapFragment
//        supportMapsFragment.getMapAsync(this)

//        tvPhone.setOnClickListener { makeCall(contact.phone!!.plainText().split(" ")[0]) }
        tvEmail.setOnClickListener { email(contact.email!!.plainText()) }

        ivLocation.setOnClickListener { openGMap(contactLatLng?.latitude.toString(), contactLatLng?.longitude.toString()) }

        RxView.clicks(tvPhone)
                .compose(RxPermissions.getInstance(this).ensure(Manifest.permission.CALL_PHONE))
                .subscribe { granted ->
                    if (granted) {
                        makeCall(contact.phone!!.plainText().split(" ")[0])
                    } else {
                        toast(getString(R.string.permission_phone_required))
                    }

                }

        RxView.clicks(ivPhone)
                .compose(RxPermissions.getInstance(this).ensure(Manifest.permission.CALL_PHONE))
                .subscribe { granted ->
                    if (granted) {
                        makeCall(contact.phone!!.plainText().split(" ")[0])
                    } else {
                        toast(getString(R.string.permission_phone_required))
                    }

                }
//        ivPhone.setOnClickListener {

//            makeCall(contact.phone!!.plainText().split(" ")[0])
//        }

        ivEmail.setOnClickListener {
            email(contact.email!!.plainText())
        }
    }

    private fun openGMap(lat: String?, lng: String?) {
        if (lat != null && lng != null) {
            val location = "geo:$lat,$lng(${contact.getTitle()})"
            val marker = "$lat,$lng(${contact.getTitle()})"
            val uriString = "$location?q=${Uri.encode(marker)}&z=16"
            val gmmIntentUri = Uri.parse(uriString)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.`package` = "com.google.android.apps.maps"
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            }
        } else {
            toast(getString(R.string.toast_branch_detail_location_null))
        }
    }

    private fun setData() {
        tvPhone.text = contact.phone?.plainText()
        tvEmail.text = contact.email?.plainText()?.replace("\r", "")?.replace("\n", "")?.replace("\t", "")

        if (contact.businessHours != null) tvHours.text = Html.fromHtml(contact.businessHours?.replace("\r", "")?.replace("\n", "")?.replace("\t", ""))
        tvAddress.text = contact.address?.plainText()?.replace("\r", "")?.replace("\n", "")?.replace("\t", "")

        when (contact.id) {
            ID_CHAENGWATTANA ->
                Glide.with(this).load(R.drawable.changwattana).into(ivTransparent)
            ID_PHUKET ->
                Glide.with(this).load(R.drawable.phuket).into(ivTransparent)
            ID_THONGLOR ->
                Glide.with(this).load(R.drawable.thonglor).into(ivTransparent)
        }

    }
    /** Listener zone **/

}
package com.socket9.thetsl.activities


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.plainText
import com.socket9.thetsl.extensions.replaceFragment
import com.socket9.thetsl.models.Model
import kotlinx.android.synthetic.main.activity_branch_detail.*

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */

class BranchDetailActivity : AppCompatActivity(), OnMapReadyCallback {
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

        when(item?.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(map: GoogleMap?) {
        mMap = map!!

        if(contactLatLng != null){
            mMap.addMarker(MarkerOptions().position(contactLatLng).title(contact.titleEn))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(contactLatLng, 15f))
        }
    }

    /** Method zone **/

    private fun initInstance() {
        setToolbar()
        contact = intent.getParcelableExtra<Model.ContactEntity>("contact")
        if(contact.lat != null && contact.lng != null)
            contactLatLng = LatLng(contact.lat!!, contact.lng!!)

        setData()

        supportMapsFragment = SupportMapFragment.newInstance()
        replaceFragment(R.id.mapContainer, supportMapsFragment)
        supportMapsFragment.getMapAsync(this)
    }

    private fun setData() {
        toolbarTitle.text = contact.titleEn
        toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        tvPhone.text = contact.phone?.plainText()
        tvEmail.text = contact.email?.plainText()
        if(contact.businessHours != null) tvHours.text = Html.fromHtml(contact.businessHours)
        tvAddress.text = contact.address?.plainText()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val tvTitle: TextView = toolbar.findViewById(R.id.toolbarTitle) as TextView
        tvTitle.text = getString(R.string.toolbar_branch_detail)

    }

    /** Listener zone **/

}
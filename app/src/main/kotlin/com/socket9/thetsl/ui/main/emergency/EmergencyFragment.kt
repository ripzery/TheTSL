package com.socket9.thetsl.ui.main.emergency

import android.Manifest
import android.app.ProgressDialog
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.replaceFragment
import com.socket9.thetsl.extensions.requestPhonePermission
import com.socket9.thetsl.extensions.toast
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.models.Model
import com.socket9.thetsl.utils.DialogUtil
import com.socket9.thetsl.utils.SharePref
import com.tbruyelle.rxpermissions.RxPermissions
import com.trello.rxlifecycle.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_emergency.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.makeCall
import pl.charmas.android.reactivelocation.ReactiveLocationProvider

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */
class EmergencyFragment : RxFragment(), OnMapReadyCallback, AnkoLogger {


    /** Variable zone **/
    lateinit var param1: String
    private var isMechanic = true
    private var myPosition: LatLng? = null
    private var requestEmergency = "MECHANIC"
    private var progressDialog: ProgressDialog ? = null
    lateinit private var supportMapsFragment: SupportMapFragment
    lateinit private var locationProvider: ReactiveLocationProvider
    lateinit private var mMap: GoogleMap
    private val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            .setNumUpdates(1)
            .setInterval(1000);


    /** Static method zone **/
    companion object {
        val ARG_1 = "ARG_1"
        val REQUEST_CODE_LOCATION_SETTING = 100
        val MECHANIC = "REQUIRE MECHANIC"
        val TOWCAR = "TOW CAR"

        fun newInstance(param1: String): EmergencyFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val emergencyFragment: EmergencyFragment = EmergencyFragment()
            emergencyFragment.arguments = bundle
            return emergencyFragment
        }

    }

    /** Activity method zone  **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            /* if newly created */
            param1 = arguments.getString(ARG_1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_emergency, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()


    }

    override fun onMapReady(map: GoogleMap?) {
        mMap = map!!
        mMap.isMyLocationEnabled = true
        startSubscribeUserLastKnownLocation()
        startSubscribeUserLocation()

    }

    override fun onStop() {
        super.onStop()
        try {
            progressDialog?.dismiss()
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    /** Method zone **/

    private fun initInstance() {
        supportMapsFragment = SupportMapFragment.newInstance()
        replaceFragment(R.id.mapContainer, supportMapsFragment)

        RxPermissions.getInstance(context).request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe({ granted ->
                    info{ granted }
                    if (granted) {
                        supportMapsFragment.getMapAsync(this)

                        locationProvider = ReactiveLocationProvider(activity)

                        locationProvider.checkLocationSettings(LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                                .setAlwaysShow(true).build())
                                .subscribe({
                                    val status = it.status
                                    if (status.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                                        try {
                                            status.startResolutionForResult(activity, REQUEST_CODE_LOCATION_SETTING)
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                }, { error ->
                                    toast(getString(R.string.toast_unknown_error_try_again))

                                })
                    } else {
                        toast(getString(R.string.permission_location_required))
                    }
                }, {
                    it.printStackTrace()
                })



        initEmergencyIcon()

        ivTowCar.setOnClickListener {
            if (isMechanic) {
                if (SharePref.isEnglish()) {
                    ivTowCar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.towcar_active_en))
                    ivMechanic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mechanic_en))
                } else {
                    ivTowCar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.towcar_active_th))
                    ivMechanic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mechanic_th))
                }

                requestEmergency = TOWCAR
                isMechanic = false
            }
        }

        ivMechanic.setOnClickListener {
            if (!isMechanic) {
                if (SharePref.isEnglish()) {
                    ivMechanic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mechanic_active_en))
                    ivTowCar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.towcar_en))
                } else {
                    ivMechanic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mechanic_active_th))
                    ivTowCar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.towcar_th))
                }

                requestEmergency = MECHANIC
                isMechanic = true
            }
        }

        btnRequest.setOnClickListener {
            info { myPosition }

            val profile = SharePref.getProfile()

            if (profile.data?.phone.isNullOrEmpty()) {

                val requiredPhoneDialog = DialogUtil.getRequiredPhoneDialog(context, MaterialDialog.InputCallback { dialog, input ->
                    progressDialog = indeterminateProgressDialog(getString(R.string.dialog_progress_emergency_call_content), getString(R.string.dialog_progress_title))
                    progressDialog?.setCancelable(false)
                    progressDialog?.show()
                    HttpManager.updatePhone(input.toString())
                            .compose(bindToLifecycle<Model.BaseModel>())
                            .subscribe({
                                progressDialog?.dismiss()
                                if (it.result) {
                                    toast(it.message)

                                    requestPhonePermission {
                                        emergencyCall()
                                    }

                                } else {
                                    toast(it.message)
                                }
                            }, { error ->
                                progressDialog?.dismiss()
                                info { error }
                                toast(getString(R.string.toast_internet_connection_problem))
                            })
                })

                requiredPhoneDialog.show()

            } else {
                requestPhonePermission {
                    emergencyCall()
                }
            }

        }

    }

    private fun initEmergencyIcon() {
        if (SharePref.isEnglish()) {
            ivMechanic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mechanic_active_en))
            ivTowCar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.towcar_en))
        } else {
            ivTowCar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.towcar_th))
            ivMechanic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mechanic_active_th))
        }
    }

    private fun emergencyCall() {
        progressDialog = indeterminateProgressDialog(getString(R.string.dialog_progress_emergency_call_content), getString(R.string.dialog_progress_title))
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        HttpManager.emergencyCall(myPosition?.latitude.toString(), myPosition?.longitude.toString(), requestEmergency)
                .compose(this.bindToLifecycle<Model.BaseModel>())
                .subscribe({
                    progressDialog?.dismiss()
                    toast(it.message)

                    if (it.result) {
                        info { it }
                        showCallDialog()
                    }

                }, { error ->
                    progressDialog?.dismiss()
                    toast(error.message.toString())
                    showCallDialog()
                })
    }

    private fun showCallDialog() {
        val callusDialog = DialogUtil.getCallUsDialog(activity, MaterialDialog.SingleButtonCallback { dialog, which ->
            makeCall("022699999")
        })

        callusDialog.setCancelable(false)
        callusDialog.show()
    }

    fun userNotEnabledLocation() {
        toast(getString(R.string.toast_enable_location))
        //        activity.finish()
    }

    fun userEnabledLocation() {
        startSubscribeUserLocation()
    }

    private fun startSubscribeUserLocation() {
        locationProvider.getUpdatedLocation(locationRequest)
                .compose(bindToLifecycle<Location>())
                .subscribe({
                    moveToLocation(mMap, it)
                }, {
                    toast(getString(R.string.toast_unknown_error_try_again))
                })
    }

    private fun startSubscribeUserLastKnownLocation() {
        ReactiveLocationProvider(activity)
                .lastKnownLocation
                .compose(bindToLifecycle<Location>())
                .subscribe({
                    moveToLocation(mMap, it)
                }, {
                    toast(getString(R.string.toast_unknown_error_try_again))
                })
    }

    private fun moveToLocation(map: GoogleMap?, location: Location) {
        myPosition = LatLng(location.latitude, location.longitude)
        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(with(location) { LatLng(latitude, longitude) }, 17.0f))

    }

}
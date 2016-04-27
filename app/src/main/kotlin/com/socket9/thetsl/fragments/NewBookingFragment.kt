package com.socket9.thetsl.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import com.codetroopers.betterpickers.datepicker.DatePickerBuilder
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment
import com.codetroopers.betterpickers.timepicker.TimePickerBuilder
import com.socket9.thetsl.R
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.models.Model
import kotlinx.android.synthetic.main.fragment_new_booking_service.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.toast
import rx.Observable
import rx.Subscription
import java.util.*
import kotlin.reflect.KProperty

/**
 * Created by Euro on 3/10/16 AD.
 */
class NewBookingFragment : Fragment(), AnkoLogger{

    /** Variable zone **/
    lateinit var param1: String
    var modelList: MutableList<Model.BaseModel>? = null
    private var dataSubscription: Subscription? = null
    private var progressDialog: ProgressDialog? = null
    private var basicData: Model.ServiceBasicData? = null
    private var datePicker: CalendarDatePickerDialogFragment? = null
    private var timePicker: RadialTimePickerDialogFragment? = null
    private var dateTime: String = ""


    /** Static method zone **/
    companion object{
        val ARG_1 = "ARG_1"

        fun newInstance(param1:String) : NewBookingFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val newBookingFragment: NewBookingFragment = NewBookingFragment()
            newBookingFragment.arguments = bundle
            return newBookingFragment
        }

    }

    /** Activity method zone  **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            /* if newly created */
            param1 = arguments.getString(ARG_1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_new_booking_service, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    override fun onPause() {
        super.onPause()
        progressDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        dataSubscription?.unsubscribe()
    }

    /** Method zone **/

    private fun initInstance(){

        datePicker = CalendarDatePickerDialogFragment()
            .setOnDateSetListener({ calendarDatePickerDialogFragment, year, monthOfYear, dayOfMonth ->
                info { "$dayOfMonth/$monthOfYear/$year" }
                dateTime = "$year-${String.format("%02d", monthOfYear)}-${String.format("%02d", dayOfMonth)} "
                timePicker?.show(childFragmentManager, "TimePicker")
            })
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setDoneText("Done")
            .setCancelText("Cancel")

        timePicker = RadialTimePickerDialogFragment()
            .setOnTimeSetListener({ radialTimePickerDialogFragment, hour, minute ->
                info { "$hour:$minute" }
                dateTime += "${String.format("%02d", hour)}:${String.format("%02d", minute)}:00"
                btnDate.text = dateTime
            })
            .setDoneText("Done")
            .setCancelText("Cancel")

        progressDialog = indeterminateProgressDialog(R.string.dialog_progress_service_content, R.string.dialog_progress_title)
        progressDialog?.setCancelable(false)

        /* Loading spinner data */
        dataSubscription = loadData().subscribe {
            basicData = it
            progressDialog?.dismiss()
            setSpinnerData(it)
        }

        initListener()
    }

    private fun initListener() {
        btnBook.setOnClickListener {

            try {
                val newBooking = Model.NewBooking(etLicensePlate.text.toString(),
                        basicData!!.data.modelCategories[spinnerModel.selectedItemPosition-1],
                        dateTime,
                        basicData!!.data.serviceTypes[spinnerType.selectedItemPosition-1],
                        basicData!!.data.branches[spinnerBranch.selectedItemPosition-1],
                        etMoreInfo.text.toString(),
                        "087-1234567")

                info { newBooking }

                book(newBooking)
            }catch(e: Exception){

                toast("An error has occurred")

                e.printStackTrace()
            }


        }

        btnDate.setOnClickListener {
            datePicker?.show(childFragmentManager, "DatePicker")
        }
    }

    private fun book(newBooking: Model.NewBooking) {

        progressDialog = indeterminateProgressDialog(R.string.dialog_progress_save_service_content, R.string.dialog_progress_title)
        progressDialog?.setCancelable(false)

        HttpManager.bookService(newBooking)
            .subscribe {
                progressDialog?.dismiss()
                toast("Service has been booked successful")
                info { it }
                activity.finish()
            }
    }

    private fun setSpinnerData(it: Model.ServiceBasicData) {
        val branchAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, getListNameFromBasicData(it.data.branches))
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBranch.adapter = branchAdapter
        val modelAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, getListNameFromBasicData(it.data.modelCategories))
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerModel.adapter = modelAdapter
        val typeAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, getListNameFromBasicData(it.data.serviceTypes))
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = typeAdapter
    }

    private fun loadData(): Observable<Model.ServiceBasicData> {
        return HttpManager.getServiceBasicData()
    }

    private fun getListNameFromBasicData(dataList: MutableList<Model.BasicData>) : List<String>{
        var list: MutableList<String> = mutableListOf()
        for(item in dataList){
            list.add(item.nameEn)
        }
        return list
    }
}


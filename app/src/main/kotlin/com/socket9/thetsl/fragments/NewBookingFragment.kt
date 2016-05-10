package com.socket9.thetsl.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment
import com.jakewharton.rxbinding.widget.RxTextView
import com.socket9.thetsl.R
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.models.Model
import kotlinx.android.synthetic.main.fragment_new_booking_service.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.toast
import rx.Subscription
import java.util.*

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */
class NewBookingFragment : Fragment(), AnkoLogger {

    /** Variable zone **/
    lateinit var param1: String
    var modelList: MutableList<Model.BaseModel>? = null
    private var dataSubscription: Subscription? = null
    private var progressDialog: ProgressDialog? = null
    private var basicData: Model.ServiceBasicData? = null
    private var datePicker: CalendarDatePickerDialogFragment? = null
    private var timePicker: RadialTimePickerDialogFragment? = null
    private var dateTime: String = ""
    private var isModified: Boolean = false
    private var isDateSet: Boolean = false
    private var isTimeSet: Boolean = false
    private var choosenModelIndex = -1
    private var choosenTypeIndex = -1
    private var choosenBranchIndex = -1

    /** Static method zone **/
    companion object {
        val ARG_1 = "ARG_1"

        fun newInstance(param1: String): NewBookingFragment {
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
        if (savedInstanceState == null) {
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

    private fun initInstance() {

        val now: Calendar = Calendar.getInstance()
        val minDate: MonthAdapter.CalendarDay = MonthAdapter.CalendarDay(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))

        datePicker = CalendarDatePickerDialogFragment()
                .setDateRange(minDate, null)
                .setOnDateSetListener({ calendarDatePickerDialogFragment, year, monthOfYear, dayOfMonth ->
                    info { "$dayOfMonth/${monthOfYear + 1}/$year" }
                    dateTime = "$year-${String.format("%02d", monthOfYear + 1)}-${String.format("%02d", dayOfMonth)} "
                    isDateSet = true
                    btnDate.text = dateTime
                    //                    timePicker?.show(childFragmentManager, "TimePicker")
                })
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setDoneText("Done")
                .setCancelText("Cancel")

        timePicker = RadialTimePickerDialogFragment()
                .setOnTimeSetListener({ radialTimePickerDialogFragment, hour, minute ->
                    info { "$hour:$minute" }
                    btnTime.text = "${String.format("%02d", hour)}:${String.format("%02d", minute)}:00"
                    dateTime += "${String.format("%02d", hour)}:${String.format("%02d", minute)}:00"
                    isModified = true
                    isTimeSet = true
                })
                .setStartTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE))
                .setDoneText("Done")
                .setCancelText("Cancel")

        initListener()

        loadData()

    }

    private fun initListener() {
        btnBook.setOnClickListener {

            try {
                val newBooking = Model.NewBooking(etLicensePlate.text.toString(),
                        basicData!!.data.modelCategories[choosenModelIndex],
                        dateTime,
                        basicData!!.data.serviceTypes[choosenTypeIndex],
                        basicData!!.data.branches[choosenBranchIndex],
                        etMoreInfo.text.toString(),
                        "087-1234567")

                info { newBooking }
                book(newBooking)

            } catch(e: Exception) {

                toast(getString(R.string.toast_fill_all_field))

                e.printStackTrace()
            }

        }

        btnDate.setOnClickListener {
            datePicker?.show(childFragmentManager, "DatePicker")
        }

        btnTime.setOnClickListener { timePicker?.show(childFragmentManager, "TimePicker") }

        RxTextView.textChangeEvents(etLicensePlate)
                .skip(1)
                .subscribe {
                    isModified = true
                }
        RxTextView.textChangeEvents(etMoreInfo)
                .skip(1)
                .subscribe {
                    isModified = true
                }

        //        spinnerBranch.onItemSelectedListener = spinSelectedListener
        //        spinnerModel.onItemSelectedListener = spinSelectedListener
        //        spinnerType.onItemSelectedListener = spinSelectedListener

        btnChooseModel.setOnClickListener {
            selector(getString(R.string.fragment_new_booking_service_select_model_hint), getListNameFromBasicData(basicData!!.data.modelCategories), {
                btnChooseModel.text = basicData!!.data.modelCategories[it].getName()
                choosenModelIndex = it
                isModified = true
            })
        }

        btnChooseType.setOnClickListener {
            selector(getString(R.string.fragment_new_booking_service_type_hint), getListNameFromBasicData(basicData!!.data.serviceTypes), {
                btnChooseType.text = basicData!!.data.serviceTypes[it].getName()
                choosenTypeIndex = it
                isModified = true
            })
        }

        btnChooseBranch.setOnClickListener {
            selector(getString(R.string.fragment_new_booking_service_branch_hint), getListNameFromBasicData(basicData!!.data.branches), {
                btnChooseBranch.text = basicData!!.data.branches[it].getName()
                choosenBranchIndex = it
                isModified = true
            })
        }

    }

    fun isModified(): Boolean {
        return isModified
    }

    private fun book(newBooking: Model.NewBooking) {

        progressDialog = indeterminateProgressDialog(R.string.dialog_progress_save_service_content, R.string.dialog_progress_title)
        progressDialog?.setCancelable(false)

        HttpManager.bookService(newBooking)
                .subscribe ({
                    progressDialog?.dismiss()
                    toast(getString(R.string.toast_service_booking_successful))
                    info { it }
                    activity.setResult(Activity.RESULT_OK)
                    activity.finish()
                }, {
                    progressDialog?.dismiss()
                    toast(getString(R.string.toast_internet_connection_problem))
                })
    }


    //    private fun setSpinnerData(it: Model.ServiceBasicData) {
    //        val branchAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, getListNameFromBasicData(it.data.branches))
    //        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    //        spinnerBranch.adapter = branchAdapter
    ////        val modelAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, getListNameFromBasicData(it.data.modelCategories))
    ////        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    ////        spinnerModel.adapter = modelAdapter
    //        val typeAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, getListNameFromBasicData(it.data.serviceTypes))
    //        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    //        spinnerType.adapter = typeAdapter
    //    }

    private fun loadData() {
        progressDialog = indeterminateProgressDialog(R.string.dialog_progress_service_content, R.string.dialog_progress_title)
        progressDialog?.setCancelable(false)
        progressDialog?.show()

        /* Loading spinner data */
        dataSubscription = HttpManager.getServiceBasicData().subscribe ({
            basicData = it
            progressDialog?.dismiss()
            //            setSpinnerData(it)
        }, { error ->
            progressDialog?.dismiss()
            toast(getString(R.string.toast_internet_connection_problem))
        })
    }

    private fun getListNameFromBasicData(dataList: MutableList<Model.BasicData>): List<String> {
        var list: MutableList<String> = mutableListOf()
        for (item in dataList) {
            list.add(item.getName())
        }
        return list
    }
}


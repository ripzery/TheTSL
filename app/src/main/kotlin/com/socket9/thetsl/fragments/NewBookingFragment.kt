package com.socket9.thetsl.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.afollestad.materialdialogs.MaterialDialog
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment
import com.jakewharton.rxbinding.widget.RxTextView
import com.rengwuxian.materialedittext.MaterialEditText
import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.validatePhone
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.models.Model
import com.socket9.thetsl.utils.DialogUtil
import com.socket9.thetsl.utils.SharePref
import com.trello.rxlifecycle.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_new_booking_service.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.toast
import rx.Subscription
import java.util.*

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */
class NewBookingFragment : RxFragment(), AnkoLogger {

    /** Variable zone **/
    lateinit var param1: String
    private var dataSubscription: Subscription? = null
    private var progressDialog: ProgressDialog? = null
    lateinit var basicData: Model.ServiceBasicData
    private var datePicker: CalendarDatePickerDialogFragment? = null
    private var timePicker: RadialTimePickerDialogFragment? = null
    private var date: String = ""
    private var time: String = ""
    private var isModified: Boolean = false
    private var isDateSet: Boolean = false
    private var isTimeSet: Boolean = false
    private var chosenBrandIndex = -1
    private var chosenModelIndex = -1
    private var chosenTypeIndex = -1
    private var chosenBranchIndex = -1

    /** Static method zone **/
    companion object {
        val ARG_1 = "ARG_1"
        val ARG_2 = "ARG_2"

        fun newInstance(param1: String, param2: Model.ServiceBasicData?): NewBookingFragment {
            val bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            bundle.putParcelable(ARG_2, param2)
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
            basicData = arguments.getParcelable<Model.ServiceBasicData>(ARG_2)
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
                    date = "$year-${String.format("%02d", monthOfYear + 1)}-${String.format("%02d", dayOfMonth)} "
                    isDateSet = true
                    btnDate.text = date
                    //                    timePicker?.show(childFragmentManager, "TimePicker")
                })
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setDoneText("Done")
                .setCancelText("Cancel")

        timePicker = RadialTimePickerDialogFragment()
                .setOnTimeSetListener({ radialTimePickerDialogFragment, hour, minute ->
                    info { "$hour:$minute" }
                    btnTime.text = "${String.format("%02d", hour)}:${String.format("%02d", minute)}:00"
                    time = "${String.format("%02d", hour)}:${String.format("%02d", minute)}:00"
                    isModified = true
                    isTimeSet = true
                })
                .setStartTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE))
                .setDoneText("Done")
                .setCancelText("Cancel")

        initListener()
    }

    private fun showCustomInputDialog(hint: String, action: (name: String) -> Unit) {
        val customInput: View = LayoutInflater.from(context).inflate(R.layout.dialog_enter_other, null)

        val alertInput = alert {
            customView(customInput)
        }.show()

        val etOther = customInput.find<MaterialEditText>(R.id.etOther)
        val btnEnter = customInput.find<Button>(R.id.btnEnter)
        etOther.hint = hint
        etOther.floatingLabelText = hint
        btnEnter.setOnClickListener {
            action(etOther.text.toString())
            alertInput.dismiss()
        }
    }

    fun isModified(): Boolean {
        return isModified
    }

    private fun book(newBooking: Model.NewBooking) {

        progressDialog = indeterminateProgressDialog(R.string.dialog_progress_save_service_content, R.string.dialog_progress_title)
        progressDialog?.setCancelable(false)

        HttpManager.bookService(newBooking)
//                .bindToLifecycle(this)
                .subscribe ({
                    if (it.result) {
                        progressDialog?.dismiss()
                        toast(getString(R.string.toast_service_booking_successful))
                        info { it }
                        activity.setResult(Activity.RESULT_OK)
                        activity.finish()
                    } else {
                        progressDialog?.dismiss()
                        toast(it.message)
                        warn { it }
                    }

                }, {
                    progressDialog?.dismiss()
                    toast(getString(R.string.toast_internet_connection_problem))
                })
    }


//    private fun setSpinnerData(it: Model.ServiceBasicData) {
//        val branchAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, getListNameFromBasicData(it.data.branches))
//        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerBranch.adapter = branchAdapter
//        val modelAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, getListNameFromBasicData(it.data.modelCategories))
//        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerModel.adapter = modelAdapter
//        val typeAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, getListNameFromBasicData(it.data.serviceTypes))
//        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerType.adapter = typeAdapter
//    }


    private fun getListNameFromBasicData(dataList: MutableList<Model.BasicData>): List<String> {
        val list: MutableList<String> = mutableListOf()
        for (item in dataList) {
            list.add(item.getName())
        }
        return list
    }

    private fun getListBrandNameFromBranchService(dataList: MutableList<Model.BrandServiceData>): MutableList<String> {
        val list: MutableList<String> = mutableListOf()
        for (item in dataList) {
            list.add(item.name)
        }
        return list
    }

    private fun initListener() {
        btnBook.setOnClickListener {

            val phone = SharePref.getProfile().data!!.phone

            if (!phone.validatePhone()) {

                DialogUtil.getUpdatePhoneDialog(activity, MaterialDialog.InputCallback { dialog, input -> })

            } else {

                try {

                    val newBooking = Model.NewBooking(etLicensePlate.text.toString(),
                            btnChooseModel.text.toString(),
                            btnChooseBrand.text.toString(),
                            "$date$time",
                            basicData!!.data.serviceTypes[chosenTypeIndex].id,
                            basicData!!.data.branches[chosenBranchIndex].id,
                            etMoreInfo.text.toString(),
                            phone)

                    info { newBooking }

                    book(newBooking)

                } catch(e: Exception) {

                    toast(getString(R.string.toast_fill_all_field))

                    e.printStackTrace()
                }
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

        btnChooseBrand.setOnClickListener {
            val brandList = getListBrandNameFromBranchService(basicData!!.data.brandServices)

            /* show brand list  */
            selector(getString(R.string.fragment_new_booking_service_select_model_hint), brandList, { index ->

                /* if user doesn't select other  */
                if (!brandList[index].equals("OTHER")) {

                    /* set text to the one that user has selected*/
                    btnChooseBrand.text = basicData!!.data.brandServices[index].name

                    /* set selected index */
                    chosenBrandIndex = index
                } else {

                    /* if user chosen other then show dialog to let user input brand by manually */
                    showCustomInputDialog("Enter brand name") {
                        btnChooseBrand.text = it
                        chosenBrandIndex = index
                    }
                }

                if (chosenBrandIndex != -1) {
                    btnChooseModel.isEnabled = true
                    btnChooseModel.hint = getString(R.string.fragment_new_booking_service_select_model_hint)
                    btnChooseModel.text = ""
                }

                isModified = true
            })
        }

        btnChooseModel.setOnClickListener {

            val brandList = getListBrandNameFromBranchService(basicData!!.data.brandServices)

            /* If user doesn't select other brand */
            if (!brandList[chosenBrandIndex].equals("OTHER")) {

                val brandServiceData = basicData!!.data.brandServices[chosenBrandIndex]

                brandServiceData.modelServices = addOtherToModelIfNeeded(brandServiceData.modelServices)

                /* show model list */

                selector(getString(R.string.fragment_new_booking_service_select_model_hint), brandServiceData.modelServices, { index ->

                    /* if user doesn't select other model */
                    if (!brandServiceData.modelServices[index].equals("Other")) {
                        btnChooseModel.text = brandServiceData.modelServices[index]
                        chosenModelIndex = index

                    } else {

                        /* if user select other model, then let user type it manually */

                        showCustomInputDialog("Enter model name") {
                            btnChooseModel.text = it
                            chosenModelIndex = index
                        }

                    }
                    isModified = true
                })


            } else {/* User select other brand */

                /* show dialog to let user type the model name manually */
                showCustomInputDialog("Enter model name") {
                    btnChooseModel.text = it
                    chosenModelIndex = -1
                }
            }

        }

        btnChooseType.setOnClickListener {
            selector(getString(R.string.fragment_new_booking_service_type_hint), getListNameFromBasicData(basicData!!.data.serviceTypes), {
                btnChooseType.text = basicData!!.data.serviceTypes[it].getName()
                chosenTypeIndex = it
                isModified = true
            })
        }

        btnChooseBranch.setOnClickListener {
            selector(getString(R.string.fragment_new_booking_service_branch_hint), getListNameFromBasicData(basicData!!.data.branches), {
                btnChooseBranch.text = basicData!!.data.branches[it].getName()
                chosenBranchIndex = it
                isModified = true
            })
        }

    }

    private fun addOtherToModelIfNeeded(modelServices: MutableList<String>): MutableList<String> {

        if (modelServices.find { it.equals("Other") } == null) {
            modelServices.add("Other")
        }

        return modelServices
    }
}


package com.socket9.thetsl.dialog

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker

/**
 * Created by Euro (ripzery@gmail.com) on 5/6/16 AD.
 */

class TimePickerFragmentDialog(context: Context, listener: OnTimeSetListener, hour: Int, min: Int, is24Hour: Boolean) : TimePickerDialog(context, listener, hour, min, is24Hour) {

    override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
        super.onTimeChanged(view, hourOfDay, minute)
    }

}
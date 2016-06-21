package com.socket9.thetsl.extensions

import android.support.v4.app.Fragment
import android.widget.Toast

/**
 * Created by Euro (ripzery@gmail.com) on 4/10/16 AD.
 */

fun Fragment.toast(msg: String) {
    toaster?.cancel()
    toaster = Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
    toaster?.show()
}

fun Fragment.replaceFragment(fragmentContainer:Int , fragment: Fragment) {
    childFragmentManager.beginTransaction()
            .replace(fragmentContainer, fragment)
            .commit()
}

fun Fragment.applyTransition(enter: Int, exit: Int){
    activity.overridePendingTransition(enter, exit)
}
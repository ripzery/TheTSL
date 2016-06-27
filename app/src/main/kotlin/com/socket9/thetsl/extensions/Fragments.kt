package com.socket9.thetsl.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.support.v4.app.Fragment
import android.widget.Toast
import com.socket9.thetsl.BuildConfig
import com.socket9.thetsl.R
import com.tbruyelle.rxpermissions.RxPermissions
import org.jetbrains.anko.support.v4.makeCall

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

@TargetApi(21)
@SuppressLint("NewApi")
fun Fragment.supportsLollipop(code: () -> Unit){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        code()
    }
}

@TargetApi(19)
@SuppressLint("NewApi")
fun Fragment.supportsBackward(code: () -> Unit){
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
        code()
    }
}

fun Fragment.requestPhonePermission(code: () -> Unit){
    RxPermissions.getInstance(activity).request(Manifest.permission.CALL_PHONE)
        .subscribe { granted ->
            if (granted) {
                code()
            } else {
                toast(getString(R.string.permission_phone_required))
            }
        }
}
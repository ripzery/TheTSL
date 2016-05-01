package com.socket9.thetsl.extensions

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.socket9.thetsl.R

/**
 * Created by Euro (ripzery@gmail.com) on 4/10/16 AD.
 */

fun AppCompatActivity.replaceFragment(fragmentContainer: Int = R.id.contentContainer, fragment: Fragment){
    supportFragmentManager.beginTransaction()
            .replace(fragmentContainer, fragment)
            .commit()
}


fun Activity.toast(msg: String) {
    toaster?.cancel()
    toaster = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
    toaster?.show()
}

var toaster: Toast? = null
package com.socket9.thetsl.extensions

import android.widget.EditText

/**
 * Created by Euro (ripzery@gmail.com) on 5/13/16 AD.
 */


fun EditText.validatePhone(): Boolean {
    return text.toString().validatePhone()
}

fun EditText.validateEmail(): Boolean {
    return text.toString().validateEmail()
}

fun EditText.validateName(): Boolean {
    return text.toString().validateName()
}

fun EditText.validatePassword(): Boolean {
    return text.toString().validatePassword()
}

fun EditText.validateConfirmPassword(password: String): Boolean {
    return text.toString().validateConfirmPassword(password)
}


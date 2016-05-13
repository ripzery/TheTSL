package com.socket9.thetsl.extensions


/**
 * Created by Euro (ripzery@gmail.com) on 4/20/16 AD.
 */


fun String.plainText() : String{
    return replace("<.*?>".toRegex(), "")
}

fun String.validatePhone(): Boolean {
    return length == 10 && android.util.Patterns.PHONE.matcher(this).matches()
}

fun String.validateEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.validateName(): Boolean {
    return trim().length > 0
}

fun String.validatePassword(): Boolean {
    return length >= 8
}

fun String.validateConfirmPassword(password: String): Boolean {
    return equals(password)
}


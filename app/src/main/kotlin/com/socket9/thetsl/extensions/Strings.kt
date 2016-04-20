package com.socket9.thetsl.extensions

/**
 * Created by Euro (ripzery@gmail.com) on 4/20/16 AD.
 */

fun String.plainText() : String{
    return replace("<.*?>".toRegex(), "")
}
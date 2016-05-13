package com.socket9.thetsl.utils

import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.validateName
import com.socket9.thetsl.extensions.validatePassword
import com.socket9.thetsl.extensions.validatePhone

/**
 * Created by Euro (ripzery@gmail.com) on 5/13/16 AD.
 */

object ValidatorUtil {

    fun getErrorMsgUpdateProfile(phone: String, password: String, name: String): String {
        if (!phone.validatePhone()) {
            return Contextor.context!!.getString(R.string.validate_phone)
        } else if (!password.validatePassword()) {
            return Contextor.context!!.getString(R.string.validate_password)
        } else if (!name.validateName()) {
            return Contextor.context!!.getString(R.string.validate_name)
        } else {
            return ""
        }
    }

}
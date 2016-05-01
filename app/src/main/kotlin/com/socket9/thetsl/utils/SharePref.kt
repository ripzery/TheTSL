package com.socket9.thetsl.utils

import android.content.SharedPreferences
import com.socket9.thetsl.extensions.getSp

/**
 * Created by Euro (ripzery@gmail.com) on 4/11/16 AD.
 */

object SharePref {
    var sharePref: SharedPreferences? = null

    val SHARE_PREF_KEY_GCM_TOKEN = "GCM_TOKEN"
    val SHARE_PREF_KEY_API_TOKEN = "API_TOKEN"
    val SHARE_PREF_KEY_APP_LANG = "APP_LANGUAGE"

    fun putString(key:String, value:String){
        sharePref?.edit()?.putString(key,value)?.apply()
    }

    fun putInt(key:String, value:Int){
        sharePref?.edit()?.putInt(key,value)?.apply()
    }

    fun putLong(key:String, value:Long){
        sharePref?.edit()?.putLong(key,value)?.apply()
    }

    fun putFloat(key:String, value:Float){
        sharePref?.edit()?.putFloat(key, value)?.apply()
    }

    fun putBoolean(key:String, value:Boolean){
        sharePref?.edit()?.putBoolean(key, value)?.apply()
    }

    fun getToken(): String{
        return getSp(SHARE_PREF_KEY_API_TOKEN, "") as String
    }

    fun isEnglish(): Boolean{
        return (getSp(SHARE_PREF_KEY_APP_LANG, "") as String).equals("en")
    }

}
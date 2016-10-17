package com.socket9.thetsl.utils

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.socket9.thetsl.extensions.getSp
import com.socket9.thetsl.extensions.saveSp
import com.socket9.thetsl.models.Model
import com.socket9.thetsl.ui.main.MainActivity

/**
 * Created by Euro (ripzery@gmail.com) on 4/11/16 AD.
 */

object SharePref {
    var sharePref: SharedPreferences? = null

    val SHARE_PREF_KEY_GCM_TOKEN = "GCM_TOKEN"
    val SHARE_PREF_KEY_API_TOKEN = "API_TOKEN"
    val SHARE_PREF_KEY_APP_LANG = "APP_LANGUAGE"
    val SHARE_PREF_KEY_USER_DATA = "USER_DATA"
    val SHARE_PREF_KEY_NOTI_GCM_DATA = "GCM_DATA"
    val SHARE_PREF_KEY_NOTI_FRAGMENT_INDEX = "FRAGMENT_INDEX"

    fun putString(key: String, value: String) {
        sharePref?.edit()?.putString(key, value)?.apply()
    }

    fun putInt(key: String, value: Int) {
        sharePref?.edit()?.putInt(key, value)?.apply()
    }

    fun putLong(key: String, value: Long) {
        sharePref?.edit()?.putLong(key, value)?.apply()
    }

    fun putFloat(key: String, value: Float) {
        sharePref?.edit()?.putFloat(key, value)?.apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        sharePref?.edit()?.putBoolean(key, value)?.apply()
    }

    fun getToken(): String {
        return getSp(SHARE_PREF_KEY_API_TOKEN, "") as String
    }

    fun isEnglish(): Boolean {
        return (getSp(SHARE_PREF_KEY_APP_LANG, "") as String).equals("en")
    }

    fun getProfile(): Model.Profile {
        var userData = getSp(SHARE_PREF_KEY_USER_DATA, "") as String

        /* user has been already saved to share preference */
        if (!userData.equals("")) {
            return Gson().fromJson(userData, Model.Profile::class.java)

        } else {
            throw IllegalStateException("User data is null")
            /* user has not been saved yet*/
        }
    }

    fun saveProfile(model: Model.Profile) {
        saveSp(SHARE_PREF_KEY_USER_DATA, Gson().toJson(model))
        Log.d("SharePref", "Save profile success : " + model.toString())
    }

    fun getFragmentIndex(): Int {
        return sharePref!!.getInt(SHARE_PREF_KEY_NOTI_FRAGMENT_INDEX, MainActivity.FRAGMENT_DISPLAY_EMERGENCY)
    }

    fun setFragmentIndex(value: Int) {
        putInt(SHARE_PREF_KEY_NOTI_FRAGMENT_INDEX, value)
    }

    fun getGcmData(): Model.GCMData? {
        val string = sharePref!!.getString(SHARE_PREF_KEY_NOTI_GCM_DATA, "")
        if (string.isNotEmpty()) {
            return Gson().fromJson(string, Model.GCMData::class.java)
        } else {
            return null
        }
    }

    fun setGcmData(data: Model.GCMData?) {
        try {
            val string = Gson().toJson(data)
            putString(SHARE_PREF_KEY_NOTI_GCM_DATA, string)
        } catch (e: Exception) {

        }
    }
}
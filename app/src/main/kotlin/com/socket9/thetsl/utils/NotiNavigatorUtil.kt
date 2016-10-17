package com.socket9.thetsl.utils

import com.socket9.thetsl.models.Model
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by ripzery on 10/17/16.
 */
object NotiNavigatorUtil : AnkoLogger {
    var currentFragmentIndex: Int
        get() {
            return SharePref.getFragmentIndex()
        }
        set(value) {
            info { "setCurrentIndex to $value" }
            SharePref.setFragmentIndex(value)
        }
    var gcmData: Model.GCMData?
        get() {
            return SharePref.getGcmData()
        }
        set(value) {
            SharePref.setGcmData(value)
        }

}
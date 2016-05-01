package com.socket9.thetsl

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.socket9.thetsl.utils.Contextor
import com.socket9.thetsl.utils.SharePref
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * Created by Euro (ripzery@gmail.com) on 4/9/16 AD.
 */


class BaseApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics());
        SharePref.sharePref = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        Contextor.context = this
        Timber.plant(Timber.DebugTree())
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder().setDefaultFontPath("fonts/samakarn/Samakarn-Bold.ttf").setFontAttrId(R.attr.fontPath).build())
    }

}
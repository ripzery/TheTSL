package com.socket9.thetsl

import android.app.Application
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.socket9.thetsl.extensions.getSp
import com.socket9.thetsl.extensions.saveSp
import com.socket9.thetsl.utils.Contextor
import com.socket9.thetsl.utils.SharePref
import com.socket9.thetsl.viewgroups.FontsOverride
import io.fabric.sdk.android.Fabric
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * Created by Euro (ripzery@gmail.com) on 4/9/16 AD.
 */


class BaseApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics());
        FacebookSdk.sdkInitialize(applicationContext);
        AppEventsLogger.activateApp(this);
        SharePref.sharePref = getSharedPreferences(getString(R.string.app_name_tsl), MODE_PRIVATE)

        /* Initial english language if no language set */
        if ((getSp(SharePref.SHARE_PREF_KEY_API_TOKEN, "") as String).isEmpty()) saveSp(SharePref.SHARE_PREF_KEY_APP_LANG, "en")

        Contextor.context = this
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder().setDefaultFontPath("fonts/samakarn/Samakarn-Regular.ttf").setFontAttrId(R.attr.fontPath).build())

        try {
            FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/samakarn/Samakarn-Regular.ttf")
        }catch(e: RuntimeException){
            Log.w("FontsOverride", "Font asset not found fonts/samakarn/Samakarn-Regular.ttf")
        }
    }

}
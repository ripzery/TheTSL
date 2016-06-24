package com.socket9.thetsl.ui.signin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.socket9.thetsl.extensions.getSp
import com.socket9.thetsl.extensions.saveSp
import com.socket9.thetsl.ui.main.MainActivity
import com.socket9.thetsl.utils.SharePref
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*

/**
* Created by Euro (ripzery@gmail.com) on 4/20/16 AD.
*/
class EntryActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: Implement locale base on language user defined in share pref
        val token = getSp(SharePref.SHARE_PREF_KEY_API_TOKEN, "") as String
        val isHasToken: Boolean = !token.equals("")
        setLocale(getSp(SharePref.SHARE_PREF_KEY_APP_LANG, "") as String)
        info { token }

        startActivity(Intent(this, if (isHasToken) MainActivity::class.java else SignInActivity::class.java))
        finish()

    }

    private fun setLocale(lang: String) {
        val myLocale = Locale(lang)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
        saveSp(SharePref.SHARE_PREF_KEY_APP_LANG, lang)
    }

}
package com.socket9.thetsl.activities


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.socket9.thetsl.R
import com.socket9.thetsl.SignInActivity
import com.socket9.thetsl.extensions.getSp
import com.socket9.thetsl.extensions.replaceFragment
import com.socket9.thetsl.extensions.saveSp
import com.socket9.thetsl.fragments.*
import com.socket9.thetsl.utils.DialogUtil
import com.socket9.thetsl.utils.SharePref
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */

class MainActivity : AppCompatActivity(), AnkoLogger {

    /** Variable zone **/

    companion object {
        val FRAGMENT_DISPLAY_HOME = 1
        val FRAGMENT_DISPLAY_NEWS = 2
        val FRAGMENT_DISPLAY_CONTACT = 3
        val FRAGMENT_DISPLAY_EMERGENCY = 4
        val FRAGMENT_DISPLAY_PROFILE = 5
        val FRAGMENT_DISPLAY_EVENT = 6
    }

    private var homeFragment: HomeFragment? = null
    private var newsFragment: NewsEventFragment? = null
    private var contactFragment: ContactFragment? = null
    private var emergencyFragment: EmergencyFragment? = null
    private val profileFragment: Fragment? = null
    private var eventFragment: NewsEventFragment? = null

    /** Lifecycle  zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        setContentView(R.layout.activity_main)
        initInstance()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            when(requestCode){
                EmergencyFragment.REQUEST_CODE_LOCATION_SETTING -> {
                    if(resultCode == RESULT_OK){
                        emergencyFragment?.userEnabledLocation()
                    }else{
                        emergencyFragment?.userNotEnabledLocation()

                    }
                }

            }
    }

    /** Method zone **/

    private fun initInstance() {
        val isEnglish = (getSp(SharePref.SHARE_PREF_KEY_APP_LANG, "") as String).equals("en")
        btnChangeLanguage.text = getString(if (isEnglish) R.string.dialog_change_lang_english else R.string.dialog_change_lang_thai)

        initToolbar(toolbar, getString(R.string.toolbar_main), false)
        initFragment()
        setListener()
        setupDrawerContent()
        changeFragment(FRAGMENT_DISPLAY_HOME)
        navView.setCheckedItem(R.id.nav_home)
    }

    fun onFragmentAttached(number: Int) {
        var mTitle = ""
        try {
            toolbarTitle.visibility = if (number == FRAGMENT_DISPLAY_NEWS || number == FRAGMENT_DISPLAY_NEWS || number == FRAGMENT_DISPLAY_HOME) View.GONE else View.VISIBLE
            layoutNewsEvent.visibility = if (number == FRAGMENT_DISPLAY_NEWS || number == FRAGMENT_DISPLAY_EVENT) View.VISIBLE else View.GONE
            ivLogo.visibility = if (number == FRAGMENT_DISPLAY_HOME) View.VISIBLE else View.GONE
            when (number) {
                FRAGMENT_DISPLAY_HOME -> {
                }
                FRAGMENT_DISPLAY_EMERGENCY -> mTitle = getString(R.string.nav_emergency)
                FRAGMENT_DISPLAY_CONTACT -> mTitle = getString(R.string.nav_contact)
                FRAGMENT_DISPLAY_NEWS -> {
                }
            }
            toolbarTitle.text = mTitle
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initFragment() {
        homeFragment = HomeFragment.newInstance("homeFragment")
        newsFragment = NewsEventFragment.newInstance(true)
        eventFragment = NewsEventFragment.newInstance(false)
        contactFragment = ContactFragment.newInstance("ContactFragment")
        emergencyFragment = EmergencyFragment.newInstance("EmergencyFragment")
    }

    private fun changeFragment(mode: Int) {
        when (mode) {
            FRAGMENT_DISPLAY_HOME -> replaceFragment(fragment = homeFragment!!)
            FRAGMENT_DISPLAY_NEWS -> replaceFragment(fragment = newsFragment!!)
            FRAGMENT_DISPLAY_CONTACT -> replaceFragment(fragment = contactFragment!!)
            FRAGMENT_DISPLAY_EMERGENCY -> replaceFragment(fragment = emergencyFragment!!)
            FRAGMENT_DISPLAY_EVENT -> replaceFragment(fragment = eventFragment!!)
        }
        onFragmentAttached(mode)
    }

    private fun setupDrawerContent() {
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    changeFragment(FRAGMENT_DISPLAY_HOME)
                    menuItem.isChecked = true
                }
                R.id.nav_news -> {
                    //TODO: Save states is news or event is lastly visible
                    changeFragment(FRAGMENT_DISPLAY_NEWS)
                    menuItem.isChecked = true
                }
                R.id.nav_contact -> {
                    changeFragment(FRAGMENT_DISPLAY_CONTACT)
                    menuItem.isChecked = true
                }
                R.id.nav_emergency_call -> {
                    changeFragment(FRAGMENT_DISPLAY_EMERGENCY)
                    menuItem.isChecked = true
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun setListener() {
        btnLeft.setOnClickListener {
            if (!newsFragment!!.isVisible) {
                changeFragment(FRAGMENT_DISPLAY_NEWS)
            }

            btnLeft.setTextColor(ContextCompat.getColor(this, R.color.colorTextPrimary))
            btnLeft.background = ContextCompat.getDrawable(this, R.drawable.button_corner_left)
            btnRight.setTextColor(ContextCompat.getColor(this, R.color.colorTextSecondary))
            btnRight.background = ContextCompat.getDrawable(this, R.drawable.button_corner_right_white)
        }
        btnRight.setOnClickListener {
            if (!eventFragment!!.isVisible) {
                changeFragment(FRAGMENT_DISPLAY_EVENT)
            }

            btnLeft.setTextColor(ContextCompat.getColor(this, R.color.colorTextSecondary))
            btnLeft.background = ContextCompat.getDrawable(this, R.drawable.button_corner_left_white)
            btnRight.setTextColor(ContextCompat.getColor(this, R.color.colorTextPrimary))
            btnRight.background = ContextCompat.getDrawable(this, R.drawable.button_corner_right)
        }

        btnChangeLanguage.setOnClickListener {
            DialogUtil.getChangeLangDialog(this, MaterialDialog.ListCallbackSingleChoice { materialDialog, view, i, charSequence ->
                if (i == 0) {
                    setLocale("th")
                } else {
                    setLocale("en")
                }
                startActivity(Intent(this@MainActivity, MainActivity::class.java))
                finish()
                true
            }).show()
        }
        btnSignOut.setOnClickListener {
            DialogUtil.getSignOutDialog(this@MainActivity, MaterialDialog.SingleButtonCallback { materialDialog, dialogAction ->
                LoginManager.getInstance().logOut()
                saveSp(SharePref.SHARE_PREF_KEY_API_TOKEN, "")
                startActivity(Intent(this@MainActivity, SignInActivity::class.java))
                finish()
            }).show()
        }
    }

    fun initToolbar(myToolbar: Toolbar, title: String, isBackVisible: Boolean) {
        setSupportActionBar(myToolbar)
        val mActionBar = supportActionBar
        val tvTitle = myToolbar.findViewById(R.id.toolbarTitle) as TextView
        tvTitle.text = title
        mActionBar?.setDisplayShowCustomEnabled(true)
        mActionBar?.setDisplayShowTitleEnabled(false)
        mActionBar?.setDisplayHomeAsUpEnabled(true)
        if (!isBackVisible) mActionBar?.setHomeAsUpIndicator(R.drawable.menu)
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

    /** Listener zone **/

}
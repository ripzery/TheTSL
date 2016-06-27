package com.socket9.thetsl.ui.main


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.*
import com.socket9.thetsl.fragments.NewsEventFragment
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.models.Model
import com.socket9.thetsl.ui.main.contact.ContactFragment
import com.socket9.thetsl.ui.main.emergency.EmergencyFragment
import com.socket9.thetsl.ui.main.miracle.MiracleClubFragment
import com.socket9.thetsl.ui.main.profile.MyProfileActivity
import com.socket9.thetsl.ui.main.tracking.sale.CarTrackingFragment
import com.socket9.thetsl.ui.main.tracking.service.ServiceFragment
import com.socket9.thetsl.ui.main.website.WebsiteFragment
import com.socket9.thetsl.ui.signin.SignInActivity
import com.socket9.thetsl.utils.DialogUtil
import com.socket9.thetsl.utils.SharePref
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_navigation_header.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.info
import rx.Subscription
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */

class MainActivity : RxAppCompatActivity(), AnkoLogger, BottomNavigationFragment.OnChangeTabListener {

    /** Variable zone **/

    companion object {
        val FRAGMENT_DISPLAY_HOME = 1
        val FRAGMENT_DISPLAY_NEWS = 2
        val FRAGMENT_DISPLAY_CONTACT = 3
        val FRAGMENT_DISPLAY_EMERGENCY = 4
        val FRAGMENT_DISPLAY_EVENT = 6
        val FRAGMENT_DISPLAY_SERVICE = 7
        val FRAGMENT_DISPLAY_CAR_TRACKING = 8
        val FRAGMENT_DISPLAY_MIRACLE_CLUB = 9
        val FRAGMENT_DISPLAY_WEBSITE = 10
    }


    private var homeFragment: HomeFragment? = null
    private var newsFragment: NewsEventFragment? = null
    private var contactFragment: ContactFragment? = null
    private var emergencyFragment: EmergencyFragment? = null
    private var eventFragment: NewsEventFragment? = null
    private var serviceFragment: ServiceFragment? = null
    private var carTrackingFragment: CarTrackingFragment? = null
    private var websiteFragment: WebsiteFragment? = null
    private var miracleClubFragment: MiracleClubFragment? = null
    private var bottomNavigationFragment: BottomNavigationFragment? = null
    private var currentFragmentIndex: Int = 4
    lateinit var myProfile: Model.Profile;
    private var getProfileSubscriber: Subscription? = null
    private var dialog: ProgressDialog ? = null
    private var tvName: TextView ? = null
    private var headerView: View? = null
    //    private var isLaunchedByGcm: Boolean = false
//    private var type: String = ""
    private var gcmData: Model.GCMData? = null

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

    override fun onStop() {
        super.onStop()
        dialog?.dismiss()
        getProfileSubscriber?.unsubscribe()
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
        when (requestCode) {
            EmergencyFragment.REQUEST_CODE_LOCATION_SETTING -> {
                if (resultCode == RESULT_OK) {
                    try {
                        emergencyFragment?.userEnabledLocation()
                    } catch(e: Exception) {
                        info { e }
                    }
                } else {
                    toast(getString(R.string.toast_enable_location))
//                    emergencyFragment?.userNotEnabledLocation()
                }
            }
            HomeFragment.REQUEST_MY_PROFILE -> {
                if (resultCode == RESULT_OK) loadProfile(false)
            }

        }
    }

    override fun onBackPressed() {
        DialogUtil.getQuitDialog(this, MaterialDialog.SingleButtonCallback { dialog, which ->
            finish()
        }).show()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    /** Method zone **/

    private fun initInstance() {

        /* check if coming from verification url in email */
        checkIfEnterFromUrl()

        /* init language */
        val isEnglish = SharePref.isEnglish()
        btnChangeLanguage.text = getString(if (isEnglish) R.string.dialog_change_lang_english else R.string.dialog_change_lang_thai)

        /* Init toolbar text */
        headerView = navView.getHeaderView(0)
        tvName = headerView?.findViewById(R.id.tvName) as TextView
        initToolbar(toolbar, getString(R.string.toolbar_main), false)

        /* Init fragment */
        initFragment()

        /* setup listener's sake*/
        setListener()

        /* setup navigation view font */
        setupNavigationViewFont()

        /* get user profile from api */
        loadProfile(false)

        /* set boolean if this activity is launched by gcm  */
        gcmData = intent.getParcelableExtra("gcmData")

//        isLaunchedByGcm = intent.getBooleanExtra("isGcm", false)
//        type = intent.getStringExtra("type") ?: ""


        /* set currentFragment when change language */
        currentFragmentIndex = intent.getIntExtra("currentFragmentIndex", 4)

        changeFragment(currentFragmentIndex)
        setCheckedItem(currentFragmentIndex)

    }

    private fun setupNavigationViewFont() {
//        for (i in 0..navView.menu.size() - 1) {
//            val menuItem = navView.menu.getItem(i)
//            val t = CalligraphyTypefaceSpan(Typeface.createFromAsset(assets, "fonts/samakarn/Samakarn-Regular.ttf"))
//            val spannableString = SpannableString(menuItem.title)
//            info { menuItem.title }
//            spannableString.setSpan(t, 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//            menuItem?.title = spannableString
//        }
    }

    private fun checkIfEnterFromUrl() {
        if (intent.data != null) {
            try {
                val data = intent.data
                saveSp(SharePref.SHARE_PREF_KEY_API_TOKEN, data.getQueryParameter("token"))

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
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
                FRAGMENT_DISPLAY_SERVICE -> mTitle = getString(R.string.nav_service)
                FRAGMENT_DISPLAY_CAR_TRACKING -> mTitle = getString(R.string.nav_car_tracking)
                FRAGMENT_DISPLAY_WEBSITE -> mTitle = getString(R.string.nav_website)
                FRAGMENT_DISPLAY_MIRACLE_CLUB -> mTitle = getString(R.string.nav_miracle)
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
        carTrackingFragment = CarTrackingFragment.newInstance("CarTrackingFragment")
        emergencyFragment = EmergencyFragment.newInstance("EmergencyFragment")
        serviceFragment = ServiceFragment.newInstance("ServiceFragment")
        miracleClubFragment = MiracleClubFragment.newInstance("MiracleFragment")
        websiteFragment = WebsiteFragment.newInstance("WebsiteFragment")
        bottomNavigationFragment = BottomNavigationFragment.newInstance(BottomNavigationFragment.EMERGENCY, this, gcmData)
    }

    private fun changeFragment(mode: Int) {
        when (mode) {
            FRAGMENT_DISPLAY_HOME -> replaceFragment(fragment = homeFragment!!)
            FRAGMENT_DISPLAY_NEWS -> replaceFragment(fragment = newsFragment!!)
            FRAGMENT_DISPLAY_CONTACT -> replaceFragment(fragment = contactFragment!!)
            FRAGMENT_DISPLAY_EMERGENCY -> {
                //                replaceFragment(fragment = emergencyFragment!!)
                bottomNavigationFragment = BottomNavigationFragment.newInstance(BottomNavigationFragment.EMERGENCY, this, gcmData)
                replaceFragment(fragment = bottomNavigationFragment!!)
                bottomNavigationFragment!!.setTab(BottomNavigationFragment.EMERGENCY, gcmData)

                /* cancel isLaunched flag */
                gcmData = null
            }
            FRAGMENT_DISPLAY_EVENT -> replaceFragment(fragment = eventFragment!!)
            FRAGMENT_DISPLAY_SERVICE -> {
                //                replaceFragment(fragment = serviceFragment!!)
                bottomNavigationFragment = BottomNavigationFragment.newInstance(BottomNavigationFragment.SERVICE_TRACKING, this, gcmData)
                replaceFragment(fragment = bottomNavigationFragment!!)
                bottomNavigationFragment!!.setTab(BottomNavigationFragment.SERVICE_TRACKING, gcmData)

                /* cancel isLaunched flag */
                gcmData = null
            }
            FRAGMENT_DISPLAY_CAR_TRACKING -> {
                //                replaceFragment(fragment = carTrackingFragment!!)
                bottomNavigationFragment = BottomNavigationFragment.newInstance(BottomNavigationFragment.CAR_TRACKING, this, gcmData)
                replaceFragment(fragment = bottomNavigationFragment!!)
                bottomNavigationFragment!!.setTab(BottomNavigationFragment.CAR_TRACKING, gcmData)

                /* cancel isLaunched flag */
            }
            FRAGMENT_DISPLAY_MIRACLE_CLUB -> {
                replaceFragment(fragment = miracleClubFragment!!)
            }
            FRAGMENT_DISPLAY_WEBSITE -> {
                replaceFragment(fragment = websiteFragment!!)
            }
        }
        gcmData = null
        currentFragmentIndex = mode
        onFragmentAttached(mode)
    }

    private fun setCheckedItem(itemIndex: Int) {
        when (itemIndex) {
            FRAGMENT_DISPLAY_NEWS -> navView.setCheckedItem(R.id.nav_news)
            FRAGMENT_DISPLAY_EVENT -> navView.setCheckedItem(R.id.nav_news)
            FRAGMENT_DISPLAY_CONTACT -> navView.setCheckedItem(R.id.nav_contact)
            FRAGMENT_DISPLAY_EMERGENCY -> navView.setCheckedItem(R.id.nav_emergency_call)
            FRAGMENT_DISPLAY_SERVICE -> navView.setCheckedItem(R.id.nav_service)
            FRAGMENT_DISPLAY_CAR_TRACKING -> navView.setCheckedItem(R.id.nav_car_tracking)
            FRAGMENT_DISPLAY_MIRACLE_CLUB -> navView.setCheckedItem(R.id.nav_miracle_club)
            FRAGMENT_DISPLAY_WEBSITE -> navView.setCheckedItem(R.id.nav_website)
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
                val lastLocale = if (SharePref.isEnglish()) "en" else "th"

                if (i == 0) {
                    setLocale("th")
                } else {
                    setLocale("en")
                }

                if (!lastLocale.equals(getSp(SharePref.SHARE_PREF_KEY_APP_LANG, ""))) {
                    startActivity(Intent(this@MainActivity, MainActivity::class.java)
                            .putExtra("currentFragmentIndex", currentFragmentIndex))
                    finish()
                }

                true
            }).show()
        }
        btnSignOut.setOnClickListener {
            DialogUtil.getSignOutDialog(this@MainActivity, MaterialDialog.SingleButtonCallback { materialDialog, dialogAction ->
                LoginManager.getInstance().logOut()
                saveSp(SharePref.SHARE_PREF_KEY_API_TOKEN, "")
                saveSp(SharePref.SHARE_PREF_KEY_APP_LANG, "en")
                saveSp(SharePref.SHARE_PREF_KEY_USER_DATA, "")
                saveSp(SharePref.SHARE_PREF_KEY_GCM_TOKEN, "")
                setLocale("th")
                startActivity(Intent(this@MainActivity, SignInActivity::class.java))
                finish()
            }).show()
        }

        val ivUser = headerView!!.find<CircleImageView>(R.id.cvUserImage)
        ivUser.setOnClickListener {
            startEditProfile()
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
            //                R.id.nav_home -> {
            //                    changeFragment(FRAGMENT_DISPLAY_HOME)
            //                    menuItem.isChecked = true
            //                }
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
                R.id.nav_service -> {
                    changeFragment(FRAGMENT_DISPLAY_SERVICE)
                    menuItem.isChecked = true
                }
                R.id.nav_car_tracking -> {
                    changeFragment(FRAGMENT_DISPLAY_CAR_TRACKING)
                    menuItem.isChecked = true
                }
                R.id.nav_website -> {
                    changeFragment(FRAGMENT_DISPLAY_WEBSITE)
                    menuItem.isChecked = true
                }
                R.id.nav_miracle_club -> {
                    changeFragment(FRAGMENT_DISPLAY_MIRACLE_CLUB)
                    menuItem.isChecked = true
                }
            }

            //            navView.setChe

            drawerLayout.closeDrawers()
            true
        }
    }

    private fun startEditProfile() {
        try {
            val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, cvUserImage as View, "civUser")
            startActivityForResult(Intent(this, MyProfileActivity::class.java).putExtra("myProfile", myProfile), HomeFragment.REQUEST_MY_PROFILE, options.toBundle())
//            applyTransition(R.anim.activity_forward_enter, R.anim.activity_forward_exit)
        } catch (e: Exception) {

            loadProfile(true)
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

    private fun loadProfile(isFromEditProfile: Boolean) {
        dialog = indeterminateProgressDialog (R.string.dialog_progress_profile_content, R.string.dialog_progress_title)
        dialog?.setCancelable(false)
        dialog?.show()


        if (intent.data != null) {
            getProfileSubscriber = HttpManager.saveDeviceId(getSp(SharePref.SHARE_PREF_KEY_GCM_TOKEN, "") as String)
                    .compose(bindToLifecycle<Model.BaseModel>())
                    .flatMap {
                        info { it }
                        HttpManager.getProfile()
                                .compose(bindToLifecycle<Model.Profile>())
                    }
//                    .bindToLifecycle(this)
                    .subscribe({
                        updateProfileUI(isFromEditProfile, it)

                    }, { error ->
                        dialog?.dismiss()
                        toast(getString(R.string.toast_internet_connection_problem))
                    })
        } else {
            getProfileSubscriber = HttpManager.getProfile()
//                    .bindToLifecycle(this)
                    .compose(bindToLifecycle<Model.Profile>())
                    .subscribe({
                        updateProfileUI(isFromEditProfile, it)
                    }, { error ->
                        dialog?.dismiss()
                        toast(getString(R.string.toast_internet_connection_problem))
                    })
        }
    }

    private fun updateProfileUI(isFromEditProfile: Boolean, it: Model.Profile) {
        dialog?.dismiss()
        myProfile = it
        if (it.data?.pic != null || it.data?.facebookPic != null) {
            Glide.with(this).load(it.data?.pic ?: it.data?.facebookPic).centerCrop().into(cvUserImage)
        } else {
            Glide.with(this).load(R.drawable.default_photo).centerCrop().into(cvUserImage)
        }
        tvName?.text = it.data?.nameEn

        /* check if load data because user tap on user profile
        * (and also doesn't have profile yet or not?) */

        if (isFromEditProfile) {
            startEditProfile()
        }
    }

    /* ButtomBar onchanged tab listener */
    override fun onChangedTab(index: Int) {
        setCheckedItem(index)
        onFragmentAttached(index)
    }

}
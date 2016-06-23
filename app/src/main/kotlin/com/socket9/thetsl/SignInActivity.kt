package com.socket9.thetsl

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import com.afollestad.materialdialogs.MaterialDialog
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.socket9.thetsl.activities.CreateAccountActivity
import com.socket9.thetsl.activities.MainActivity
import com.socket9.thetsl.extensions.getSp
import com.socket9.thetsl.extensions.saveSp
import com.socket9.thetsl.gcm.RegistrationIntentService
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.models.Model
import com.socket9.thetsl.utils.DialogUtil
import com.socket9.thetsl.utils.SharePref
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.*
import org.json.JSONException
import rx.Subscription

class SignInActivity : RxAppCompatActivity(), AnkoLogger {

    /** Variable zone **/
    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    private var callbackManager: CallbackManager? = null
    //    private var facebookId: String? = null
    private var mRegistrationBroadcastReceiver: BroadcastReceiver? = null
    private var isReceiverRegistered = false
    private var loginFbSubscription: Subscription? = null
    private var loginSubscription: Subscription? = null
    private var loginProgressDialog: ProgressDialog? = null


    /** Static method zone **/
    companion object {
        private val TAG = "SignInActivity"
        private val REGISTRATION_COMPLETE = "REGISTER_COMPLETE"
    }

    /** Lifecycle method zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        setContentView(R.layout.activity_sign_in)
        initGCM()
        registerReceiver()
        setListener()
        setupFacebook()
        initInstance()
    }

    private fun initInstance() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            val intent = Intent(this, RegistrationIntentService::class.java)
            startService(intent)
        }

        if (intent.getBooleanExtra("invalidToken", false)) {
            toast(getString(R.string.toast_token_invalid))
        }
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver)
        loginProgressDialog?.dismiss()
        isReceiverRegistered = false
        loginFbSubscription?.unsubscribe()
        loginSubscription?.unsubscribe()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_sign_in, menu)
        return true
    }

    /** Method zone  */

    fun initGCM() {
        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                info { "Register complete!" }
                //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
            }
        }
    }

    private fun registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, IntentFilter(REGISTRATION_COMPLETE))
            isReceiverRegistered = true
        }
    }

    private fun setListener() {
        //ivForgotPassword.setOnClickListener(this);
        btnFbLoginFake.setOnClickListener {
            LoginManager.getInstance().logOut()
            btnFbLoginReal.performClick()
        }

        btnRegister.setOnClickListener {
            startActivity<CreateAccountActivity>()
            overridePendingTransition(R.anim.activity_forward_enter, R.anim.activity_forward_exit)

        }

        btnLogin.setOnClickListener {
            login()
        }

        ivForgotPassword.setOnClickListener {
            forgetPassword()
        }

        tvCall.setOnClickListener {
            DialogUtil.getCallUsDialog(this, MaterialDialog.SingleButtonCallback { dialog, which ->
                makeCall("022699999")
            }).show()
        }
    }

    private fun setupFacebook() {
        callbackManager = CallbackManager.Factory.create()
        btnFbLoginReal.setReadPermissions("user_friends", "user_hometown", "email", "user_about_me");

        //         Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                requestGraphApi(loginResult.accessToken)
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })

    }

    private fun requestGraphApi(token: AccessToken) {
        val request = GraphRequest.newMeRequest(token) { jsonObject, response ->
            // Application code
            try {
                val facebookId = jsonObject.getString("id")
                val name = jsonObject.getString("name")
                val email = jsonObject.getString("email")
                var hometown = ""
                var fbPhoto = ""
                try {
                    hometown = jsonObject.getJSONObject("hometown").getString("name")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                try {
                    fbPhoto = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                loginWithFb(email, facebookId, fbPhoto, hometown, name)


                //BusProvider.post(new ApiFire.LoginWithFb(email, name, name, email, hometown, facebookId, fbPhoto));
            } catch (e: JSONException) {
                toast(e.message ?: "")
                e.printStackTrace()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,email,hometown,picture")
        request.parameters = parameters
        request.executeAsync()
    }

    /* Manual login with username and password */
    private fun login() {
        loginProgressDialog = indeterminateProgressDialog(R.string.dialog_progress_login_content, R.string.dialog_progress_title)
        loginProgressDialog?.setCancelable(false)
        loginProgressDialog?.show()

        loginSubscription = HttpManager.login(etUsername.text.toString(), etPassword.text.toString(), getSp(SharePref.SHARE_PREF_KEY_GCM_TOKEN, "") as String)
                .compose(this.bindToLifecycle<Model.User>())
                .subscribe ({
                    loginProgressDialog?.dismiss()
                    info { it.message }
                    if (it.result) {
                        saveSp(SharePref.SHARE_PREF_KEY_API_TOKEN, it.data.token)
                        toast(getString(R.string.toast_login_successful))
                        startActivity<MainActivity>()
                        finish()
                    } else {
                        toast(it.message!!)
                    }

                }, { error ->
                    info { error.message }
                    toast(getString(R.string.toast_unknown_error_try_again))
                    loginProgressDialog?.dismiss()

                })
    }

    /* Login by using facebook account */
    private fun loginWithFb(email: String, facebookId: String, fbPhoto: String, hometown: String, name: String) {

        info { facebookId }

        loginProgressDialog = indeterminateProgressDialog(R.string.dialog_progress_login_content, R.string.dialog_progress_title)
        loginProgressDialog?.setCancelable(false)
        loginProgressDialog?.show()

        loginFbSubscription = HttpManager.registerUser(email, email, name, hometown, "", facebookId, fbPhoto)
                .flatMap { response ->
                    info { response.toString() }
                    HttpManager.loginWithFb(facebookId, fbPhoto, getSp(SharePref.SHARE_PREF_KEY_GCM_TOKEN, "") as String)
                }
                .doOnNext {
                    if (!it.result) toast("${it.message}")
                    else saveSp(SharePref.SHARE_PREF_KEY_API_TOKEN, it.data.token)
                }
                .filter { it.result }
                .compose(this.bindToLifecycle<Model.User>())
                .subscribe ({
                    loginProgressDialog?.dismiss()
                    info { it.toString() }
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                    finish()
                }, { error ->
                    loginProgressDialog?.dismiss()
                    toast(getString(R.string.toast_unknown_error_try_again))
                    error { error.message }
                })
    }

    /* Show dialog forget password */
    private fun forgetPassword() {
        DialogUtil.getForgotDialog(this, MaterialDialog.InputCallback { dialog, input ->

            loginProgressDialog = indeterminateProgressDialog(R.string.dialog_progress_forgot_password_content, R.string.dialog_progress_title)
            loginProgressDialog?.setCancelable(false)
            loginProgressDialog?.show()

            HttpManager.forgetPassword(input.toString())
                    .compose(this.bindToLifecycle<Model.BaseModel>())
                    .subscribe ({
                        loginProgressDialog?.dismiss()
                        toast(it.message)
                    }, {
                        loginProgressDialog?.dismiss()
                        toast(getString(R.string.toast_unknown_error_try_again))
                    })
        }).show()
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */

    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show()
            } else {
                Log.i(TAG, "This device is not supported.")
                finish()
            }
            return false
        }
        return true
    }

}

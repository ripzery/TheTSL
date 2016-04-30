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
import com.socket9.thetsl.utils.SharePref
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.*
import org.json.JSONException
import rx.Subscription
import timber.log.Timber

class SignInActivity : AppCompatActivity(), AnkoLogger {

    /** Variable zone **/
    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    private var callbackManager: CallbackManager? = null
    //    private var facebookId: String? = null
    private var mRegistrationBroadcastReceiver: BroadcastReceiver? = null
    private var isReceiverRegistered = false
    private var loginFbSubscription: Subscription? = null
    private var loginSubscription: Subscription? = null
    private var loginProgressDialog: ProgressDialog? = null

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
    }

    override fun onResume() {
        super.onResume()
        registerReceiver()

    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver)
        loginProgressDialog?.dismiss()
        isReceiverRegistered = false
        loginFbSubscription?.unsubscribe()
        loginSubscription?.unsubscribe()
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
        }

        btnLogin.setOnClickListener {
            loginProgressDialog = indeterminateProgressDialog(R.string.dialog_progress_login_content, R.string.dialog_progress_title)
            loginProgressDialog?.setCancelable(false)
            loginProgressDialog?.show()

            loginSubscription = HttpManager.login(etUsername.text.toString(), etPassword.text.toString(), getSp(SharePref.SHARE_PREF_KEY_GCM_TOKEN, "") as String)
                    .subscribe ({
                        loginProgressDialog?.dismiss()
                        info { it.message }
                        if(it.result){
                            toast("Login succesful")
                        }else{
                            toast(it.message)
                        }

                    }, { error ->
                        toast("Something went wrong, please try again")
                        loginProgressDialog?.dismiss()

                    })
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
                Timber.d("cancel")
            }

            override fun onError(exception: FacebookException) {
                // App code
                Timber.d(exception.message)
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
                e.printStackTrace()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,email,hometown,picture")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun loginWithFb(email: String, facebookId: String, fbPhoto: String, hometown: String, name: String) {

        info { facebookId }

        loginFbSubscription = HttpManager.registerUser(email, name, hometown, facebookId, fbPhoto)
                .flatMap { response ->
                    info { response.toString() }
                    HttpManager.loginWithFb(facebookId, fbPhoto, getSp(SharePref.SHARE_PREF_KEY_GCM_TOKEN, "") as String)
                }
                .doOnNext {
                    if (!it.result) toast("${it.message}")
                    else saveSp(SharePref.SHARE_PREF_KEY_API_TOKEN, it.data.token)
                }
                .filter { it.result }
                .subscribe ({
                    info { it.toString() }
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                    finish()
                }, { error ->
                    toast("Error has occurred ${error.message}")
                    error { error.message }
                })
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

    /** Static method zone **/
    companion object {
        private val TAG = "SignInActivity"
        private val REGISTRATION_COMPLETE = "REGISTER_COMPLETE"
    }

}

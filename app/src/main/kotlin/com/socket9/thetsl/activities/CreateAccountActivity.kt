package com.socket9.thetsl.activities


import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import com.jakewharton.rxbinding.widget.textChanges
import com.rengwuxian.materialedittext.validation.METValidator
import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.*
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.utils.Contextor
import com.socket9.thetsl.utils.DialogUtil
import kotlinx.android.synthetic.main.activity_create_account.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import rx.Subscription
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */

class CreateAccountActivity : ToolbarActivity(), AnkoLogger {

    /** Variable zone **/
    private var dialog: ProgressDialog? = null
    private var isUserAlreadyInput = false
    private var usernameSubscription: Subscription? = null
    private var phoneSubscription: Subscription? = null
    private var emailSubscription: Subscription? = null
    private var passwordSubscription: Subscription? = null

    /** Lifecycle  zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        initInstance()
    }

    override fun onStart() {
        super.onStart()
        textListener()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        dialog?.dismiss()
    }

    override fun onStop() {
        super.onStop()
        usernameSubscription?.unsubscribe()
        emailSubscription?.unsubscribe()
        phoneSubscription?.unsubscribe()
        passwordSubscription?.unsubscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                if (!isFormEmpty()) {
                    DialogUtil.getUpdateProfileDialog(this, MaterialDialog.SingleButtonCallback { dialog, which ->
                        finish()
                        applyTransition(R.anim.activity_backward_enter, R.anim.activity_backward_exit)
                    }).show()
                } else {
                    finish()
                    applyTransition(R.anim.activity_backward_enter, R.anim.activity_backward_exit)
                }
                return true
            }
        }

        return false
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onBackPressed() {

        if (!isFormEmpty()) {
            DialogUtil.getUpdateProfileDialog(this, MaterialDialog.SingleButtonCallback { dialog, which ->
                finish()
                applyTransition(R.anim.activity_backward_enter, R.anim.activity_backward_exit)
            }).show()
        } else {
            finish()
            applyTransition(R.anim.activity_backward_enter, R.anim.activity_backward_exit)
        }
    }

    /** Method zone **/

    private fun initInstance() {
        setupToolbar("Create Account")

        btnRegister.setOnClickListener() {
            val requirement = etUsername.validateName() && etEmail.validateEmail() && etPassword.validatePassword()
                    && etConfirmPassword.validateConfirmPassword(etPassword.text.toString()) && etPhone.validatePhone()

            if (requirement) {
                registerUser()
            } else {
                validate()
            }
        }

    }

    private fun textListener() {
        usernameSubscription = etUsername.textChanges().subscribe { isUserAlreadyInput = it.length > 0 }
        phoneSubscription = etPhone.textChanges().subscribe { isUserAlreadyInput = it.length > 0 }
        passwordSubscription = etPassword.textChanges().subscribe { isUserAlreadyInput = it.length > 0 }
        emailSubscription = etEmail.textChanges().subscribe { isUserAlreadyInput = it.length > 0 }
    }

    private fun isFormEmpty(): Boolean {
        return etUsername.text.isEmpty() && etPhone.text.isEmpty() && etPassword.text.isEmpty() && etEmail.text.isEmpty()
    }

    private fun registerUser() {

        dialog = indeterminateProgressDialog(R.string.dialog_progress_create_account_content, R.string.dialog_progress_title)
        dialog?.setCancelable(false)
        dialog?.show()

        HttpManager.registerUser(etEmail.text.toString(), etPassword.text.toString(), etUsername.text.toString(),
                etAddress.text.toString(), etPhone.text.toString(), "", "")
                .subscribe ({
                    if (it.result) {
                        toast(getString(R.string.toast_activate_account))
                        finish()
                        applyTransition(R.anim.activity_backward_enter, R.anim.activity_backward_exit)
                    } else if (it.message!!.contains("required")) {
                        toast(getString(R.string.toast_create_account_failed))
                    } else {
                        toast(it.message)
                    }

                    dialog?.dismiss()
                    info { it.message }

                }, { error ->
                    toast(getString(R.string.toast_unknown_error_try_again))
                    dialog?.dismiss()
                    error.printStackTrace()

                })


    }

    private fun validate() {
        etUsername.validateWith(usernameValidator)
        etEmail.validateWith(emailValidator)
        etPhone.validateWith(phoneValidator)
        etPassword.validateWith(passwordValidator)
        etConfirmPassword.validateWith(confirmPasswordValidator)
    }

    /** Validator zone **/

    val usernameValidator: METValidator = object : METValidator(Contextor.context!!.getString(R.string.validate_name)) {
        override fun isValid(text: CharSequence, isEmpty: Boolean): Boolean {
            return text.toString().validateName()
        }
    }

    val confirmPasswordValidator: METValidator = object : METValidator(Contextor.context!!.getString(R.string.validate_confirm_password)) {
        override fun isValid(text: CharSequence, isEmpty: Boolean): Boolean {
            return text.toString().validateConfirmPassword(etPassword.text.toString())
        }
    }

    val phoneValidator: METValidator = object : METValidator(Contextor.context!!.getString(R.string.validate_phone)) {
        override fun isValid(text: CharSequence, isEmpty: Boolean): Boolean {
            return text.toString().validatePhone()
        }
    }

    val passwordValidator: METValidator = object : METValidator(Contextor.context!!.getString(R.string.validate_password)) {
        override fun isValid(text: CharSequence, isEmpty: Boolean): Boolean {
            return text.toString().validatePassword()
        }
    }

    val emailValidator: METValidator = object : METValidator(Contextor.context!!.getString(R.string.validate_email)) {
        override fun isValid(text: CharSequence, isEmpty: Boolean): Boolean {
            return text.toString().validateEmail()
        }
    }

}
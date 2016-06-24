//package com.socket9.thetsl.activities
//
//
//import android.app.ProgressDialog
//import android.content.Context
//import android.os.Bundle
//import android.view.Menu
//import android.view.MenuItem
//import com.jakewharton.rxbinding.widget.RxTextView
//import com.socket9.thetsl.R
//import com.socket9.thetsl.extensions.validateConfirmPassword
//import com.socket9.thetsl.extensions.validateEmail
//import com.socket9.thetsl.extensions.validateName
//import com.socket9.thetsl.extensions.validatePassword
//import com.socket9.thetsl.managers.HttpManager
//import kotlinx.android.synthetic.main.activity_create_account.*
//import org.jetbrains.anko.AnkoLogger
//import org.jetbrains.anko.indeterminateProgressDialog
//import org.jetbrains.anko.info
//import org.jetbrains.anko.toast
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
//
///**
// * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
// */
//
//class CreateAccountActivityBackup : ToolbarActivity(), AnkoLogger {
//
//    /** Variable zone **/
//    private var dialog: ProgressDialog? = null
//    private var isRegisterClicked: Boolean = false
//
//    /** Lifecycle  zone **/
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_create_account)
//        initInstance()
//    }
//
//    override fun onResume() {
//        super.onResume()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        dialog?.dismiss()
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when (item?.itemId) {
//            android.R.id.home -> {
//                finish()
//                return true
//            }
//        }
//
//        return false
//    }
//
//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
//    }
//
//    /** Method zone **/
//
//    private fun initInstance() {
//        setupToolbar("Create Account")
//
//        btnRegister.setOnClickListener() {
//
//            // TODO: Validate before register
//
//            val requirement = etUsername.validateName() && etEmail.validateEmail() && etPassword.validatePassword()
//                    && etConfirmPassword.validateConfirmPassword(etPassword.text.toString())
//
//            if(requirement){
//                registerUser()
//            }else{
//                setEditTextListener()
//            }
//        }
//
//    }
//
//    private fun setEditTextListener() {
//
//
//        RxTextView.textChangeEvents(etUsername).subscribe {
//            if (isRegisterClicked) return@subscribe
//
//            val isEmpty = it.count() == 0
//            if (isEmpty) {
//                textInputUsername.error = getString(R.string.create_account_required_name)
//            } else {
//                textInputUsername.isErrorEnabled = false
//            }
//        }
//
//        RxTextView.textChangeEvents(etEmail).subscribe {
//            if (isRegisterClicked) return@subscribe
//
//            val isEmpty = it.count() == 0
//            if (isEmpty) {
//                textInputEmail.error = getString(R.string.create_account_required_email)
//            } else {
//                textInputEmail.isErrorEnabled = false
//            }
//        }
//
//        RxTextView.textChangeEvents(etPassword).subscribe {
//            if (isRegisterClicked) return@subscribe
//
//            val isEmpty = it.count() == 0
//
//            if (isEmpty) {
//                textInputPassword.error = getString(R.string.create_account_required_password)
//            } else {
//                textInputPassword.isErrorEnabled = false
//            }
//        }
//
//        RxTextView.textChangeEvents(etConfirmPassword).subscribe {
//            if (isRegisterClicked) return@subscribe
//
//            val isEmpty = it.count() == 0
//            val isPasswordMatched = it.text().toString().equals(etPassword.text.toString())
//
//            textInputConfirmPassword.isErrorEnabled = isEmpty || !isPasswordMatched
//
//            if (isEmpty) {
//                textInputConfirmPassword.error = getString(R.string.create_account_required_confirmpw)
//            } else if (!isPasswordMatched) {
//                textInputConfirmPassword.error = getString(R.string.create_account_not_match_confirmpw)
//                btnRegister.isEnabled = false
//            } else {
//                btnRegister.isEnabled = true
//                textInputConfirmPassword.isErrorEnabled = false
//            }
//        }
//    }
//
//    private fun registerUser() {
//
//        dialog = indeterminateProgressDialog(R.string.dialog_progress_create_account_content, R.string.dialog_progress_title)
//        dialog?.setCancelable(false)
//        dialog?.show()
//
//        HttpManager.registerUser(etEmail.text.toString(), etUsername.text.toString(), etAddress.text.toString(), "", "")
//                .subscribe ({
//                    if (it.result) {
//                        toast(getString(R.string.toast_activate_account))
//                        finish()
//                    } else if (it.message!!.contains("required")) {
//                        toast(getString(R.string.toast_create_account_failed))
//                    } else {
//                        toast(it.message)
//                    }
//
//                    dialog?.dismiss()
//                    info { it.message }
//
//                }, { error ->
//                    toast(getString(R.string.toast_unknown_error_try_again))
//                    dialog?.dismiss()
//                    error.printStackTrace()
//
//                })
//
//
//    }
//
//    private fun validateRequiredField() {
//        if (etUsername.text.length == 0) {
//            textInputUsername.error = getString(R.string.create_account_required_name)
//        } else {
//            textInputUsername.isErrorEnabled = false
//        }
//
//        if (etEmail.text.length == 0) {
//            textInputEmail.error = getString(R.string.create_account_required_email)
//        } else {
//            textInputEmail.isErrorEnabled = false
//        }
//
//        if (etPassword.text.length == 0) {
//            textInputPassword.error = getString(R.string.create_account_required_password)
//        } else {
//            textInputPassword.isErrorEnabled = false
//        }
//
//        if (etConfirmPassword.text.length == 0) {
//            textInputConfirmPassword.error = getString(R.string.create_account_required_confirmpw)
//        } else {
//            textInputConfirmPassword.isErrorEnabled = false
//        }
//    }
//
//    private fun setToolbar() {
//        supportActionBar?.title = "Create Account"
//        supportActionBar?.setHomeButtonEnabled(true)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//    }
//
//    /** Listener zone **/
//
//}
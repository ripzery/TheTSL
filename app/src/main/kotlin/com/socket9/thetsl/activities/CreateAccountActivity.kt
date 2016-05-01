package com.socket9.thetsl.activities


import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.socket9.thetsl.R
import com.socket9.thetsl.managers.HttpManager
import kotlinx.android.synthetic.main.activity_create_account.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */

class CreateAccountActivity : AppCompatActivity(), AnkoLogger {

    /** Variable zone **/
    private var dialog: ProgressDialog? = null

    /** Lifecycle  zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        initInstance()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        dialog?.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }


    /** Method zone **/

    private fun initInstance() {
        setToolbar()

        btnRegister.setOnClickListener(){

            dialog = indeterminateProgressDialog(R.string.dialog_progress_create_account_content, R.string.dialog_progress_title)
            dialog?.setCancelable(false)
            dialog?.show()

            registerUser()
        }
    }

    private fun registerUser() {
        HttpManager.registerUser(etEmail.text.toString(), etUsername.text.toString(), etAddress.text.toString(), "", "")
                .subscribe ({
                    dialog?.dismiss()
                    info { it.message }
                    toast(getString(R.string.toast_activate_account))
                    finish()

                }, { error ->
                    dialog?.dismiss()
                    error.printStackTrace()

                })
    }

    private fun setToolbar(){
        supportActionBar?.title = "Create Account"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /** Listener zone **/

}
package com.socket9.thetsl.activities


import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding.widget.RxTextView
import com.socket9.thetsl.R
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.model.Model
import kotlinx.android.synthetic.main.activity_my_profile.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.indeterminateProgressDialog

/**
 * Created by Euro on 3/10/16 AD.
 */

class MyProfileActivity : AppCompatActivity(), AnkoLogger {

    /** Variable zone **/
    lateinit var myProfile: Model.Profile
    private var save: MenuItem? = null

    /** Lifecycle  zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        initInstance()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_my_profile, menu)
        save = menu?.findItem(R.id.action_save)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_save -> {
                updateProfile()
                return true
            }
        }

        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    /** Method zone **/

    private fun initInstance() {
        initToolbar()

        myProfile = intent.getParcelableExtra<Model.Profile>("myProfile")

        with(myProfile.data!!) {
            Glide.with(this@MyProfileActivity).load(pic ?: facebookPic).into(ivUser)
            etName.setText(if (nameEn.isNullOrBlank()) "Blank" else nameEn)
            etAddress.setText(if (address.isNullOrBlank()) "Blank" else address)
            etPhone.setText(if (phone.isNullOrBlank()) "Blank" else phone)
            etEmail.setText(if (email.isNullOrBlank()) "Blank" else email)
        }


        RxTextView.afterTextChangeEvents(etName)
                .subscribe { save?.isVisible = true }

        RxTextView.afterTextChangeEvents(etAddress)
                .subscribe { save?.isVisible = true }

        RxTextView.afterTextChangeEvents(etPhone)
                .subscribe { save?.isVisible = true }

        RxTextView.afterTextChangeEvents(etEmail)
                .subscribe { save?.isVisible = true }

        RxTextView.afterTextChangeEvents(etPassword)
                .subscribe { save?.isVisible = true }

    }

    private fun initToolbar(){
        supportActionBar?.title = "My Profile"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun updateProfile() {
        val progressDialog:ProgressDialog = indeterminateProgressDialog(R.string.dialog_progress_profile_content, R.string.dialog_progress_title)
        progressDialog.show()

        /* call updateProfile api */
        HttpManager.getProfile()
    }


    /** Listener zone **/

}
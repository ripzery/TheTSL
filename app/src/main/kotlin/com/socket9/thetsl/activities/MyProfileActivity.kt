package com.socket9.thetsl.activities


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.signature.MediaStoreSignature
import com.jakewharton.rxbinding.widget.RxTextView
import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.toast
import com.socket9.thetsl.extensions.validateName
import com.socket9.thetsl.extensions.validatePassword
import com.socket9.thetsl.extensions.validatePhone
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.managers.PickImageChooserManager
import com.socket9.thetsl.models.Model
import com.socket9.thetsl.utils.DialogUtil
import com.socket9.thetsl.utils.PhotoUtil
import com.socket9.thetsl.utils.SharePref
import com.socket9.thetsl.utils.ValidatorUtil
import com.soundcloud.android.crop.Crop
import kotlinx.android.synthetic.main.activity_my_profile.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.info
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.File

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */

class MyProfileActivity : ToolbarActivity(), AnkoLogger {

    /** Variable zone **/
    lateinit var myProfile: Model.Profile
    private var save: MenuItem? = null
    private var cacheCropImg: File? = null
    private var photo: Model.PhotoEntity? = null

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
        save = menu?.findItem(R.id.action_save)!!
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                if (save!!.isVisible) {
                    DialogUtil.getUpdateProfileDialog(this, MaterialDialog.SingleButtonCallback { dialog, which ->
                        setResult(RESULT_CANCELED)
                        supportFinishAfterTransition()
                    }).show()
                } else {
                    setResult(RESULT_CANCELED)
                    supportFinishAfterTransition()
                }
                return true
            }
            R.id.action_save -> {
                val requirement = etPhone.validatePhone() && etName.validateName() && etPassword.validatePassword()

                if (requirement) {
                    updateProfile()
                } else {
                    toast(ValidatorUtil.getErrorMsgUpdateProfile(etPhone.text.toString(), etPassword.text.toString(), etName.text.toString()))
                }

                return true
            }
        }

        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        info { "result code : $resultCode, requestCode : $requestCode" }

        if (resultCode == RESULT_OK) {
            if (requestCode != Crop.REQUEST_CROP) {
                cacheCropImg = File(cacheDir, "cropped")
                val destination = Uri.fromFile(cacheCropImg)
                val px = PhotoUtil.convertDpToPx(this, 128)
                Crop.of(PickImageChooserManager.getPickImageResultUri(data, this), destination).withMaxSize(px, px).asSquare().start(this)
            } else {
                handleCrop(resultCode, data!!)
            }
        }
    }

    override fun onBackPressed() {
        if (save!!.isVisible) {
            DialogUtil.getUpdateProfileDialog(this, MaterialDialog.SingleButtonCallback { dialog, which ->
                setResult(RESULT_CANCELED)
                supportFinishAfterTransition()
            }).show()
        } else {
            setResult(RESULT_CANCELED)
            supportFinishAfterTransition()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    /** Method zone **/

    private fun initInstance() {
        setupToolbar(getString(R.string.title_activity_my_profile_title))

//        myProfile = intent.getParcelableExtra<Model.Profile>("myProfile")

        myProfile = SharePref.getProfile()

        with(myProfile.data!!) {
            if (pic != null || facebookPic != null) Glide.with(this@MyProfileActivity).load(pic ?: facebookPic).into(ivUser)
            etName.setText(if (getName().isNullOrBlank()) "" else getName())
            etAddress.setText(if (address.isNullOrBlank()) "" else address)
            etPhone.setText(if (phone.isNullOrBlank()) "" else phone)
            tvEmail.text = if (email.isNullOrBlank()) "" else email
            etPassword.setText(if (password.isNullOrBlank()) "" else password)
        }

        ivUser.setOnClickListener { startActivityForResult(PickImageChooserManager.getPickCaptureChooserIntent(this), 200) }

        RxTextView.afterTextChangeEvents(etName)
                .subscribe { save?.isVisible = true }

        RxTextView.afterTextChangeEvents(etAddress)
                .subscribe { save?.isVisible = true }

        RxTextView.afterTextChangeEvents(etPhone)
                .subscribe { save?.isVisible = true }

        RxTextView.afterTextChangeEvents(etPassword)
                .subscribe { save?.isVisible = true }

    }

    private fun handleCrop(resultCode: Int, result: Intent) {
        if (resultCode == RESULT_OK) {
            save?.isVisible = true
            val px: Int = PhotoUtil.convertDpToPx(this, 128)

            Glide.with(this).load(Crop.getOutput(result))
                    .asBitmap()
                    .signature(MediaStoreSignature("image/jpeg", cacheCropImg!!.lastModified(), 0))
                    .into(object : SimpleTarget<Bitmap>(px, px) {
                        override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                            uploadPhoto(PhotoUtil.compressThenEncoded(resource!!, 100))
                        }
                    })
        } else if (resultCode == Crop.RESULT_ERROR) {
            toast(Crop.getError(result).message!!)
        }
    }

    private fun updateProfile() {
        val progressDialog: ProgressDialog = indeterminateProgressDialog(R.string.dialog_progress_profile_content, R.string.dialog_progress_title)
        progressDialog.setCancelable(false)
        progressDialog.show()

        val picturePath = photo?.pathSave ?: ""

        /* call updateProfile api */
        HttpManager.updateProfile(etName.text.toString(), etName.text.toString(), etPassword.text.toString(), etPhone.text.toString(), etAddress.text.toString(), picturePath)
                .compose(this.bindToLifecycle<Model.BaseModel>())
                .subscribe ({
                    progressDialog.dismiss()
                    toast(it.message)
                    setResult(RESULT_OK)
                    supportFinishAfterTransition()
                }, { error ->
                    progressDialog.dismiss()
                    error.printStackTrace()
                    toast(getString(R.string.toast_internet_connection_problem))

                })
    }

    private fun uploadPhoto(imagePath: String) {
        HttpManager.uploadPhoto(imagePath)
//                .bindToLifecycle(this)
                .compose(this.bindToLifecycle<Model.Photo>())
                .subscribe ({
                    photo = it.data
                    Glide.with(this).load(photo?.pathUse).centerCrop().into(ivUser)
                }, { error ->
                    toast(getString(R.string.toast_internet_connection_problem))
                })
    }

    /** Listener zone **/

}
package com.socket9.thetsl.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.socket9.thetsl.R
import com.socket9.thetsl.activities.MyProfileActivity
import com.socket9.thetsl.extensions.toast
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.models.Model
import com.trello.rxlifecycle.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import rx.Subscription

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */
class HomeFragment : RxFragment(), AnkoLogger {

    /** Variable zone **/
    lateinit var param1: String
    lateinit var myProfile: Model.Profile;
    private var getProfileSubscriber: Subscription? = null


    /** Static method zone **/
    companion object {
        val ARG_1 = "ARG_1"
        val REQUEST_MY_PROFILE: Int = 200

        fun newInstance(param1: String): HomeFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val homeFragment: HomeFragment = HomeFragment()
            homeFragment.arguments = bundle
            return homeFragment
        }

    }

    /** Activity method zone  **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            /* if newly created */
            param1 = arguments.getString(ARG_1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_home, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        getProfileSubscriber?.unsubscribe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_MY_PROFILE -> getProfile()
            }
        }

    }

    /** Method zone **/

    private fun initInstance() {
        getProfile()
        ivUser.setOnClickListener {
            startActivityForResult(Intent(activity, MyProfileActivity::class.java).putExtra("myProfile", myProfile), REQUEST_MY_PROFILE)
        }
    }

    private fun getProfile() {
        var dialog = indeterminateProgressDialog (R.string.dialog_progress_profile_content, R.string.dialog_progress_title)
        dialog.setCancelable(false)
        dialog.show()
        getProfileSubscriber = HttpManager.getProfile()
                .compose(this.bindToLifecycle<Model.Profile>())
                .doOnNext {
                    if (!it.result && it.message != null) toast(it.message)
                }
                .subscribe({
                    dialog.dismiss()
                    myProfile = it
                    Glide.with(this).load(it.data?.pic ?: it.data?.facebookPic).centerCrop().into(ivUser)
                    tvName.text = it.data?.getName()
                    info { it }
                }, { error ->
                    dialog.dismiss()
                    error { error }
                })
    }
}
package com.socket9.thetsl.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.socket9.thetsl.R
import com.socket9.thetsl.activities.MyProfileActivity
import com.socket9.thetsl.extensions.toast
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.model.Model
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.startActivity

/**
 * Created by Euro on 3/10/16 AD.
 */
class HomeFragment : Fragment() , AnkoLogger{

    /** Variable zone **/
    lateinit var param1: String
    lateinit var myProfile: Model.Profile;

    /** Static method zone **/
    companion object {
        val ARG_1 = "ARG_1"

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

    /** Method zone **/

    private fun initInstance() {
        toast("HomeFragment")
        getProfile()
        ivUser.setOnClickListener {
            startActivity<MyProfileActivity>("myProfile" to myProfile)
        }
    }

    private fun getProfile() {
        var dialog = indeterminateProgressDialog ( "Getting profile..." )
        dialog.show()
        HttpManager.getProfile()
                .doOnNext {
                    if(!it.result && it.message != null ) toast(it.message)
                }
                .subscribe({
                    dialog.dismiss()
                    myProfile = it
                    Glide.with(this).load(it.data?.pic ?: it.data?.facebookPic).centerCrop().into(ivUser)
                    tvName.text = it.data?.nameEn
                    info { it }
                }, { error ->
                    dialog.dismiss()
                    error { error }
                })
    }
}
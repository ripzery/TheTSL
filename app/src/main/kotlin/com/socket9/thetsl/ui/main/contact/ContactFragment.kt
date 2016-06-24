package com.socket9.thetsl.ui.main.contact

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.applyTransition
import com.socket9.thetsl.extensions.toast
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.models.Model
import com.trello.rxlifecycle.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_contact.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.*
import rx.Observable
import rx.Subscription

/**
* Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
*/

class ContactFragment : RxFragment(), AnkoLogger, ContactAdapter.ContactInteractionListener {


    /** Variable zone **/
    lateinit var param1: String
    private val BASE_ID = 1000000
    private var contactSubscription: Subscription? = null
    private var progressDialog: ProgressDialog? = null


    /** Static method zone **/
    companion object {
        val ARG_1 = "ARG_1"

        fun newInstance(param1: String): ContactFragment {
            val bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val contactFragment: ContactFragment = ContactFragment()
            contactFragment.arguments = bundle
            return contactFragment
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
        val rootView: View = inflater!!.inflate(R.layout.fragment_contact, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    override fun onPause() {
        super.onPause()
        progressDialog?.dismiss()
        contactSubscription?.unsubscribe()
    }

    /** Method zone **/

    private fun initInstance() {
        progressDialog = indeterminateProgressDialog(R.string.dialog_progress_contact_content, R.string.dialog_progress_title)
        progressDialog?.setCancelable(false)
        progressDialog?.show()

        contactSubscription = loadContacts()
                .subscribe ({
                    info { it }
                    progressDialog?.dismiss()
                    val contactAdapter = ContactAdapter(addContact(it.data), this)
                    val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity)
                    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                    recyclerView.layoutManager = linearLayoutManager
                    recyclerView.adapter = contactAdapter

                }, { error ->
                    progressDialog?.dismiss()
                    error.printStackTrace()
                    info { error.message }
                    toast(getString(R.string.toast_internet_connection_problem))
                })
    }

    private fun loadContacts(): Observable<Model.ListContacts> {
        return HttpManager.getListContact()
    }

    private fun addContact(listContact: MutableList<Model.ContactEntity>): MutableList<Model.ContactEntity> {
        listContact.add(Model.ContactEntity(BASE_ID + 1, getString(R.string.contact_us_email), "services@tsl.co.th", R.drawable.ic_email_grey_500_24dp))
        listContact.add(Model.ContactEntity(BASE_ID + 2, getString(R.string.contact_us_call_center), "02-269-9999", R.drawable.call_grey))
//        listContact.add(Model.ContactEntity(BASE_ID + 3, getString(R.string.contact_us_website), "www.tsl.co.th", R.drawable.www_grey))
        listContact.add(Model.ContactEntity(BASE_ID + 4, getString(R.string.contact_us_facebook), "TSL Auto Corporation", R.drawable.fb_grey))
        listContact.add(Model.ContactEntity(BASE_ID + 5, getString(R.string.contact_us_instagram), "TSL_Auto", R.drawable.ig_grey))
        return listContact
    }

    /** Listener zone **/
    override fun onContactClicked(index: Int, model: Model.ContactEntity) {
        if (model.id < BASE_ID) {
            progressDialog = indeterminateProgressDialog(R.string.dialog_progress_contact_content, R.string.dialog_progress_title)
            progressDialog?.setCancelable(false)
            progressDialog?.show()

            HttpManager.getContact(model.id)
//                    .bindToLifecycle(this)
                    .subscribe({
                        progressDialog?.dismiss()
                        val contact = it.data.copy(titleEn = model.titleEn, subTitle = model.subTitle)
                        startActivity<BranchDetailActivity>("contact" to contact)
                        applyTransition(R.anim.activity_forward_enter, R.anim.activity_forward_exit)

                    }, {
                        progressDialog?.dismiss()
                        toast(getString(R.string.toast_internet_connection_problem))
                    })
        } else {

            val id = model.id - BASE_ID

            when (id) {
                1 -> {
                    email(model.subTitle!!)
                }
                2 -> {
                    makeCall(model.subTitle!!)
                }
                3 -> {
                    browse("http://${model.subTitle!!}")
                }
                4 -> {
                    browse("https://www.facebook.com/tslauto")
                }
                5 -> {
                    browse("https://www.instagram.com/tsl_auto/")
                }
            }
        }
    }
}
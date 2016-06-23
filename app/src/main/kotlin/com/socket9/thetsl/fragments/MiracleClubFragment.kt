package com.socket9.thetsl.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.socket9.thetsl.R
import kotlinx.android.synthetic.main.activity_news_event.*
import org.jetbrains.anko.support.v4.indeterminateProgressDialog

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */
class MiracleClubFragment : Fragment() {

    /** Variable zone **/
    lateinit var param1: String
    val URL_MIRACLE_CLUB = "http://www.tsl.co.th/en/miraclecard"

    /** Static method zone **/
    companion object {
        val ARG_1 = "ARG_1"

        fun newInstance(param1: String): MiracleClubFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val templateFragment: MiracleClubFragment = MiracleClubFragment()
            templateFragment.arguments = bundle
            return templateFragment
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
        val rootView: View = inflater!!.inflate(R.layout.fragment_miracle_club, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    /** Method zone **/

    private fun initInstance() {
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(URL_MIRACLE_CLUB)
        val progressDialog = indeterminateProgressDialog(R.string.dialog_progress_web_content, R.string.dialog_progress_title)
        progressDialog.setCancelable(false)
        progressDialog.show()

        webView.setWebViewClient(object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                progressDialog.dismiss()

//                val sendIntent = Intent()
//                sendIntent.action = Intent.ACTION_SEND
//                sendIntent.type = "text/plain"
//                sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getTitle())
//                sendIntent.putExtra(Intent.EXTRA_TEXT, url)
//                setShareIntent(sendIntent)
            }
        })
    }
}
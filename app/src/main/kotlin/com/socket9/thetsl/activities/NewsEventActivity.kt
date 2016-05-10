package com.socket9.thetsl.activities


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ShareActionProvider
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import com.socket9.thetsl.R
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.models.Model
import kotlinx.android.synthetic.main.activity_news_event.*
import org.jetbrains.anko.indeterminateProgressDialog
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */

class NewsEventActivity : AppCompatActivity() {

    /** Variable zone **/
    lateinit private var sendIntent: Intent
    private var mShareActionProvider: ShareActionProvider? = null

    /** Lifecycle  zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_event)
        initInstance()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_news_event, menu)

        val item:MenuItem = menu.findItem(R.id.menu_item_share)

        mShareActionProvider = MenuItemCompat.getActionProvider(item) as ShareActionProvider

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> finish()
            R.id.menu_item_share -> startActivity(Intent.createChooser(sendIntent, getString(R.string.share)))
        }

        return super.onOptionsItemSelected(item)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    /** Method zone **/

    private fun initInstance() {
        initToolbar()

        val isNews: Boolean = intent.getBooleanExtra("isNews", true)
        val id: Int = intent.getIntExtra("id", 1)

        if (isNews) {
            HttpManager.getEvent(id).subscribe { setInfo(it.data) }
        } else {
            HttpManager.getNews(id).subscribe { setInfo(it.data) }
        }

    }

    private fun setInfo(data: Model.NewsEventEntity) {
        with(data) {
            webView.loadUrl(getContent())
            val progressDialog = indeterminateProgressDialog(R.string.dialog_progress_web_content, R.string.dialog_progress_title)
            progressDialog.setCancelable(false)
            progressDialog.show()

            webView.setWebViewClient(object : WebViewClient() {

                override fun onPageFinished(view: WebView?, url: String?) {
                    progressDialog.dismiss()

                    sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.type = "text/plain"
                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, getTitle())
                    sendIntent.putExtra(Intent.EXTRA_TEXT, url)
                    setShareIntent(sendIntent)
                }
            })

        }
    }

    private fun setShareIntent(shareIntent: Intent) {
        mShareActionProvider?.setShareIntent(shareIntent)
    }

    private fun initToolbar() {
//        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    /** Listener zone **/

}
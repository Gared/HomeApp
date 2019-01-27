package io.github.domi04151309.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_web.*


class WebActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        val progress = findViewById<ProgressBar>(R.id.progressBar)
        val uri = intent.getStringExtra("URI")
        val title = intent.getStringExtra("title")
        val webView = findViewById<WebView>(R.id.webView)
        val webSettings = webView.settings
        Log.d(Global.LOG_TAG,uri)
        webSettings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {

            private val activity: Activity? = null

            override fun onPageFinished(view: WebView, url: String) {
                injectCSS(webView)
                progress.visibility = View.GONE
                webView.visibility = View.VISIBLE
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
                if (url.contains("github.com")) return false
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                activity!!.startActivity(intent)
                return true
            }

            override fun onReceivedError(webView: WebView, errorCode: Int, description: String, failingUrl: String) {
                webView.loadUrl("file:///android_asset/error.html")
            }
        }
        webView.loadUrl(uri)
        if (title != null)
            setTitle(title)
    }

    private fun injectCSS(webView: WebView) {
        try {
            val inputStream = assets.open("style.css")
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()
            val encoded = Base64.encodeToString(buffer, Base64.NO_WRAP)
            webView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.webView.canGoBack()) {
            this.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
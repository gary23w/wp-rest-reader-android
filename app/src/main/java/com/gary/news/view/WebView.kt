package com.gary.news.view

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gary.news.Firebase.FireStoreClass
import com.gary.news.R
import com.gary.news.model.Favorite
import com.gary.news.model.Users
import com.gary.news.utils.constant
import com.gary.news.view.adapters.ChatList
import com.gary.news.viewModel.DetailViewModel
import com.gary.news.viewModel.FavoriteViewModel
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.layout_search.*


class WebView : BaseActivity() {
    lateinit var viewModel: DetailViewModel
    private val chatListAdapter = ChatList()
    private lateinit var viewModeltwo: FavoriteViewModel



    fun updateNavigationUserDetails(user: Users) {
        Glide
            .with(this@WebView)
            .load(user.image)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_profile_icon)
            )
            .into(iv_user_image_drop)
        tv_username_drop.text = user.name
        tv_email_drop.text = user.email
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeStuff()
        setContentView(R.layout.activity_web_view)
        var searchLocation = intent.getStringExtra("urlSearch")
        if(searchLocation.isNullOrEmpty()) {
            webView.loadUrl(constant.link)
        } else {
          webView.loadUrl("https://the-latest.news/?s=$searchLocation")
            searchLocation == ""
            constant.check = true
        }
        // Get the web view settings instance
        val settings = webView.settings
        // Enable java script in web view
        settings.javaScriptEnabled = true
        // Enable and setup web view cache
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCachePath(cacheDir.path)
        // Enable zooming in web view
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = true
        // Zoom web view text
        settings.textZoom = 125
        // Enable disable images in web view
        settings.blockNetworkImage = false
        // Whether the WebView should load image resources
        settings.loadsImagesAutomatically = true
        // More web view settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true  // api 26
        }
        //settings.pluginState = WebSettings.PluginState.ON
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mediaPlaybackRequiresUserGesture = false
        // More optional settings, you can enable it by yourself
        settings.domStorageEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.setGeolocationEnabled(true)
        settings.allowUniversalAccessFromFileURLs = true
        settings.allowFileAccess = true
        // WebView settings
        webView.fitsSystemWindows = true
        /*
            if SDK version is greater of 19 then activate hardware acceleration
            otherwise activate software acceleration
        */
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        // Set web view client
        webView.webViewClient = object: WebViewClient() {
            override fun onPageStarted(
                view: android.webkit.WebView?,
                url: String?,
                favicon: Bitmap?
            ) {
                // Enable disable back forward button
                toolbar_webview.isEnabled = webView.canGoBack()
                constant.link = webView.url
            }
        }
        webView.webChromeClient = object: WebChromeClient(){
            override fun onProgressChanged(view: android.webkit.WebView?, newProgress: Int) {
                progress_bar.progress = newProgress
            }
        }

        FireStoreClass().loadUserData(this)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        viewModeltwo = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        chatListWebview.apply {
            layoutManager = LinearLayoutManager(this@WebView)
            adapter = chatListAdapter
        }
        setSupportActionBar(toolbar_webview)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_webview.setNavigationOnClickListener {
                finish()
        }
        expandable.parentLayout.setOnClickListener {
            if (expandable.isExpanded) {
                expandable.collapse()
                val intent = Intent(this, WebView::class.java)
                startActivity(intent)
                finish()
            } else {
                expandable.expand()
                observeViewModel()
            }
        }
        send_comment_webview.setOnClickListener {
            val intent = Intent(this, ReplyActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check this out.")
                intent.putExtra(Intent.EXTRA_TEXT, constant.link
                )
                startActivity(Intent.createChooser(intent, "Share with"))
            }
            R.id.action_favorite -> {
                if(constant.link != null) {
                    var title = constant.link.toString()
                    var imageLinkFav = constant.ARTICLEIMAGEURL.toString()
                    var articletitle = constant.ARTICLEDATA.toString()
                    var note =
                        Favorite(0, title, System.currentTimeMillis(), imageLinkFav, articletitle)
                    viewModeltwo.addNotes(note)
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No", Toast.LENGTH_SHORT).show()
                }
                constant.link = ""
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun observeViewModel() {
        viewModel.newsArticlesComments.observe(this, Observer { article ->
            chatListWebview.visibility = View.VISIBLE
            chatListAdapter.onAddNewsItem(article)
            chatListWebview.smoothScrollToPosition(0)
        })
    }
    override fun onBackPressed() {
        finish()
    }
}


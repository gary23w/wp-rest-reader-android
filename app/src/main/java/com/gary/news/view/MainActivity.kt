package com.gary.news.view




import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gary.news.Firebase.FireStoreClass
import com.gary.news.R
import com.gary.news.model.Users
import com.gary.news.utils.READ_STORAGE_PERMISSION_CODE
import com.gary.news.utils.constant
import com.gary.news.view.adapters.NewsListAdapter
import com.gary.news.view.adapters.TopicAdapter
import com.gary.news.viewModel.ListViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_main_drop_data.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var viewModel: ListViewModel
    val newsListAdapter = NewsListAdapter()
    private val topicListAdapter = TopicAdapter()
    private lateinit var mSharedPreferences: SharedPreferences
    private var topicCounter = 1

    fun updateNavigationUserDetails(user: Users) {
        val headerView = nav_view.getHeaderView(0)
        val navUserImage = headerView.findViewById<ImageView>(R.id.iv_user_image)
        Glide
            .with(this@MainActivity)
            .load(user.image)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_profile_icon)
            )
            .into(navUserImage)
        tv_username.text = user.name
        constant.USER = user.name.toString()
        Glide
            .with(this@MainActivity)
            .load(user.image)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_profile_icon)
            )
            .into(user_drop_main_image)
        tv_username_drop_main.text = user.name
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeStuff()
        setContentView(R.layout.activity_main)
        nav_view.setNavigationItemSelectedListener(this)
        checkPermissions()
        setupActionBar()
        FireStoreClass().loadUserData(this)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        expandableMAIN.parentLayout.setOnClickListener {
            if (expandableMAIN.isExpanded) {
                expandableMAIN.collapse()
            } else {
                expandableMAIN.expand()
            }
        }
        navigationViewMain.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.main_articles-> {
                    newsList.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = newsListAdapter
                    }
                    observeViewModel()
                }

                R.id.topics_main-> {
                    newsList.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = topicListAdapter
                    }
                    if(topicCounter == 1) {
                        topicListAdapter.onAddNewsItem(null)
                        topicCounter++
                    }
                }
                R.id.shareApp-> {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Check this out.")
                    intent.putExtra(Intent.EXTRA_TEXT, "https://the-latest.news/"
                    )
                    startActivity(Intent.createChooser(intent, "Share with"))
                }

            }
            false
        }
        getSearchString.setOnFocusChangeListener { v, hasFocus ->
            newsList.visibility = View.GONE
        }
        search_button_main.setOnClickListener {
            newsList.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = newsListAdapter
            }
           constant.SEARCH = getSearchString.text.toString()
            viewModel.searchArticles.observe(this, Observer { article ->
                newsList.visibility = View.VISIBLE
                newsListAdapter.onAddNewsItem(article)
                newsList.smoothScrollToPosition(0)
            })
        }
        newsList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = newsListAdapter
        }
        observeViewModel()
    }
    fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_CODE
            )
        }
    }
    private fun setupActionBar() {
        setSupportActionBar(toolbar_main_activity)
        toolbar_main_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        toolbar_main_activity.setNavigationOnClickListener {
            toggleDrawer()
        }
    }
    private fun observeViewModel() {
        viewModel.newsArticles.observe(this, Observer { article ->
            newsList.visibility = View.VISIBLE
            newsListAdapter.onAddNewsItem(article)
            newsList.smoothScrollToPosition(0)
        })
    }
    private fun toggleDrawer() {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.newsHome -> {
                showErrorSnackBar("Your already home.")
            }

            R.id.favorites -> {
                val intent = Intent(this, FavActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.newsProfile -> {
                val intent = Intent(this, MyProfileActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.newsSignout -> {
                showErrorSnackBar("Hope to see you soon!")
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            R.id.trendingButton -> {
                viewModel.trendingArticles.observe(this, Observer { article ->
                    newsList.visibility = View.VISIBLE
                    newsListAdapter.onAddNewsItem(article)
                    newsList.smoothScrollToPosition(0)
                })
            }
            R.id.generalButton -> {
                viewModel.generalArticles.observe(this, Observer { article ->
                    newsList.visibility = View.VISIBLE
                    newsListAdapter.onAddNewsItem(article)
                    newsList.smoothScrollToPosition(0)
                })
            }
            R.id.businessButton -> {
                viewModel.businessArticles.observe(this, Observer { article ->
                    newsList.visibility = View.VISIBLE
                    newsListAdapter.onAddNewsItem(article)
                    newsList.smoothScrollToPosition(0)
                })
            }
            R.id.scienceButton -> {
                viewModel.scienceArticles.observe(this, Observer { article ->
                    newsList.visibility = View.VISIBLE
                    newsListAdapter.onAddNewsItem(article)
                    newsList.smoothScrollToPosition(0)
                })
            }
            R.id.technologyButton -> {
                viewModel.technologyArticles.observe(this, Observer { article ->
                    newsList.visibility = View.VISIBLE
                    newsListAdapter.onAddNewsItem(article)
                    newsList.smoothScrollToPosition(0)
                })
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    fun tokenUpdateSuccess() {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putBoolean(constant.FCM_TOKEN_UPDATED, true)
        editor.apply()
        FireStoreClass().loadUserData(this@MainActivity)
    }

    fun runSearchFunction() {
        newsList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = newsListAdapter
        }
        viewModel.searchArticles.observe(this, Observer { article ->
            newsList.visibility = View.VISIBLE
            newsListAdapter.onAddNewsItem(article)
            newsList.smoothScrollToPosition(0)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        if (expandableMAIN.isExpanded) {
            expandableMAIN.collapse()
        } else {
            doubleBackToExit()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

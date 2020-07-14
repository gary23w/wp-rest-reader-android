package com.gary.news.view




import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gary.news.R
import com.gary.news.model.Favorite
import com.gary.news.view.adapters.ChatList
import com.gary.news.utils.SharedPref
import com.gary.news.utils.constant
import com.gary.news.utils.loadImage
import com.gary.news.viewModel.DetailViewModel
import com.gary.news.viewModel.FavoriteViewModel
import kotlinx.android.synthetic.main.activity_article.*


class ArticleActivity : BaseActivity() {

    lateinit var viewModel: DetailViewModel
    private lateinit var viewModeltwo: FavoriteViewModel
    private val chatListAdapter = ChatList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeStuff()
        setContentView(R.layout.activity_article)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val titleAct = intent.getStringExtra("title")
        val contentAct = intent.getStringExtra("content")
        val ImageAct = intent.getStringExtra("imageUrl")
        val timeStamp = intent.getStringExtra("date")
        newTitleArticle.text = titleAct

        Glide.with(this).load(ImageAct).into(newsIMAGE);
        timeStampArticle.text = timeStamp
        newsDataArticle.text = contentAct
        if(intent.getStringExtra("imageUrl") != null) {
            constant.ARTICLEIMAGEURL = intent.getStringExtra("imageUrl")
            constant.ARTICLEDATA = intent.getStringExtra("title")
        } else if (intent.getStringExtra("imageUrl") == null) {
            newTitleArticle.text = constant.ARTICLEDATA
            Glide.with(this).load(constant.ARTICLEIMAGEURL).into(newsIMAGE);
            boxOne.visibility = View.GONE
        }
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModeltwo = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        chatList.apply {
            layoutManager = LinearLayoutManager(this@ArticleActivity)
            adapter = chatListAdapter
        }
        shareArticle.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Check this out.")
            intent.putExtra(Intent.EXTRA_TEXT, constant.link
            )
            startActivity(Intent.createChooser(intent, "Share with"))
        }
        observeViewModel()
        setSupportActionBar(toolbar_article_activity)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_article_activity.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.go_home_article-> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.read_more-> {
                    val intent = Intent(this, WebView::class.java)
                    startActivity(intent)
                }

                R.id.reply_article-> {
                    val intent = Intent(this, ReplyActivity::class.java)
                    intent.putExtra("titleTOreply", titleAct)
                    intent.putExtra("imageUrlTOreply", ImageAct)
                    startActivity(intent)
                }

            }
            false
        }
        commentOnArticle.setOnClickListener {
            val intent = Intent(this, ReplyActivity::class.java)
            startActivity(intent)
        }
        readMoreArticle.setOnClickListener {
            val intent = Intent(this, WebView::class.java)
            startActivity(intent)
        }
    }
    private fun observeViewModel() {
        viewModel.newsArticlesComments.observe(this, Observer { article ->
            chatList.visibility = View.VISIBLE
            chatListAdapter.onAddNewsItem(article)
            chatList.smoothScrollToPosition(0)
        })
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
                if(intent.getStringExtra("imageUrl") != null) {
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
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}



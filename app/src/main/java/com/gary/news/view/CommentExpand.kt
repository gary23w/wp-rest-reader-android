package com.gary.news.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gary.news.R
import com.gary.news.utils.constant
import kotlinx.android.synthetic.main.activity_comment_expand.*


class CommentExpand : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeStuff()
        setContentView(R.layout.activity_comment_expand)
        setSupportActionBar(toolbar_comment_expand)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        val Content = intent.getStringExtra("content")
        val Author = intent.getStringExtra("author")
        val Post = intent.getStringExtra("post")
        contentText.text = Content
        authorText.text = Author
        toolbar_comment_expand.setNavigationOnClickListener {
            val intent = Intent(this, ArticleActivity::class.java)
            startActivity(intent)
            finish()
        }
            buttonGotoLink.setOnClickListener {
                constant.link = Post
                val intent = Intent(this, WebView::class.java)
                startActivity(intent)
            }
        buttonReply.setOnClickListener {
            val intent = Intent(this, ReplyActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
}
package com.gary.news.view

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gary.news.Firebase.FireStoreClass
import com.gary.news.R
import com.gary.news.model.ChatResponse
import com.gary.news.model.NewsRepository
import com.gary.news.model.Users
import com.gary.news.utils.constant
import kotlinx.android.synthetic.main.activity_article.*
import kotlinx.android.synthetic.main.activity_comment_expand.*
import kotlinx.android.synthetic.main.activity_reply.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReplyActivity : BaseActivity() {
    fun updateCommentReplyData(user: Users) {
        Glide
            .with(this@ReplyActivity)
            .load(user.image)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_profile_icon)
            )
            .into(iv_user_image_reply)
        tv_username_reply.text = user.name
        constant.email = user.email
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeStuff()
        openActivityExplosion()
        setContentView(R.layout.activity_reply)
        FireStoreClass().loadUserData(this)
        setSupportActionBar(toolbar_reply)
        val titleAct = intent.getStringExtra("titleTOreply")
        val ImageAct = intent.getStringExtra("imageUrlTOreply")
        if (constant.ID.isNullOrEmpty()) {
            constant.ID == "1234"
        }
        chatBoxWork.setOnFocusChangeListener { v, hasFocus ->
            chatBoxWork.hint = "Ok"
            cardOne.visibility = View.GONE
            postButton.visibility = View.VISIBLE
        }
        postButton.setOnClickListener {
            comment()
        }
        val actionbar = supportActionBar
        supportActionBar?.setIcon(R.color.blue)
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_reply.setNavigationOnClickListener {
            finish()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.reply_menu, menu)
        return true
    }
    private fun comment() {
            if(chatBoxWork.text.isNotEmpty()) {
                NewsRepository.instance.submitComment("APP-USER@APP-USER.COM", constant.USER, constant.USER, chatBoxWork.text.toString(), constant.ID.toString())
                    .enqueue(object : Callback<ChatResponse> {
                        override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                            Log.d("no - success", "11111111111")
                        }
                        override fun onResponse(
                            call: Call<ChatResponse>,
                            response: Response<ChatResponse>
                        ) {
                            Log.d("!! - success", response.toString())
                            Toast.makeText(this@ReplyActivity, "Thank you for your support!", Toast.LENGTH_LONG).show()
                            chatBoxWork.visibility = View.GONE
                            val intent = Intent(applicationContext, ArticleActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })
            } else {
                Toast.makeText(this@ReplyActivity, "Field empty.", Toast.LENGTH_SHORT).show()
            }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.replyTopBar -> {
             comment()
            }
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        finish()
    }
}
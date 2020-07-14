package com.gary.news.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.gary.news.R
import com.gary.news.utils.constant
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeStuff()
        setContentView(R.layout.activity_settings)
        setSupportActionBar(settings_toolbar)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        settings_toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        nightmodeButton.setOnClickListener {
            sharedPreferences.edit().putString(themeKey, "nightmode").apply()
            val intent = intent // from getIntent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            finish()
            startActivity(intent)
        }
        daymodeButton.setOnClickListener {
            sharedPreferences.edit().putString(themeKey, "daymode").apply()
            val intent = intent // from getIntent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            finish()
            startActivity(intent)
        }
            reviewStuff.setOnClickListener {
            constant.link = "https://the-latest.news/contact/"
            val intent = Intent(this, WebView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
    }
    fun deleteAccount(view: View) {
        Toast.makeText(this, "Bye :*(", Toast.LENGTH_SHORT).show()
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
package com.gary.news.view


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.gary.news.Firebase.FireStoreClass
import com.gary.news.R
import com.gary.news.model.Users
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        animateLoginStuff(img_logo, container)
        Handler().postDelayed({
            var currentUserID = FireStoreClass().getCurrentUserId()
            if (currentUserID.isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }, 100L)


        register.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
            finish()
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        login.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            signInRegisteredUser()
        }
    }
    private fun signInRegisteredUser() {
        val email: String = edit_name.text.toString()
        val password: String = edit_password.text.toString()

        if (validateForm(email, password)) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FireStoreClass().loadUserData(this@SignInActivity)
                    } else {
                        Toast.makeText(
                            this@SignInActivity,
                            task.exception!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
    private fun validateForm(email: String, password: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            showErrorSnackBar("Please enter an email.")
            false
        } else if (TextUtils.isEmpty(password)) {
            showErrorSnackBar("Please enter password.")
            false
        } else {
            true
        }
    }
    fun signInSuccess(user: Users) {
        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finish()
    }
}
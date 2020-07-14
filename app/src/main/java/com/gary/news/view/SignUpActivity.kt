package com.gary.news.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.gary.news.Firebase.FireStoreClass
import com.gary.news.R
import com.gary.news.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        animateLoginStuff(img_logo, container)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setupActionBar()
    }
    fun userRegisteredSuccess(){
        showErrorSnackBar("Welcome.")
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user", "put stuff here")
        startActivity(intent)
    }
    private fun setupActionBar() {
        setSupportActionBar(toolbar_sign_up_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_backspace_24)
        }
        toolbar_sign_up_activity.setNavigationOnClickListener { onBackPressed() }
        btn_sign_up.setOnClickListener {
            registerUser()
        }
    }
    private fun registerUser() {
        val name: String = et_name.text.toString()
        val email: String = et_email.text.toString()
        val password: String = et_password.text.toString()
        if (validateForm(name, email, password)) {
            showErrorSnackBar("Please wait.")
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val firebaseUser : FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = Users(
                            firebaseUser.uid,
                            name,
                            registeredEmail
                        )
                        FireStoreClass().registerUser(this@SignUpActivity, user)

                    } else {
                        showErrorSnackBar("Error")
                    }
                }
        }
    }
    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter name")
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter email")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter password")
                false
            }
            else -> {
                true
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

}

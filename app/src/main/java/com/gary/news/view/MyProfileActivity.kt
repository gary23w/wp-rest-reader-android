package com.gary.news.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri

import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorCompat


import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gary.news.Firebase.FireStoreClass
import com.gary.news.R
import com.gary.news.utils.SharedPref
import com.gary.news.model.Users
import com.gary.news.utils.constant
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_my_profile.*
import java.io.IOException


class MyProfileActivity : BaseActivity() {

    companion object {
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }
    private var mSelectedImageFileUri: Uri? = null
    private lateinit var mUserDetails: Users
    private var mProfileImageURL: String = ""
    var prefs: SharedPref? = null
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeStuff()
        setContentView(R.layout.activity_my_profile)
        FireStoreClass().loadUserData(this)
        animateProfile(iv_user_image_profile, myProfileContainer)
        prefs = SharedPref(this)

        val auth = FirebaseAuth.getInstance()

        setSupportActionBar(toolbar_my_profile_activity)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_my_profile_activity.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        et_passWordProfile.setOnFocusChangeListener { v, hasFocus ->
            et_passWordProfile.hint = "Enter your old password"
            et_passWordProfile_retype.visibility = View.VISIBLE
            newPasswordTitle.visibility = View.VISIBLE
        }
        iv_user_image_profile.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                showImageChooser()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    READ_STORAGE_PERMISSION_CODE
                )
            }
        }
            btn_update.setOnClickListener {
                if(et_passWordProfile_retype.text!!.isNotEmpty()) {
                    val usertochange = auth.currentUser!!
                    val credential = EmailAuthProvider
                        .getCredential(mUserDetails.email, et_passWordProfile.text.toString())
                    usertochange.reauthenticate(credential)
                        .addOnCompleteListener {
                            Log.d("TAG", "User re-authenticated.")
                        }
                        .addOnFailureListener {
                            showErrorSnackBar("Something went wrong")
                        }
                    val newPassword = et_passWordProfile.text.toString()
                    usertochange!!.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("TAG", "User password updated.")
                                showErrorSnackBar("Password updated")
                            }
                        }
                }
                if (mSelectedImageFileUri != null) {
                    uploadUserImage()
                    showErrorSnackBar("Image updated")
                } else {
                    updateUserProfileData()
                    showErrorSnackBar("Data updated")
                }
            }
        setUpActionBar()
        et_about_me.setOnFocusChangeListener { v, hasFocus ->
            goAwayOne.visibility = View.GONE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageChooser()
            } else {
                Toast.makeText(this, "You need to allow permissions", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setUpActionBar() {
        setSupportActionBar(toolbar_my_profile_activity)
        val actionBar = supportActionBar
        if(actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_backspace_24)
            actionBar.title = resources.getString(R.string.my_profile_title)
        }
        toolbar_my_profile_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
    fun setUserDataInUI(user: Users) {
        mUserDetails = user
        Glide.with(this@MyProfileActivity)
            .load(prefs?.savePref.toString())
            .apply(
                RequestOptions()
                .placeholder(R.drawable.ic_profile_icon)
            )
            .into(iv_user_image_profile)
        et_name.text = user.name?.toEditable()
        et_email.text = user.email?.toEditable()
        et_about_me.text = user.aboutMe?.toEditable()
        fireID.text = user.fcmToken
    }
    private fun showImageChooser() {
        var galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE && data!!.data != null) {
            mSelectedImageFileUri = data.data
            try {
                Glide.with(this@MyProfileActivity)
                    .load(Uri.parse(mSelectedImageFileUri.toString()))
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_profile_icon)
                    )
                    .into(iv_user_image_profile)
            }catch(e: IOException) {
                showErrorSnackBar("Error")
            }
        }
    }
    private fun uploadUserImage() {
        if (mSelectedImageFileUri != null) {
            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "USER_IMAGE" + System.currentTimeMillis() + "."
                        + constant.getFileExtension(this@MyProfileActivity, mSelectedImageFileUri)
            )
            sRef.putFile(mSelectedImageFileUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    Log.e(
                        "Firebase Image URL",
                        taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                    )
                    taskSnapshot.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->
                            Log.e("Downloadable Image URL", uri.toString())
                            mProfileImageURL = uri.toString()
                            prefs?.savePref = uri.toString()
                            updateUserProfileData()
                        }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this@MyProfileActivity,
                        exception.message,
                        Toast.LENGTH_LONG
                    ).show()

                }
            FireStoreClass().loadUserData(this)
        }
    }
    private fun updateUserProfileData() {
        val userHashMap = HashMap<String, Any>()
        if (mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image) {
            userHashMap[constant.IMAGE] = mProfileImageURL
        }
        if (et_name.text.toString() != mUserDetails.name) {
            userHashMap[constant.NAME] = et_name.text.toString()
        }
        if (et_about_me.text.toString() != mUserDetails.aboutMe.toString()) {
            userHashMap[constant.ABOUTME] = et_about_me.text.toString()
        }
        FireStoreClass().updateUserProfileData(this@MyProfileActivity, userHashMap)
        FireStoreClass().loadUserData(this)
    }
    fun profileUpdateSuccess() {
        setResult(Activity.RESULT_OK)
    }
    fun animateProfile(logoImageView: ImageView, container: ViewGroup) {
        val STARTUP_DELAY = 300
        val ANIM_ITEM_DURATION = 1000
        val EDITTEXT_DELAY = 300
        val BUTTON_DELAY = 500
        val VIEW_DELAY = 400
        ViewCompat.animate(logoImageView)
            .rotation(360f)
            .translationY(-250f + 250f)
            .setStartDelay(STARTUP_DELAY.toLong())
            .setDuration(ANIM_ITEM_DURATION.toLong()).setInterpolator(
                DecelerateInterpolator(1.2f)
            ).start()
        for (i in 0 until container.childCount) {
            val v = container.getChildAt(i)
            var viewAnimator: ViewPropertyAnimatorCompat
            viewAnimator = if (v is EditText) {
                ViewCompat.animate(v)
                    .scaleY(1f).scaleX(1f)
                    .setStartDelay(EDITTEXT_DELAY * i + 500.toLong())
                    .setDuration(500)
            } else if (v is Button) {
                ViewCompat.animate(v)
                    .scaleY(1f).scaleX(1f)
                    .setStartDelay(BUTTON_DELAY * i + 500.toLong())
                    .setDuration(500)
            } else {
                ViewCompat.animate(v)
                    .translationY(50f).alpha(1f)
                    .setStartDelay(VIEW_DELAY * i + 500.toLong())
                    .setDuration(1000)
            }
            viewAnimator.setInterpolator(DecelerateInterpolator()).start()
        }
    }
    override fun onBackPressed() {
        if (goAwayOne.visibility == 8) {
            goAwayOne.visibility = View.VISIBLE
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

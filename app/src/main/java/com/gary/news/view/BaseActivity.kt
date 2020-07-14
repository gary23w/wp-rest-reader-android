package com.gary.news.view

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.transition.Explode
import android.transition.Slide
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorCompat
import com.gary.news.R
import com.gary.news.model.Topics
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false
    lateinit var sharedPreferences: SharedPreferences
    val themeKey = "currentTheme"

    fun themeStuff() {
        sharedPreferences = getSharedPreferences(
            "ThemePref",
            Context.MODE_PRIVATE
        )
        when (sharedPreferences.getString(themeKey, "red")) {
            "nightmode" ->  theme.applyStyle(R.style.nightmode, true)
            "daymode" ->  theme.applyStyle(R.style.daymode, true)
        }
    }
    fun slideInActivity() {
        val slide = Slide()
        slide.setSlideEdge(Gravity.LEFT)
        slide.setDuration(400)
        slide.setInterpolator(DecelerateInterpolator())
        window.exitTransition = slide
        window.enterTransition = slide
    }
    fun openActivityExplosion() {
        val explode = Explode()
        explode.setDuration(1000)
        explode.setInterpolator(DecelerateInterpolator())
        window.exitTransition = explode
        window.enterTransition = explode
    }
    fun animateLoginStuff(logoImageView: ImageView, container: ViewGroup) {
        val STARTUP_DELAY = 200
        val ANIM_ITEM_DURATION = 700
        val EDITTEXT_DELAY = 200
        val BUTTON_DELAY = 200
        val VIEW_DELAY = 300
        ViewCompat.animate(logoImageView)
            .translationY(-250f)
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
    fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
    fun doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
    fun showErrorSnackBar(message: String) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@BaseActivity,
                R.color.snackbar_error_color
            )
        )
        snackBar.show()
    }
    fun showAppExitDialog(string: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Please confirm")
        builder.setMessage(string)
        builder.setCancelable(true)

        builder.setPositiveButton("Yes", { _, _ ->

        })

        builder.setNegativeButton("No", { _, _ ->
            // Do something when want to stay in the app
            showErrorSnackBar("Okay.")
        })

        // Create the alert dialog using alert dialog builder
        val dialog = builder.create()

        // Finally, display the dialog when user press back button
        dialog.show()
    }
}

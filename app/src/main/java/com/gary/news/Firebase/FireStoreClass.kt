package com.gary.news.Firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.gary.news.model.Users
import com.gary.news.utils.constant
import com.gary.news.view.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: Users) {

        mFireStore.collection(constant.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
    }
    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(constant.USERS) // Collection Name
            .document(getCurrentUserId()) // Document ID
            .update(userHashMap) // A hashmap of fields which are to be updated.
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Data updated successfully!")

                // Notify the success result.

                when (activity) {
                    is MainActivity -> {
                        activity.tokenUpdateSuccess()
                    }
                    is MyProfileActivity -> {
                        activity.profileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is MainActivity -> {
                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                    }
                    is MyProfileActivity -> {
                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
    fun loadUserData(activity: Activity) {
        mFireStore.collection(constant.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.toString())
                val loggedInUser = document.toObject(Users::class.java)

                when (activity) {
                    is SignInActivity -> {
                        if (loggedInUser != null) {
                            activity.signInSuccess(loggedInUser)
                        }

                    }
                    is MainActivity -> {
                        if (loggedInUser != null) {
                            activity.updateNavigationUserDetails(loggedInUser)
                        }
                    }
                    is WebView -> {
                        if (loggedInUser != null) {
                            activity.updateNavigationUserDetails(loggedInUser)
                        }
                    }
                    is ReplyActivity -> {
                        if (loggedInUser != null) {
                            activity.updateCommentReplyData(loggedInUser)
                        }
                    }
                   is MyProfileActivity -> {
                       if (loggedInUser != null) {
                           activity.setUserDataInUI(loggedInUser)
                       }
                    }
                }
            }.addOnFailureListener { e ->
                when (activity) {
                    is SignInActivity -> {
                      Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                    }
                    is MainActivity -> {
                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
                Log.e("SignInUser", "Error writing document", e)
            }
    }
    fun getCurrentUserId(): String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }
}




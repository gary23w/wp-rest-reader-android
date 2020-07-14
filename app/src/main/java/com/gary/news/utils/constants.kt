package com.gary.news.utils

import android.app.Activity
import android.net.Uri
import android.webkit.MimeTypeMap

class constant {
    companion object {
        const val USERS: String = "Users"
        var ID: String = "1234"
        var USER: String = ""
        var link: String? = ""
        var SEARCH: String? = ""
        var ARTICLEIMAGEURL: String = ""
        var ARTICLEDATA: String = ""
        var check: Boolean = false
        /// MOCK DATA ///
        var email: String? = ""
        /////////////////////
        const val IMAGE: String = "image"
        const val NAME: String = "name"
        const val MOBILE: String = "mobile"
        const val ABOUTME: String = "about_me"
        const val PICK_IMAGE_REQUEST_CODE = 2
        const val FCM_TOKEN:String = "fcmToken"
        const val FCM_TOKEN_UPDATED:String = "fcmTokenUpdated"
        const val FCM_KEY_TITLE:String = "title"
        const val FCM_KEY_MESSAGE:String = "message"
        fun getFileExtension(activity: Activity, uri: Uri?): String? {
            return MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
        }
    }
}

package com.gary.news.utils

import android.os.Build
import android.text.Html
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gary.news.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

const val PICK_IMAGE_REQUEST_CODE = 2
const val READ_STORAGE_PERMISSION_CODE = 1
var FILE_CHECK = false


/// DATE API
var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
var date: Date = dateFormat.parse("2017-04-26T20:55:00.000Z") //You will get date object relative to server/client timezone wherever it is parsed
var formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd") //If you need time just put specific format for time like 'HH:mm:ss'
var dateStr: String = formatter.format(date)
/// DATE API


fun ImageView.loadImage(uri: String?) {
    val options = RequestOptions()
        .error(R.mipmap.ic_launcher_round)
    Glide.with(this.context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}
fun convertHtml(string: String?) =
    if(Build.VERSION.SDK_INT >= 24) {
        Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(string).toString()
    }
//parse date
fun getDateTime(s: String): String? {
    return try {
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        val netDate = Date(s.toLong() * 1000 )
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

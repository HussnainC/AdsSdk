package com.codex.googleadssdk.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

fun Context.isNetworkConnected(): Boolean {
    return try {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    } catch (ex: Exception) {
        true
    }

}

fun String.showLog(message: String) {
    Log.d(this, message)
}
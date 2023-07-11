package com.codex.googleadssdk.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

fun Context.isNetworkConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
}

fun String.showLog(message:String){
    Log.d(this,message)
}
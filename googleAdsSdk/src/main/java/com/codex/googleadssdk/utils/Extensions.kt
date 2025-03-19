package com.codex.googleadssdk.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

fun Context.isNetworkConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)

    val hasNetwork = capabilities != null
    val hasInternetCapability =
        capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    val hasValidatedInternet =
        capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true

    val hasInternet = hasNetwork && hasInternetCapability && hasValidatedInternet

    Log.d("NetworkInfoD","Network Available: $hasNetwork")
    Log.d("NetworkInfoD","Has Internet Capability: $hasInternetCapability")
    Log.d("NetworkInfoD","Has Validated Internet: $hasValidatedInternet")
    Log.d("NetworkInfoD","Final Internet Status: $hasInternet")
    return hasInternet
}


fun String.showLog(message: String) {
    Log.d(this, message)
}
package com.codex.googleadmobads

import android.app.Application
import com.codex.googleadssdk.openAd.OpenApp

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        OpenApp(
            globalClass = this,
            adIds = listOf(getString(R.string.testOpenAdId)),
            isAdAllowed = true
        )
    }
}
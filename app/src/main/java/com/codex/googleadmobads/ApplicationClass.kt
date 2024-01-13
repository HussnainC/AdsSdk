package com.codex.googleadmobads

import android.app.Application
import com.codex.googleadssdk.ads.CodecxAdsConfig
import com.codex.googleadssdk.openAd.OpenAdConfig
import com.codex.googleadssdk.openAd.OpenApp

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        OpenApp(this, getString(R.string.testOpenAdId), true)
    }
}
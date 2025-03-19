package com.codex.googleadmobads

import android.app.Application
import com.codex.googleadssdk.CodecxAd
import com.codex.googleadssdk.CodecxAdsConfig
import com.codex.googleadssdk.openAd.OpenApp

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        initAds()
        OpenApp(this, getString(R.string.testOpenAdId), true)
    }
    private fun initAds() {
        CodecxAd.initAds(
            CodecxAdsConfig.builder().setIsDebugged(true)
                .setIsYandexAllowed(false)
                .setIsGoogleAdsAllowed(true)
                .setDisableResumeAdOnClick(true)
                .setShowYandexOnGoogleAdFail(false).build(), this
        )
    }
}
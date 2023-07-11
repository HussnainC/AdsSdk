package com.codex.googleadssdk.bannerAds

import android.content.Context
import android.widget.FrameLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

class BannerAd(private val context: Context) {
    fun showBanner(isAdAllowed: Boolean, bannerLayout: FrameLayout, unitId: String) {
        if (isAdAllowed) {
            val adaptiveAds = AdaptiveAds(context)
            val adView = AdView(context)
            adView.adUnitId = unitId
            bannerLayout.addView(adView)
            val testDevices = ArrayList<String>()
            testDevices.add(AdRequest.DEVICE_ID_EMULATOR)
            val requestConfiguration = RequestConfiguration.Builder()
                .setTestDeviceIds(testDevices)
                .build()
            MobileAds.setRequestConfiguration(requestConfiguration)
            adView.setAdSize(adaptiveAds.adSize)
            adView.loadAd(AdRequest.Builder().build())
        } else {
            bannerLayout.removeAllViews()
        }

    }

    private inner class AdaptiveAds(private var contextA: Context) {
        val adSize: AdSize
            get() {
                val outMetrics = contextA.resources.displayMetrics
                val widthPixels = outMetrics.widthPixels.toFloat()
                val density = outMetrics.density
                val adWidth = (widthPixels / density).toInt()
                return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    contextA,
                    adWidth
                )
            }
    }
}
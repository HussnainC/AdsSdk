package com.codex.googleadssdk.bannerAds

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import com.codex.googleadssdk.interfaces.AdCallBack
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import java.lang.Exception

object BannerAd {
    fun showBanner(
        isAdAllowed: Boolean,
        adContainer: ViewGroup,
        unitId: String,
        context: Context, adCallBack: AdCallBack
    ) {
        if (isAdAllowed) {
            val adView = AdView(context)
            adView.adUnitId = unitId
            adView.setAdSize(getAdSize(context))
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    adContainer.removeAllViews()
                    adCallBack.onAdLoaded()
                    adContainer.addView(adView)
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    adCallBack.onAdFailToLoad(Exception(p0.message))
                    adContainer.removeAllViews()
                }
            }
            adView.loadAd(AdRequest.Builder().build())
        } else {
            adContainer.removeAllViews()
        }

    }

    private fun getAdSize(context: Context): AdSize {
        val outMetrics = context.resources.displayMetrics
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
            context,
            adWidth
        )
    }


}
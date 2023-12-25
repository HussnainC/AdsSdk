package com.codex.googleadssdk.collapsBannerAd

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.codex.googleadssdk.GDPR.UMPConsent
import com.codex.googleadssdk.bannerAds.BannerAd
import com.codex.googleadssdk.interfaces.AdCallBack
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import java.lang.Exception

object CollapseBannerAd {

    fun loadCollapseBanner(
        activity: Activity,
        isAdAllowed: Boolean,
        adContainer: ViewGroup,
        unitId: String, adCallBack: AdCallBack
    ) {
        if (isAdAllowed && UMPConsent.isUMPAllowed) {
            val extras = Bundle()
            extras.putString("collapsible", "bottom")
            val adRequest: AdRequest = AdRequest
                .Builder()
                .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                .build()
            val adView = AdView(activity)
            adView.setAdSize(getAdSize(activity))
            adView.adUnitId = unitId
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
            adView.loadAd(adRequest)
        } else {
            adContainer.removeAllViews()
        }

    }

    private fun getAdSize(context: Context): AdSize {
        val outMetrics = context.resources.displayMetrics
        val widthPixels = outMetrics.widthPixels.toFloat()
        val heightPixels = outMetrics.heightPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        val adHeight = (heightPixels / density).div(2).toInt()
        //  return AdSize(adWidth, adHeight)
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
            context,
            adWidth
        )
    }

}
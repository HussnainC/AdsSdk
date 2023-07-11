package com.codex.googleadssdk.collapsBannerAd

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class CollapseBannerAd(private val context: Context) {

    fun loadCollapseBanner(
        activity: Activity,
        isAdAllowed: Boolean,
        frameAdView: FrameLayout,
        unitId: String
    ) {
        if (isAdAllowed) {
            val extras = Bundle()
            extras.putString("collapsible", "bottom")
            val adRequest: AdRequest = AdRequest
                .Builder()
                .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                .build()
            val adView = AdView(activity)
            adView.setAdSize(getAdSize)
            adView.adUnitId = unitId
            adView.loadAd(adRequest)
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    frameAdView.addView(adView)
                }
            }
        } else {
            frameAdView.removeAllViews()
        }

    }

    private val getAdSize: AdSize
        get() {
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
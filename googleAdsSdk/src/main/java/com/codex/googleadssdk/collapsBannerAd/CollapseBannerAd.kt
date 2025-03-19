package com.codex.googleadssdk.collapsBannerAd

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import com.codex.googleadssdk.GDPR.UMPConsent
import com.codex.googleadssdk.CodecxAd
import com.codex.googleadssdk.interfaces.AdCallBack
import com.codex.googleadssdk.openAd.OpenAdConfig
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
        unitId: String, adCallBack: AdCallBack? = null
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

                    adContainer.removeAllViews()
                    adCallBack?.onAdLoaded()
                    adContainer.addView(adView)
                }

                override fun onAdClicked() {

                    if (CodecxAd.getAdConfig()?.isDisableResumeAdOnClick == true) {
                        OpenAdConfig.isOpenAdStop = true
                    }
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {

                    adCallBack?.onAdFailToLoad(Exception(p0.message))
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
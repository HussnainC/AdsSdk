package com.codex.googleadssdk.bannerAds

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import com.codex.googleadssdk.GDPR.UMPConsent
import com.codex.googleadssdk.R
import com.codex.googleadssdk.ads.CodecxAd
import com.codex.googleadssdk.interfaces.AdCallBack
import com.codex.googleadssdk.yandaxAds.YandexBannerAd
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
        unitId: String?,
        context: Context, adCallBack: AdCallBack
    ) {

        if (CodecxAd.getAdConfig()?.isYandexAllowed == true && CodecxAd.getAdConfig()?.isGoogleAdsAllowed == false) {
            YandexBannerAd.showBanner(
                isAdAllowed,
                adContainer,
             CodecxAd.getAdConfig()?.yandexAdIds?.bannerId ?: "demo-banner-yandex",
                context,
                adCallBack
            )
        } else {
            if (isAdAllowed && UMPConsent.isUMPAllowed && CodecxAd.getAdConfig()?.isGoogleAdsAllowed == true) {
                val adView = AdView(context)
                adView.adUnitId = unitId ?: "ca-app-pub-3940256099942544/6300978111"
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
                        if (CodecxAd.getAdConfig()?.isYandexAllowed == true && CodecxAd.getAdConfig()?.shouldShowYandexOnGoogleAdFail == true) {
                            YandexBannerAd.showBanner(
                                isAdAllowed,
                                adContainer,
                                CodecxAd.getAdConfig()?.yandexAdIds?.bannerId
                                ?: context.getString(R.string.yandexBannerTestId),
                                context,
                                adCallBack
                            )
                        } else {
                            adCallBack.onAdFailToLoad(Exception(p0.message))
                            adContainer.removeAllViews()
                        }
                    }
                }
                adView.loadAd(AdRequest.Builder().build())
            } else {
                adContainer.removeAllViews()
            }
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
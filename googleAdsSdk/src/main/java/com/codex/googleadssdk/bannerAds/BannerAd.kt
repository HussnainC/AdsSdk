package com.codex.googleadssdk.bannerAds

import android.content.Context
import android.os.Build
import android.view.ViewGroup
import android.webkit.WebView
import com.codex.googleadssdk.GDPR.UMPConsent
import com.codex.googleadssdk.R
import com.codex.googleadssdk.CodecxAd
import com.codex.googleadssdk.interfaces.AdCallBack
import com.codex.googleadssdk.openAd.OpenAdConfig
import com.codex.googleadssdk.yandaxAds.YandexBannerAd
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import java.lang.Exception

object BannerAd {
    fun showBanner(
        isAdAllowed: Boolean,
        adContainer: ViewGroup,
        unitId: String?,
        context: Context, adCallBack: AdCallBack? = null, type: Int = 0
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (WebView.getCurrentWebViewPackage() == null) {
                adCallBack?.onAdFailToLoad(Exception("Webview not found"))
                return
            }
        }
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
                adView.setAdSize(
                    if (type == 1) AdSize.MEDIUM_RECTANGLE else getAdSize(context)
                )
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
                            adCallBack?.onAdFailToLoad(Exception(p0.message))
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
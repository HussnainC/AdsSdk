package com.codex.googleadssdk.yandaxAds

import android.content.Context
import android.view.ViewGroup
import com.codex.googleadssdk.interfaces.AdCallBack
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

object YandexBannerAd {
    fun showBanner(
        isAdAllowed: Boolean,
        adContainer: ViewGroup,
        unitId: String,
        context: Context, adCallBack: AdCallBack?
    ) {

        if (isAdAllowed) {
            val yandexBannerAd = BannerAdView(context)
            yandexBannerAd.setAdSize(getAdSize(context))
            yandexBannerAd.setAdUnitId(unitId)
            yandexBannerAd.setBannerAdEventListener(object : BannerAdEventListener {
                override fun onAdLoaded() {
                    adContainer.removeAllViews()
                    adCallBack?.onAdLoaded()
                    adContainer.addView(yandexBannerAd)
                }

                override fun onAdFailedToLoad(p0: AdRequestError) {
                    adCallBack?.onAdFailToLoad(Exception(p0.description))
                    adContainer.removeAllViews()
                }

                override fun onAdClicked() {
                }

                override fun onLeftApplication() {
                }

                override fun onReturnedToApplication() {
                }

                override fun onImpression(p0: ImpressionData?) {
                }
            })
            yandexBannerAd.loadAd(AdRequest.Builder().build())

        } else {
            adContainer.removeAllViews()
        }

    }

    private fun getAdSize(context: Context): BannerAdSize {
        val outMetrics = context.resources.displayMetrics
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        return BannerAdSize.stickySize(context, adWidth)
    }


}
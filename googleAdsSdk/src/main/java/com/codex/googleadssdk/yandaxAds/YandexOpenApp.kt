package com.codex.googleadssdk.yandaxAds

import android.app.Activity
import com.codex.googleadssdk.R
import com.codex.googleadssdk.CodecxAd
import com.codex.googleadssdk.openAd.OpenAdConfig
import com.codex.googleadssdk.openAd.OpenAdConfig.isOpenAdLoading
import com.codex.googleadssdk.utils.LoadingUtils
import com.codex.googleadssdk.utils.showLog
import com.yandex.mobile.ads.appopenad.AppOpenAd
import com.yandex.mobile.ads.appopenad.AppOpenAdEventListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoader
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData


object YandexOpenApp {
    var appOpenAd: AppOpenAd? = null
    fun loadYandexOpenAd(context: Activity, isLoadingViewVisible: Boolean, loadingLayout: Int) {
        if (isLoadingViewVisible && !isOpenAdLoading) {
            "asdas".showLog("Yandex Show Loading")
            LoadingUtils.showAdLoadingScreen(context, loadingLayout)
        }
        isOpenAdLoading = true
        val appOpenAdLoader = AppOpenAdLoader(context)
        val appOpenAdLoadListener = object : AppOpenAdLoadListener {
            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                this@YandexOpenApp.appOpenAd = appOpenAd
                isOpenAdLoading = false
                "asdas".showLog("Open: Loaded")
                showAdIfAvailable(context)
            }

            override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                isOpenAdLoading = false
                appOpenAd = null
                "asdas".showLog("Open: ${adRequestError.description}")
                LoadingUtils.dismissScreen()
            }
        }

        appOpenAdLoader.setAdLoadListener(appOpenAdLoadListener)
        appOpenAdLoader.loadAd( AdRequestConfiguration.Builder(
            CodecxAd.getAdConfig()?.yandexAdIds?.openId ?: context.getString(
                R.string.yandexOpenTestId
            )
        ).build())



    }

    private fun showAdIfAvailable(context: Activity) {
        if (isAdAvailable()) {
            val fullScreenContentCallback = object : AppOpenAdEventListener {
                override fun onAdShown() {
                    "asdas".showLog("Open: Show")
                    OpenAdConfig.isOpenAdShowing = true

                }

                override fun onAdFailedToShow(p0: AdError) {
                    OpenAdConfig.isOpenAdShowing = false
                    "asdas".showLog("Open: Fail Show: ${p0.description}")


                    LoadingUtils.dismissScreen()
                }

                override fun onAdDismissed() {
                    appOpenAd = null
                    LoadingUtils.dismissScreen()
                    "asdas".showLog("Open: Dismiss")
                    OpenAdConfig.isOpenAdShowing = false
                }

                override fun onAdClicked() {
                }

                override fun onAdImpression(p0: ImpressionData?) {
                }
            }
            appOpenAd?.setAdEventListener(fullScreenContentCallback)
            appOpenAd?.show(context)

        } else {
            LoadingUtils.dismissScreen()
        }
    }

    private fun isAdAvailable(): Boolean {
        return appOpenAd != null
    }

}
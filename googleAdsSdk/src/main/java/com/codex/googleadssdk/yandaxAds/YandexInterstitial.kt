package com.codex.googleadssdk.yandaxAds

import android.app.Activity
import androidx.annotation.LayoutRes
import com.codex.googleadssdk.R
import com.codex.googleadssdk.ads.CodecxAd
import com.codex.googleadssdk.googleads.InterstitialAdHelper
import com.codex.googleadssdk.googleads.InterstitialAdHelper.isTimerComplete
import com.codex.googleadssdk.interfaces.AdCallBack
import com.codex.googleadssdk.utils.LoadingUtils
import com.codex.googleadssdk.utils.isNetworkConnected
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener

object YandexInterstitial {
    fun loadShowInterstitial(
        adId: String,
        adAllowed: Boolean,
        showLoadingLayout: Boolean = true,
        timerAllowed: Boolean = false,
        timerMilliSec: Long = 0L,
        @LayoutRes
        loadingLayout: Int = R.layout.inter_ad_loading_layout,
        adCallBack: AdCallBack, context: Activity
    ) {
        if (adAllowed) {
            if (!timerAllowed) {
                isTimerComplete = true
            }
            if (isTimerComplete) {
                if (context.isNetworkConnected()) {
                    if (showLoadingLayout && !InterstitialAdHelper.isInterstitialLoading) {
                        LoadingUtils.showAdLoadingScreen(context, layoutId = loadingLayout)
                    }
                    InterstitialAdHelper.isInterstitialLoading = true
                    val mInterstitialAd = InterstitialAd(context)
                    mInterstitialAd.setAdUnitId(adId)
                    val adRequest = AdRequest.Builder().build()

                    mInterstitialAd.setInterstitialAdEventListener(object :
                        InterstitialAdEventListener {
                        override fun onAdLoaded() {
                            InterstitialAdHelper.isInterstitialLoading = false
                            adCallBack.onAdLoaded()
                            mInterstitialAd.show()
                        }

                        override fun onAdFailedToLoad(p0: AdRequestError) {
                            LoadingUtils.dismissScreen()
                            InterstitialAdHelper.isInterstitialLoading = false
                            adCallBack.onAdFailToLoad(Exception(p0.description))
                        }

                        override fun onAdShown() {
                            InterstitialAdHelper.isInterstitialShowing = true
                            adCallBack.onAdShown()
                        }

                        override fun onAdDismissed() {
                            if (timerAllowed) {
                                InterstitialAdHelper.startTimer(timerMilliSec)
                            }
                            adCallBack.onAdDismiss()
                            LoadingUtils.dismissScreen()
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

                    mInterstitialAd.loadAd(adRequest)
                } else {
                    adCallBack.onAdFailToLoad(Exception("No internet found"))
                }
            } else {
                adCallBack.onAdDismiss()
            }
        } else {
            adCallBack.onAdDismiss()
        }

    }


}
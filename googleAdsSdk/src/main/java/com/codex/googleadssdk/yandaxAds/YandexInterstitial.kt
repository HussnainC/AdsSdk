package com.codex.googleadssdk.yandaxAds

import android.app.Activity
import androidx.annotation.LayoutRes
import com.codex.googleadssdk.R
import com.codex.googleadssdk.ads.CodecxAd
import com.codex.googleadssdk.googleads.InterstitialAdHelper
import com.codex.googleadssdk.googleads.InterstitialAdHelper.isTimerComplete
import com.codex.googleadssdk.interfaces.AdCallBack
import com.codex.googleadssdk.openAd.OpenAdConfig
import com.codex.googleadssdk.utils.LoadingUtils
import com.codex.googleadssdk.utils.isNetworkConnected
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader

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
                    var mInterstitialAd: InterstitialAd? = null
                    val loader = InterstitialAdLoader(context).apply {
                        setAdLoadListener(object : InterstitialAdLoadListener {
                            override fun onAdLoaded(p0: InterstitialAd) {
                                mInterstitialAd = p0
                                InterstitialAdHelper.isInterstitialLoading = false
                                adCallBack.onAdLoaded()
                                mInterstitialAd?.setAdEventListener(object :
                                    InterstitialAdEventListener {
                                    override fun onAdShown() {
                                        InterstitialAdHelper.isInterstitialShowing = true
                                        adCallBack.onAdShown()
                                    }

                                    override fun onAdFailedToShow(p0: AdError) {
                                        InterstitialAdHelper.isInterstitialShowing = false
                                        LoadingUtils.dismissScreen()
                                        adCallBack.onAdFailToShow(Exception(p0.description))
                                    }

                                    override fun onAdDismissed() {
                                        InterstitialAdHelper.isInterstitialShowing = false
                                        if (timerAllowed) {
                                            InterstitialAdHelper.startTimer(timerMilliSec)
                                        }
                                        adCallBack.onAdDismiss()
                                        LoadingUtils.dismissScreen()
                                    }

                                    override fun onAdClicked() {
                                        if (CodecxAd.getAdConfig()?.isDisableResumeAdOnClick == true) {
                                            OpenAdConfig.isOpenAdStop = true
                                        }
                                    }

                                    override fun onAdImpression(p0: ImpressionData?) {
                                    }

                                })
                                mInterstitialAd?.show(context)
                            }

                            override fun onAdFailedToLoad(p0: AdRequestError) {
                                LoadingUtils.dismissScreen()
                                InterstitialAdHelper.isInterstitialLoading = false
                                adCallBack.onAdFailToLoad(Exception(p0.description))
                            }
                        })
                    }


                    val adRequestConfiguration = AdRequestConfiguration.Builder(adId).build()
                    loader.loadAd(adRequestConfiguration)
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
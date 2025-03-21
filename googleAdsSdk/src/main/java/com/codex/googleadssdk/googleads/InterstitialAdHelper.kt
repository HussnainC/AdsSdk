package com.codex.googleadssdk.googleads

import android.app.Activity
import android.os.Build
import android.os.CountDownTimer
import android.webkit.WebView
import androidx.annotation.LayoutRes
import com.codex.googleadssdk.GDPR.UMPConsent
import com.codex.googleadssdk.R
import com.codex.googleadssdk.CodecxAd
import com.codex.googleadssdk.interfaces.AdCallBack
import com.codex.googleadssdk.openAd.OpenAdConfig
import com.codex.googleadssdk.utils.LoadingUtils
import com.codex.googleadssdk.utils.isNetworkConnected
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.codex.googleadssdk.yandaxAds.YandexInterstitial


object InterstitialAdHelper {
    var isInterstitialShowing: Boolean = false
    var isInterstitialLoading: Boolean = false
    var isTimerComplete: Boolean = true
    private var isTimerRunning: Boolean = false
    private var interstitialAd: InterstitialAd? = null

    fun loadShowInterstitial(
        adId: String?,
        adAllowed: Boolean,
        showLoadingLayout: Boolean = true,
        timerAllowed: Boolean = false,
        timerMilliSec: Long = 0L,
        @LayoutRes
        loadingLayout: Int = R.layout.inter_ad_loading_layout,
        adCallBack: AdCallBack, activity: Activity
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (WebView.getCurrentWebViewPackage() == null) {
                adCallBack?.onAdFailToLoad(java.lang.Exception("Webview not found"))
                return
            }
        }

        if (CodecxAd.getAdConfig()?.isYandexAllowed == true && CodecxAd.getAdConfig()?.isGoogleAdsAllowed == false) {
            YandexInterstitial.loadShowInterstitial(
                CodecxAd.getAdConfig()?.yandexAdIds?.interstitialId
                    ?: activity.getString(R.string.yandexInterTestId),
                adAllowed,
                showLoadingLayout,
                timerAllowed,
                timerMilliSec,
                loadingLayout,
                adCallBack,
                activity
            )
        } else {
            if (adAllowed && UMPConsent.isUMPAllowed && CodecxAd.getAdConfig()?.isGoogleAdsAllowed == true) {
                if (!timerAllowed) {
                    isTimerComplete = true
                }
                if (isTimerComplete) {
                    if (activity.isNetworkConnected()) {
                        if (showLoadingLayout) {
                            LoadingUtils.showAdLoadingScreen(activity, layoutId = loadingLayout)
                        }
                        isInterstitialLoading = true
                        InterstitialAd.load(
                            activity,
                            adId ?: "ca-app-pub-3940256099942544/1033173712",
                            AdRequest.Builder().build(),
                            object : InterstitialAdLoadCallback() {
                                override fun onAdLoaded(p0: InterstitialAd) {
                                    super.onAdLoaded(p0)
                                    interstitialAd = p0
                                    isInterstitialLoading = false
                                    adCallBack.onAdLoaded()
                                    interstitialAd?.show(activity)
                                    interstitialAd?.fullScreenContentCallback =
                                        object : FullScreenContentCallback() {
                                            override fun onAdDismissedFullScreenContent() {
                                                super.onAdDismissedFullScreenContent()
                                                isInterstitialShowing = false
                                                if (timerAllowed) {
                                                    startTimer(timerMilliSec)
                                                }
                                                adCallBack.onAdDismiss()
                                                LoadingUtils.dismissScreen()

                                            }

                                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                                super.onAdFailedToShowFullScreenContent(p0)
                                                isInterstitialShowing = false
                                                LoadingUtils.dismissScreen()
                                                adCallBack.onAdFailToShow(Exception(p0.message))
                                            }

                                            override fun onAdClicked() {

                                                if (CodecxAd.getAdConfig()?.isDisableResumeAdOnClick == true) {
                                                    OpenAdConfig.isOpenAdStop = true
                                                }
                                                adCallBack.onAdClick()
                                            }

                                            override fun onAdShowedFullScreenContent() {
                                                super.onAdShowedFullScreenContent()
                                                isInterstitialShowing = true
                                                adCallBack.onAdShown()

                                            }
                                        }
                                }

                                override fun onAdFailedToLoad(p0: LoadAdError) {

                                    if (CodecxAd.getAdConfig()?.shouldShowYandexOnGoogleAdFail == true && CodecxAd.getAdConfig()?.isYandexAllowed == true) {
                                        YandexInterstitial.loadShowInterstitial(
                                            CodecxAd.getAdConfig()?.yandexAdIds?.interstitialId
                                                ?: activity.getString(R.string.yandexInterTestId),
                                            adAllowed,
                                            showLoadingLayout,
                                            timerAllowed,
                                            timerMilliSec,
                                            loadingLayout,
                                            adCallBack,
                                            activity
                                        )
                                    } else {
                                        LoadingUtils.dismissScreen()
                                        isInterstitialLoading = false
                                        adCallBack.onAdFailToLoad(Exception(p0.message))
                                    }

                                }
                            })
                    } else {
                        adCallBack.onAdFailToLoad(Exception("No internet found"))
                    }
                } else {
                    adCallBack.onAdFailToLoad(Exception("Ad not allowed"))
                }
            } else {
                adCallBack.onAdFailToLoad(Exception("Ad not allowed"))
            }
        }

    }

    fun startTimer(timerMilliSec: Long) {
        if (!isTimerRunning) {
            isTimerRunning = true
            val timer = object : CountDownTimer(timerMilliSec, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    if (isTimerComplete)
                        isTimerComplete = false
                }

                override fun onFinish() {
                    isTimerRunning = false
                    isTimerComplete = true
                }
            }
            timer.start()
        }
    }

}
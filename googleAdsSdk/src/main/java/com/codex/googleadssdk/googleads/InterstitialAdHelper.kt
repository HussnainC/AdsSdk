package com.codex.googleadssdk.googleads

import android.app.Activity
import android.content.Context
import android.os.CountDownTimer
import androidx.annotation.LayoutRes
import com.codex.googleadssdk.R
import com.codex.googleadssdk.interfaces.AdCallBack
import com.codex.googleadssdk.utils.LoadingUtils
import com.codex.googleadssdk.utils.isNetworkConnected
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


object InterstitialAdHelper {

    var isInterstitialShowing: Boolean = false
    var isInterstitialLoading: Boolean = false

    private var isTimerComplete: Boolean = true
    private var interstitialAd: InterstitialAd? = null
    fun loadInterstitial(context: Context, adId: String) {
        if (interstitialAd == null)
            InterstitialAd.load(context, adId,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(p0: InterstitialAd) {
                        super.onAdLoaded(p0)
                        interstitialAd = p0
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        interstitialAd = null
                    }
                })
    }

    fun loadInterstitial(context: Context, adId: String, adCallBack: AdCallBack) {
        if (interstitialAd == null)
            InterstitialAd.load(context, adId,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(p0: InterstitialAd) {
                        super.onAdLoaded(p0)
                        interstitialAd = p0
                        adCallBack.onAdLoaded()
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        interstitialAd = null
                        adCallBack.onAdFailToLoad(java.lang.Exception(p0.message))
                    }
                })
        else
            adCallBack.onAdLoaded()
    }

    fun showInterstitial(activity: Activity, adCallBack: AdCallBack) {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        isInterstitialShowing = false
                        adCallBack.onAdDismiss()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        super.onAdFailedToShowFullScreenContent(p0)
                        isInterstitialShowing = false
                        adCallBack.onAdFailToShow(Exception(p0.message))
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        adCallBack.onAdClick()
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        isInterstitialShowing = true
                        adCallBack.onAdShown()
                    }
                }
            interstitialAd?.show(activity)
        } else {
            adCallBack.onAdFailToShow(java.lang.Exception("Load Ad First"))
        }
    }

    fun showInterstitial(
        activity: Activity,
        adId: String,
        reloadOnDismiss: Boolean,
        adCallBack: AdCallBack
    ) {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        isInterstitialShowing = false
                        if (reloadOnDismiss) {
                            loadInterstitial(activity, adId)
                        }
                        adCallBack.onAdDismiss()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        super.onAdFailedToShowFullScreenContent(p0)
                        isInterstitialShowing = false
                        adCallBack.onAdFailToShow(Exception(p0.message))
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        adCallBack.onAdClick()
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        isInterstitialShowing = true
                        adCallBack.onAdShown()
                    }
                }
            interstitialAd?.show(activity)
        } else {
            loadInterstitial(activity, adId, object : AdCallBack() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    showInterstitial(activity, adId, reloadOnDismiss, adCallBack)
                }

                override fun onAdFailToLoad(error: Exception) {
                    super.onAdFailToLoad(error)
                    adCallBack.onAdFailToLoad(error)
                }
            })
        }
    }

    fun loadShowInterstitial(
        adId: String,
        adAllowed: Boolean,
        showLoadingLayout: Boolean = true,
        timerAllowed: Boolean = false,
        timerMilliSec: Long = 0L,
        @LayoutRes
        loadingLayout: Int = R.layout.inter_ad_loading_layout,
        adCallBack: AdCallBack, activity: Activity
    ) {
        if (adAllowed) {
            if (!timerAllowed) {
                isTimerComplete = true
            }
            if (isTimerComplete) {
                if (activity.isNetworkConnected()) {

                    if (showLoadingLayout) {
                        LoadingUtils.showAdLoadingScreen(activity, layoutId = loadingLayout)
                    }
                    isInterstitialLoading = true
                    InterstitialAd.load(activity, adId,
                        AdRequest.Builder().build(),
                        object : InterstitialAdLoadCallback() {
                            override fun onAdLoaded(p0: InterstitialAd) {
                                super.onAdLoaded(p0)
                                interstitialAd = p0
                                isInterstitialLoading = false
                                adCallBack.onAdLoaded()
                                interstitialAd?.fullScreenContentCallback =
                                    object : FullScreenContentCallback() {
                                        override fun onAdDismissedFullScreenContent() {
                                            super.onAdDismissedFullScreenContent()
                                            isInterstitialShowing = false
                                            if (timerAllowed) {
                                                startTimer(timerMilliSec)
                                            }
                                            adCallBack.onAdDismiss()
                                        }

                                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                            super.onAdFailedToShowFullScreenContent(p0)
                                            isInterstitialShowing = false
                                            if (showLoadingLayout) {
                                                LoadingUtils.dismissScreen()
                                            }
                                            adCallBack.onAdFailToShow(Exception(p0.message))
                                        }

                                        override fun onAdClicked() {
                                            super.onAdClicked()
                                            adCallBack.onAdClick()
                                        }

                                        override fun onAdShowedFullScreenContent() {
                                            super.onAdShowedFullScreenContent()
                                            isInterstitialShowing = true
                                            if (showLoadingLayout) {
                                                LoadingUtils.dismissScreen()
                                            }
                                            adCallBack.onAdShown()
                                        }
                                    }
                                interstitialAd?.show(activity)
                            }

                            override fun onAdFailedToLoad(p0: LoadAdError) {
                                super.onAdFailedToLoad(p0)
                                isInterstitialLoading = false
                                adCallBack.onAdFailToLoad(Exception(p0.message))
                            }
                        })
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

    private fun startTimer(timerMilliSec: Long) {
        val timer = object : CountDownTimer(timerMilliSec, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (isTimerComplete)
                    isTimerComplete = false
            }

            override fun onFinish() {
                isTimerComplete = true
            }
        }
        timer.start()
    }

}
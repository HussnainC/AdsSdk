package com.codex.googleadssdk.openAd

import android.app.Activity
import android.util.Log
import com.codex.googleadssdk.googleads.InterstitialAdHelper
import com.codex.googleadssdk.interfaces.AdCallBack
import com.codex.googleadssdk.utils.LoadingUtils
import com.codex.googleadssdk.utils.isNetworkConnected
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd

object OpenAdConfig {
    private var appOpenAd: AppOpenAd? = null
    private var isOpenAdAllowed: Boolean = false
    var isOpenAdLoading: Boolean = false
    var isOpenAdShowing: Boolean = false
    fun enableResumeAd() {
        isOpenAdAllowed = true
    }

    fun disableResumeAd() {
        isOpenAdAllowed = false
    }

    fun isAdEnable() = isOpenAdAllowed


    fun fetchAd(activity: Activity, adId: String, adCallBack: AdCallBack? = null) {
        activity.let {
            if (it.isNetworkConnected()) {
                if (!InterstitialAdHelper.isInterstitialLoading && !InterstitialAdHelper.isInterstitialShowing && !isOpenAdLoading && !isOpenAdShowing) {
                    isOpenAdLoading = true
                    val loadCallback: AppOpenAd.AppOpenAdLoadCallback =
                        object : AppOpenAd.AppOpenAdLoadCallback() {
                            override fun onAdLoaded(ad: AppOpenAd) {
                                isOpenAdLoading = false
                                appOpenAd = ad
                                adCallBack?.onAdLoaded()
                                loadAdShowAd(activity, adCallBack)
                            }

                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                isOpenAdLoading = false
                                adCallBack?.onAdFailToLoad(java.lang.Exception(loadAdError.message))
                            }
                        }
                    val request: AdRequest = getAdRequest()
                    AppOpenAd.load(
                        it,
                        adId,
                        request,
                        loadCallback
                    )
                }
            }

        }
    }

    fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }

    private fun isAdAvailable(): Boolean {
        return appOpenAd != null
    }

    private fun loadAdShowAd(activity: Activity, adCallBack: AdCallBack?) {
        if (isAdAvailable()) {
            val fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    isOpenAdShowing = false
                    adCallBack?.onAdDismiss()

                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    isOpenAdShowing = false
                    adCallBack?.onAdFailToShow(java.lang.Exception(p0.message))
                }

                override fun onAdShowedFullScreenContent() {
                    isOpenAdShowing = true
                    adCallBack?.onAdShown()

                }
            }
            appOpenAd?.fullScreenContentCallback = fullScreenContentCallback
            activity.let {
                try {
                    appOpenAd?.show(it)
                } catch (ex: Exception) {
                    Log.d("", ex.message.toString())
                }
            }
        } else {
            LoadingUtils.dismissScreen()
        }
    }
}
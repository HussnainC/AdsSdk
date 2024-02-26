package com.codex.googleadssdk.ads

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.codex.googleadssdk.GDPR.UMPConsent
import com.codex.googleadssdk.R
import com.codex.googleadssdk.bannerAds.BannerAd
import com.codex.googleadssdk.collapsBannerAd.CollapseBannerAd
import com.codex.googleadssdk.googleads.InterstitialAdHelper
import com.codex.googleadssdk.interfaces.AdCallBack
import com.codex.googleadssdk.openAd.OpenAdConfig
import com.codex.googleadssdk.utils.LoadingUtils
import com.codex.googleadssdk.utils.isNetworkConnected
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration


object CodecxAd {

    private var adConfig: CodecxAdsConfig? = null

    fun getAdConfig(): CodecxAdsConfig? {
        return this.adConfig
    }

    fun initAds(adsConfig: CodecxAdsConfig, context: Context) {
        this.adConfig = adsConfig
        if (adsConfig.isGoogleAdsAllowed) {
            if (adsConfig.isDebugged) {
                val requestConfiguration = if (adsConfig.testDevices.isNotEmpty()) {
                    adsConfig.testDevices.toMutableList().add(AdRequest.DEVICE_ID_EMULATOR)
                    RequestConfiguration.Builder()
                        .setTestDeviceIds(adsConfig.testDevices).build()
                } else {
                    RequestConfiguration.Builder()
                        .setTestDeviceIds(listOf(AdRequest.DEVICE_ID_EMULATOR)).build()
                }
                MobileAds.setRequestConfiguration(requestConfiguration)
            }
            MobileAds.initialize(context)
        }

    }


    fun showInterstitial(
        adId: String,
        adAllowed: Boolean,
        startTimer: Boolean,
        timerMilliSec: Long,
        showLoadingLayout: Boolean,
        @LayoutRes
        loadingLayout: Int,
        activity: Activity,
        adCallBack: AdCallBack
    ) {
        InterstitialAdHelper.loadShowInterstitial(
            adId,
            adAllowed,
            showLoadingLayout,
            startTimer,
            timerMilliSec,
            loadingLayout,
            adCallBack, activity = activity
        )
    }


    fun showInterstitial(
        adId: String,
        adAllowed: Boolean,
        startTimer: Boolean,
        showLoadingLayout: Boolean,
        timerMilliSec: Long,
        activity: Activity,
        adCallBack: AdCallBack
    ) {
        InterstitialAdHelper.loadShowInterstitial(
            adId = adId,
            adAllowed = adAllowed,
            timerMilliSec = timerMilliSec,
            showLoadingLayout = showLoadingLayout,
            timerAllowed = startTimer,
            adCallBack = adCallBack,
            activity = activity
        )
    }

    fun showInterstitial(
        adId: String,
        adAllowed: Boolean,
        showLoadingLayout: Boolean,
        activity: Activity,
        adCallBack: AdCallBack
    ) {
        InterstitialAdHelper.loadShowInterstitial(
            adId = adId,
            adAllowed = adAllowed,
            adCallBack = adCallBack,
            activity = activity,
            showLoadingLayout = showLoadingLayout
        )
    }

    fun showInterstitial(
        adId: String,
        adAllowed: Boolean,
        activity: Activity,
        adCallBack: AdCallBack
    ) {
        InterstitialAdHelper.loadShowInterstitial(
            adId = adId,
            adAllowed = adAllowed,
            adCallBack = adCallBack, activity = activity
        )
    }


    fun showBannerAd(
        adId: String,
        adAllowed: Boolean,
        @LayoutRes loadingLayout: Int,
        adContainer: ViewGroup,
        activity: Activity,
        adCallBack: AdCallBack
    ) {
        adContainer.removeAllViews()
        if (activity.isNetworkConnected()) {
            adContainer.addView(LayoutInflater.from(activity).inflate(loadingLayout, null, false))
            BannerAd.showBanner(adAllowed, adContainer, adId, activity, adCallBack)
        }
    }

    fun showBannerAd(
        adId: String,
        adAllowed: Boolean,
        @LayoutRes loadingLayout: Int,
        adContainer: ViewGroup,
        activity: Activity
    ) {
        adContainer.removeAllViews()
        if (activity.isNetworkConnected()) {
            adContainer.addView(LayoutInflater.from(activity).inflate(loadingLayout, null, false))
            BannerAd.showBanner(adAllowed, adContainer, adId, activity, object : AdCallBack() {})
        }
    }


    fun showCollapseBannerAd(
        adId: String,
        adAllowed: Boolean,
        @LayoutRes loadingLayout: Int,
        adContainer: ViewGroup,
        activity: Activity,
        adCallBack: AdCallBack
    ) {
        adContainer.removeAllViews()
        if (activity.isNetworkConnected()) {
            adContainer.addView(LayoutInflater.from(activity).inflate(loadingLayout, null, false))
            CollapseBannerAd.loadCollapseBanner(activity, adAllowed, adContainer, adId, adCallBack)
        }
    }

    fun showCollapseBannerAd(
        adId: String,
        adAllowed: Boolean,
        @LayoutRes loadingLayout: Int,
        adContainer: ViewGroup,
        activity: Activity
    ) {
        adContainer.removeAllViews()
        if (activity.isNetworkConnected()) {
            adContainer.addView(LayoutInflater.from(activity).inflate(loadingLayout, null, false))
            CollapseBannerAd.loadCollapseBanner(
                activity,
                adAllowed,
                adContainer,
                adId,
                object : AdCallBack() {})

        }
    }

    fun showOpenOrInterstitialAd(
        openAdId: String,
        interAdId: String,
        openAdAllowed: Boolean,
        interAdAllowed: Boolean,
        activity: Activity,
        adCallBack: AdCallBack
    ) {
        if (!activity.isNetworkConnected()) {
            adCallBack.onAdDismiss()
        } else {
            if ((openAdAllowed || interAdAllowed) && UMPConsent.isUMPAllowed) {
                LoadingUtils.showAdLoadingScreen(activity, R.layout.inter_ad_loading_layout)
            } else {
                adCallBack.onAdDismiss()
                return
            }
            if (openAdAllowed) {
                OpenAdConfig.fetchAd(activity, openAdId, object : AdCallBack() {
                    override fun onAdDismiss() {
                        super.onAdDismiss()
                        LoadingUtils.dismissScreen()
                        adCallBack.onAdDismiss()
                    }

                    override fun onAdFailToLoad(error: Exception) {
                        super.onAdFailToLoad(error)
                        if (!interAdAllowed) {
                            LoadingUtils.dismissScreen()
                            adCallBack.onAdFailToLoad(error)
                            return
                        }
                        showInterstitial(
                            interAdId,
                            interAdAllowed,
                            startTimer = false,
                            showLoadingLayout = false,
                            timerMilliSec = 0L,
                            activity,
                            object : AdCallBack() {
                                override fun onAdFailToLoad(error: Exception) {
                                    super.onAdFailToLoad(error)
                                    LoadingUtils.dismissScreen()
                                    adCallBack.onAdFailToLoad(error)
                                }

                                override fun onAdShown() {
                                    super.onAdShown()
                                    LoadingUtils.dismissScreen()
                                    adCallBack.onAdShown()
                                }

                                override fun onAdDismiss() {
                                    super.onAdDismiss()
                                    LoadingUtils.dismissScreen()
                                    adCallBack.onAdDismiss()
                                }

                                override fun onAdLoaded() {
                                    super.onAdLoaded()
                                    adCallBack.onAdLoaded()
                                }

                                override fun onAdFailToShow(error: Exception) {
                                    super.onAdFailToShow(error)
                                    LoadingUtils.dismissScreen()
                                    adCallBack.onAdFailToShow(error)
                                }
                            }
                        )

                    }
                })
            } else {
                showInterstitial(
                    interAdId,
                    interAdAllowed,
                    startTimer = false,
                    showLoadingLayout = false,
                    timerMilliSec = 0L,
                    activity,
                    object : AdCallBack() {
                        override fun onAdFailToLoad(error: Exception) {
                            super.onAdFailToLoad(error)
                            LoadingUtils.dismissScreen()
                            adCallBack.onAdFailToLoad(error)
                        }

                        override fun onAdShown() {
                            super.onAdShown()
                            LoadingUtils.dismissScreen()
                            adCallBack.onAdShown()
                        }

                        override fun onAdDismiss() {
                            super.onAdDismiss()
                            LoadingUtils.dismissScreen()
                            adCallBack.onAdDismiss()
                        }

                        override fun onAdLoaded() {
                            super.onAdLoaded()
                            adCallBack.onAdLoaded()
                        }

                        override fun onAdFailToShow(error: Exception) {
                            super.onAdFailToShow(error)
                            LoadingUtils.dismissScreen()
                            adCallBack.onAdFailToShow(error)
                        }
                    }
                )
            }

        }
    }


}
package com.codex.googleadssdk.ads

import android.app.Activity
import android.app.ActivityManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.codex.googleadssdk.bannerAds.BannerAd
import com.codex.googleadssdk.collapsBannerAd.CollapseBannerAd
import com.codex.googleadssdk.googleads.InterstitialAdHelper
import com.codex.googleadssdk.interfaces.AdCallBack
import com.codex.googleadssdk.utils.isNetworkConnected

object CodecxAd {

    fun showGoogleInterstitial(
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


    fun showGoogleInterstitial(
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
            timerAllowed = true,
            adCallBack = adCallBack,
            activity = activity
        )
    }

    fun showGoogleInterstitial(
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

    fun showGoogleInterstitial(
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


}
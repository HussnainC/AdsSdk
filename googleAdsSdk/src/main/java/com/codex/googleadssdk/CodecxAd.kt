package com.codex.googleadssdk

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.codex.googleadssdk.GDPR.UMPConsent
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume


object CodecxAd {

    private var adConfig: CodecxAdsConfig? = null

    fun getAdConfig(): CodecxAdsConfig? {
        return adConfig
    }

    fun initAds(adsConfig: CodecxAdsConfig, context: Context) {
        adConfig = adsConfig
        if (adsConfig.isGoogleAdsAllowed) {
            try {
                val requestConfiguration = if (adsConfig.testDevices.isNotEmpty()) {
                    adsConfig.testDevices.toMutableList().add(AdRequest.DEVICE_ID_EMULATOR)
                    RequestConfiguration.Builder()
                        .setTestDeviceIds(adsConfig.testDevices).build()
                } else {
                    RequestConfiguration.Builder()
                        .setTestDeviceIds(listOf(AdRequest.DEVICE_ID_EMULATOR)).build()
                }
                MobileAds.setRequestConfiguration(requestConfiguration)
                MobileAds.initialize(context)
            } catch (ex: Exception) {
                //
            }
        }
        if (adsConfig.isYandexAllowed) {
            com.yandex.mobile.ads.common.MobileAds.initialize(context) {
            }
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
            BannerAd.showBanner(adAllowed, adContainer, adId, activity, object : AdCallBack {
                override fun onAdLoaded() {

                }

                override fun onAdFailToLoad(error: Exception) {
                }

                override fun onAdShown() {
                    TODO("Not yet implemented")
                }

                override fun onAdClick() {
                    TODO("Not yet implemented")
                }

                override fun onAdDismiss() {
                    TODO("Not yet implemented")
                }

                override fun onAdFailToShow(error: Exception) {
                    TODO("Not yet implemented")
                }
            })
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
                object : AdCallBack {
                    override fun onAdLoaded() {

                    }

                    override fun onAdFailToLoad(error: Exception) {
                    }

                    override fun onAdShown() {
                    }

                    override fun onAdClick() {
                    }

                    override fun onAdDismiss() {
                    }

                    override fun onAdFailToShow(error: Exception) {
                        TODO("Not yet implemented")
                    }
                })

        }
    }

    var job: Job? = null

    fun showOpenOrInterstitialAd(
        openAdId: String,
        interAdId: String,
        openAdAllowed: Boolean,
        interAdAllowed: Boolean,
        activity: Activity,
        adCallBack: AdCallBack
    ) {
        if (!activity.isNetworkConnected()) {
            Log.e("NetworkInfoD", "No Internet")
            adCallBack.onAdFailToShow(Exception("No Internet"))
            return
        }
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            try {
                withTimeout(10_000) {
                    if (!(openAdAllowed || interAdAllowed) || !UMPConsent.isUMPAllowed) {
                        adCallBack.onAdFailToShow(Exception("Ad not available"))
                        return@withTimeout
                    }
                    LoadingUtils.showAdLoadingScreen(activity, R.layout.inter_ad_loading_layout)

                    coroutineContext.ensureActive()

                    if (openAdAllowed) {
                        val openAdSuccess = fetchOpenAd(activity, openAdId)
                        if (openAdSuccess) {
                            adCallBack.onAdShown()
                            return@withTimeout
                        }
                    }

                    if (interAdAllowed) {
                        val interstitialSuccess = fetchInterstitialAd(activity, interAdId)
                        if (interstitialSuccess) {
                            adCallBack.onAdShown()
                            return@withTimeout
                        }
                    }

                    throw Exception("Ad not available")
                }
            } catch (e: TimeoutCancellationException) {
                adCallBack.onAdFailToLoad(Exception("Ad request timed out"))
                Log.e("NetworkInfoD", "Time cancel: ${e.message}")
            } catch (e: Exception) {
                Log.e("NetworkInfoD", "Exception: ${e.message}")
                adCallBack.onAdFailToLoad(e)
            } finally {
                LoadingUtils.dismissScreen()
                job?.cancel()
            }
        }
    }


    private suspend fun fetchOpenAd(activity: Activity, adId: String): Boolean {
        return suspendCancellableCoroutine { cont ->
            OpenAdConfig.fetchAd(activity, adId, object : AdCallBack {
                override fun onAdShown() {
                    if (cont.isActive) cont.resume(true)
                }

                override fun onAdClick() {
                }

                override fun onAdDismiss() {

                    if (cont.isActive) cont.resume(true)
                }

                override fun onAdLoaded() {

                }

                override fun onAdFailToLoad(error: Exception) {

                    if (cont.isActive) cont.resume(false)
                }

                override fun onAdFailToShow(error: Exception) {

                    if (cont.isActive) cont.resume(false)
                }
            })

            cont.invokeOnCancellation {
                LoadingUtils.dismissScreen()
            }
        }
    }


    private suspend fun fetchInterstitialAd(activity: Activity, adId: String): Boolean {
        return suspendCancellableCoroutine { cont ->
            showInterstitial(
                adId, true, startTimer = false, showLoadingLayout = false, timerMilliSec = 0L,
                activity, object : AdCallBack {
                    override fun onAdShown() {

                        if (cont.isActive) cont.resume(true)
                    }

                    override fun onAdClick() {
                    }

                    override fun onAdLoaded() {

                    }

                    override fun onAdFailToLoad(error: Exception) {

                        if (cont.isActive) cont.resume(false)
                    }

                    override fun onAdDismiss() {

                        if (cont.isActive) cont.resume(true)
                    }

                    override fun onAdFailToShow(error: Exception) {

                        if (cont.isActive) cont.resume(false)
                    }
                }
            )
            cont.invokeOnCancellation {
                LoadingUtils.dismissScreen()
            }
        }
    }


}
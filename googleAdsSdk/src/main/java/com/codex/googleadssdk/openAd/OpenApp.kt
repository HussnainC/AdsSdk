package com.codex.googleadssdk.openAd


import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.codex.googleadssdk.R
import com.codex.googleadssdk.googleads.InterstitialAdHelper
import com.codex.googleadssdk.utils.LoadingUtils
import com.codex.googleadssdk.utils.isNetworkConnected
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import org.jetbrains.annotations.NotNull

class OpenApp(
    application: Application, private val adId: String,
    private val isLoadingViewVisible: Boolean,
    @LayoutRes
    private val loadingLayout: Int = R.layout.openad_loading_layout
) :
    Application.ActivityLifecycleCallbacks,
    LifecycleObserver {

    private var appOpenAd: AppOpenAd? = null
    private var currentActivity: Activity? = null
    private var isOpenAdLoading: Boolean = false
    private var isOpenAdShowing: Boolean = false

    init {
        try {
            application.registerActivityLifecycleCallbacks(this)
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        } catch (ex: RuntimeException) {
//
        }
    }


    private fun fetchAd() {
        currentActivity?.let {
            if (it.isNetworkConnected()) {
                if (!InterstitialAdHelper.isInterstitialLoading && !InterstitialAdHelper.isInterstitialShowing && !isOpenAdLoading && !isOpenAdShowing) {
                    isOpenAdLoading = true
                    if (isLoadingViewVisible) {
                        LoadingUtils.showAdLoadingScreen(it, loadingLayout)
                    }
                    val loadCallback: AppOpenAdLoadCallback = object : AppOpenAdLoadCallback() {
                        override fun onAdLoaded(ad: AppOpenAd) {
                            isOpenAdLoading = false
                            appOpenAd = ad
                            showAdIfAvailable()
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            isOpenAdLoading = false
                            LoadingUtils.dismissScreen()
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


    private fun showAdIfAvailable() {
        if (isAdAvailable()) {
            val fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    LoadingUtils.dismissScreen()
                    isOpenAdShowing = false

                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    isOpenAdShowing = false
                    LoadingUtils.dismissScreen()
                }

                override fun onAdShowedFullScreenContent() {
                    isOpenAdShowing = true

                }
            }
            appOpenAd?.fullScreenContentCallback = fullScreenContentCallback
            currentActivity?.let {
                try {
                    appOpenAd?.show(it)
                } catch (ex: Exception) {
                    Log.d("", ex.message.toString())
                }
            }
        }else{
            LoadingUtils.dismissScreen()
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onAppForegrounded() {
        currentActivity?.let {
            if (OpenAdConfig.isAdEnable()) {
                fetchAd()
            }
        }

    }


    @NotNull
    private fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }


    private fun isAdAvailable(): Boolean {
        return appOpenAd != null
    }


    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        currentActivity = p0

    }

    override fun onActivityStarted(p0: Activity) {
        currentActivity = p0

    }

    override fun onActivityResumed(p0: Activity) {
        currentActivity = p0

    }


    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStopped(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }
}
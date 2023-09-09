package com.codex.googleadssdk.openAd


import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.codex.googleadssdk.R
import com.codex.googleadssdk.enums.InterstitialAdLayout
import com.codex.googleadssdk.interstitalAd.InterstitialAdClass
import com.codex.googleadssdk.utils.Constants.isAdLoading
import com.codex.googleadssdk.utils.Constants.isInterstitialShown
import com.codex.googleadssdk.utils.Constants.isShowingAd
import com.codex.googleadssdk.utils.isNetworkConnected
import com.codex.googleadssdk.utils.showLog
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import org.jetbrains.annotations.NotNull

class SplashOpenAppWithInterstitial(
    private val mInterstitialAdClass: InterstitialAdClass,
    private val openAdIds: List<String>,
    private val interstitialAdIds: List<String>,
    private val isOpenAdAllowed: Boolean,
    private val isInterstitialAdAllowed: Boolean,
    private val showLoadingScreen: Boolean = true,
    private val loadingAdViewLayoutId: Int = InterstitialAdLayout.DefaultLayout.adLayoutId
) {
    private var dialog: Dialog? = null


    private var appOpenAd: AppOpenAd? = null

    private var fullScreenContentCallback: FullScreenContentCallback? = null

    private var adLoadingView: View? = null


    private var requestCount = 0


    private fun fetchAd(context: Activity, work: () -> Unit) {
        if (openAdIds.isNotEmpty()) {
            if (isAdAvailable()) {
                return
            }
            val loadCallback: AppOpenAdLoadCallback = object : AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    "asdpiapso".showLog("Open Ad Loaded at: ${requestCount}")
                    requestCount = 0
                    appOpenAd = ad
                    showAdIfAvailable(context, work)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    "asdpiapso".showLog("Open Fail to load ${requestCount}")
                    requestCount++
                    if (requestCount < openAdIds.size) {
                        "asdpiapso".showLog("Open Reload At ${requestCount}")
                        fetchAd(context, work)
                    } else {
                        requestCount = 0
                        mInterstitialAdClass.showInterstitialAdNew(
                            isAdAllowed = isInterstitialAdAllowed,
                            activity = context,
                            work = {
                                removeWindowView()
                                work()
                            },
                            ids = interstitialAdIds,
                            isTimerEnable = false,
                            isLoadingDialogShow = false,
                            loadingAdViewLayoutId = this@SplashOpenAppWithInterstitial.loadingAdViewLayoutId
                        )
                    }
                }
            }
            val request: AdRequest = getAdRequest()
            context.let {
                AppOpenAd.load(
                    it,
                    openAdIds[requestCount],
                    request,
                    loadCallback
                )
            }
        } else {
            work()
        }

    }

    private var work: (() -> Unit?)? = null
    fun loadAndShowOpenAd(context: Activity, work: () -> Unit) {
        this.work = work
        if (context.isNetworkConnected()) {
            context.registerReceiver(
                internetConnectivity,
                IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
            )
            if (isOpenAdAllowed && context.isNetworkConnected() && !isInterstitialShown && !isAdLoading && !isShowingAd) {
                try {
                    if (showLoadingScreen) {
                        showAdLoadingScreen(context)
                    }
                    fetchAd(context, work)
                } catch (ex: Exception) {
                    removeWindowView()
                    work()
                }
            } else if (isInterstitialAdAllowed) {
                try {
                    if (showLoadingScreen) {
                        showAdLoadingScreen(context)
                    }
                    mInterstitialAdClass.showInterstitialAdNew(
                        true,
                        context,
                        work = {
                            removeWindowView()
                            work()
                        },
                        ids = interstitialAdIds,
                        isTimerEnable = false,
                        isLoadingDialogShow = false,
                        loadingAdViewLayoutId = this.loadingAdViewLayoutId
                    )
                } catch (ex: Exception) {
                    removeWindowView()
                    work()
                }

            } else {
                removeWindowView()
                work()
            }
        } else {
            removeWindowView()
            work()
        }

    }

    private fun showAdLoadingScreen(context: Activity) {
        try {
            if (!context.isFinishing) {
                adLoadingView =
                    LayoutInflater.from(context).inflate(loadingAdViewLayoutId, null, false)
                dialog = Dialog(context, R.style.FullScreenDialog)
                adLoadingView?.let {
                    dialog?.setContentView(it)
                    val layoutParams = WindowManager.LayoutParams()
                    layoutParams.copyFrom(dialog?.window?.attributes)
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                    dialog?.window?.attributes = layoutParams
                    dialog?.setCancelable(false)
                    dialog?.show()
                }
            }

        } catch (ex: Exception) {
            "asdjap".showLog(ex.message.toString())
        }


    }


    private fun removeWindowView() {
        try {
            if (dialog?.isShowing == true) {
                dialog?.dismiss()
            }
            adLoadingView = null
        } catch (ex: java.lang.Exception) {
            "asdjap".showLog(ex.message.toString())
        }
    }

    private val internetConnectivity = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p0?.isNetworkConnected() == false) {
                removeWindowView()
                appOpenAd = null
                if (work != null) {
                    work!!()
                    p0.unregisterReceiver(this)
                }
            }
        }

    }


    private fun showAdIfAvailable(context: Activity, work: () -> Unit) {
        if (isAdAvailable()) {
            fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    removeWindowView()
                    appOpenAd = null
                    isShowingAd = false
                    work()
                    context.unregisterReceiver(internetConnectivity)


                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    isShowingAd = false
                    removeWindowView()
                    work()
                    context.unregisterReceiver(internetConnectivity)
                }

                override fun onAdShowedFullScreenContent() {
                    isShowingAd = true
                }
            }
            appOpenAd?.fullScreenContentCallback = fullScreenContentCallback
            appOpenAd?.show(context)
        } else {
            removeWindowView()
            work()
        }

    }


    @NotNull
    private fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }


    private fun isAdAvailable(): Boolean {
        return appOpenAd != null
    }


}
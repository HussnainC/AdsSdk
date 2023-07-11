package com.codex.googleadssdk.interstitalAd


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.codex.googleadssdk.R
import com.codex.googleadssdk.enums.InterstitialAdLayout
import com.codex.googleadssdk.utils.Constants.isAdLoading
import com.codex.googleadssdk.utils.Constants.isInterEnable
import com.codex.googleadssdk.utils.Constants.isInterstitialShown
import com.codex.googleadssdk.utils.isNetworkConnected
import com.codex.googleadssdk.utils.showLog
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class InterstitialAdClass(private val context: Context) {

    private var mInterstitialAd: InterstitialAd? = null
    private var adLoadingView: View? = null
    private var interRequestCount = 0
    private var loadingAdViewLayoutId: Int = InterstitialAdLayout.DefaultLayout.adLayoutId
    private var intervalTime: Long = 0L
    private var dialog: Dialog? = null


    fun startTimer() {
        val timer = object : CountDownTimer(intervalTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                isInterEnable = false
            }

            override fun onFinish() {
                isInterEnable = true
            }
        }
        timer.start()
    }

    fun showInterstitialAdNew(
        isAdAllowed: Boolean,
        activity: Activity,
        work: () -> Unit = {},
        ids: List<String>,
        isTimerEnable: Boolean = true,
        timerMilliSec: Long = 0L,
        isLoadingDialogShow: Boolean = true,
        loadingAdViewLayoutId: Int = InterstitialAdLayout.DefaultLayout.adLayoutId
    ) {
        this.intervalTime = timerMilliSec
        this.loadingAdViewLayoutId = loadingAdViewLayoutId
        if (isAdAllowed && isInterEnable) {
            if (ids.isNotEmpty()) {
                activity.let {
                    if (mInterstitialAd == null) {
                        if (it.isNetworkConnected()) {
                            isAdLoading = true
                            if (adLoadingView == null && isLoadingDialogShow)
                                showAdLoadingScreen(it)
                            InterstitialAd.load(it, ids[interRequestCount],
                                AdRequest.Builder().build(),
                                object : InterstitialAdLoadCallback() {
                                    override fun onAdFailedToLoad(ad: LoadAdError) {
                                        "asdpiapso".showLog("$interRequestCount")
                                        interRequestCount++
                                        if (interRequestCount < ids.size) {
                                            "asdpiapso".showLog("Again Requested ${interRequestCount}")
                                            showInterstitialAdNew(
                                                isAdAllowed,
                                                activity,
                                                ids = ids,
                                                work = work,
                                                isTimerEnable = isTimerEnable,
                                                timerMilliSec = timerMilliSec,
                                                isLoadingDialogShow = isLoadingDialogShow
                                            )
                                        } else {
                                            startTimer()
                                            interRequestCount = 0
                                            dismissLoadingView()
                                            isAdLoading = false
                                            mInterstitialAd = null
                                            work()
                                        }

                                    }

                                    override fun onAdLoaded(ad: InterstitialAd) {
                                        "asdpiapso".showLog("Add loaded at ${interRequestCount}")
                                        mInterstitialAd = ad
                                        interRequestCount = 0
                                        setListener(work, isTimerEnable)
                                        isAdLoading = false
                                        mInterstitialAd?.show(it)
                                        dismissLoadingView()

                                    }
                                }
                            )
                        } else {
                            work()
                        }
                    } else {
                        work()
                    }

                }
            } else {
                work()
            }
        } else {
            work()
        }

    }

    private fun dismissLoadingView() {
        try {
            if (dialog?.isShowing == true) {
                dialog?.dismiss()
            }
            adLoadingView = null
        } catch (ex: java.lang.Exception) {
            "asdjap".showLog(ex.message.toString())
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


    private fun setListener(work: () -> Unit, isTimerEnable: Boolean) {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                if (isTimerEnable)
                    startTimer()
                dismissLoadingView()
                mInterstitialAd = null
                isInterstitialShown = false
                isAdLoading = false
                work()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                isInterstitialShown = false
                isAdLoading = false
                dismissLoadingView()
                work()

            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                isInterstitialShown = true
                isAdLoading = false
                dismissLoadingView()

            }

            override fun onAdImpression() {
                super.onAdImpression()
            }
        }
    }


}
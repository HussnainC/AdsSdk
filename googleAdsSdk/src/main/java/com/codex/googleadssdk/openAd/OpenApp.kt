package com.codex.googleadssdk.openAd


import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.codex.googleadssdk.R
import com.codex.googleadssdk.enums.OpenAdLayout
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

class OpenApp(
    private val globalClass: Application,
    private val adIds: List<String>,
    private val isAdAllowed: Boolean,
    private val restrictedScreens: List<String> = listOf(),
    private val loadingAdViewLayoutId: Int = OpenAdLayout.DefaultLayout.layoutId

) :
    Application.ActivityLifecycleCallbacks,
    LifecycleObserver {
    companion object {
        var isFromPermission: Boolean = false

    }

    private var dialog: Dialog? = null


    private var appOpenAd: AppOpenAd? = null
    private var currentActivity: Activity? = null
    private var myApplication: Application? = globalClass
    private var fullScreenContentCallback: FullScreenContentCallback? = null

    private var adLoadingView: View? = null


    private var requestCount = 0


    init {
        try {
            this.myApplication?.registerActivityLifecycleCallbacks(this)
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        } catch (ex: RuntimeException) {
            Log.d("openAd", ex.message ?: "")
        }


    }


    fun fetchAd() {
        currentActivity?.let {
            if (it.isNetworkConnected()) {
                if (!isAdAllowed) {
                    return
                }
                if (!isInterstitialShown && !isAdLoading && !isShowingAd) {
                    if (dialog?.isShowing == false || dialog == null) {
                        getBackgroundLayer(it)
                    }
                    val loadCallback: AppOpenAdLoadCallback = object : AppOpenAdLoadCallback() {
                        override fun onAdLoaded(ad: AppOpenAd) {
                            requestCount = 0
                            appOpenAd = ad
                            Log.d("asdas", "Ad Loaded")
                            showAdIfAvailable()
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            requestCount++
                            if (requestCount < adIds.size) {
                                fetchAd()
                            } else {
                                requestCount = 0
                                removeLoadingView()
                            }
                        }
                    }
                    val request: AdRequest = getAdRequest()
                    AppOpenAd.load(
                        it,
                        adIds[requestCount],
                        request,
                        loadCallback
                    )
                }
            }

        }


    }

    private fun removeLoadingView() {
        try {
            if (dialog?.isShowing == true) {
                dialog?.dismiss()
            }
        } catch (ex: java.lang.Exception) {

        }

    }


    private fun showAdIfAvailable() {
        if (isAdAvailable()) {
            fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    isShowingAd = false
                    removeLoadingView()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    isShowingAd = false
                    removeLoadingView()
                }

                override fun onAdShowedFullScreenContent() {
                    isShowingAd = true
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
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onAppBackgrounded() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onAppForegrounded() {
        currentActivity?.let {
            if (restrictedScreens.isNotEmpty()) {
                if (restrictedScreens.contains(it.javaClass.name)) {
                    return
                }
                if (isFromPermission) {
                    return
                }
                fetchAd()
            } else {
                if (isFromPermission) {
                    return
                }
                fetchAd()
            }

        }

    }

    fun isActivityDialogLayout(activity: Activity): Boolean {
        val attributes = activity.window.attributes
        return attributes.type == WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
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

    private fun getBackgroundLayer(context: Activity) {
        try {
            adLoadingView = LayoutInflater.from(context).inflate(loadingAdViewLayoutId, null, false)
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
        } catch (ex: java.lang.Exception) {
            "openWindowError".showLog(ex.message.toString())

        }


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
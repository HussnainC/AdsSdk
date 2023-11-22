package com.codex.googleadssdk.rewardedAd

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.WindowManager
import com.codex.googleadssdk.R
import com.codex.googleadssdk.utils.showLog
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback


object RewardedAdHelper {
    private var dialog: Dialog? = null
    fun showAd(activity: Activity, adId: String, listener: RewardedAdListener) {
        showAdLoadingScreen(activity)
        var rewardedAd: RewardedAd? = null
        RewardedAd.load(activity, adId,
            AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    dismissLoadingView()
                    rewardedAd = null
                    listener.onFailToLoad("Ad not available.")
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    rewardedAd?.show(
                        activity
                    ) { rewardItem -> // Handle the reward.
                        val rewardAmount = rewardItem.amount
                        dismissLoadingView()
                        listener.onRewardCollected()
                    }

                }
            })
    }

    private fun showAdLoadingScreen(context: Activity) {
        try {
            if (!context.isFinishing) {
                val adLoadingView =
                    LayoutInflater.from(context)
                        .inflate(R.layout.inter_ad_loading_layout, null, false)
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

    private fun dismissLoadingView() {
        try {
            if (dialog?.isShowing == true) {
                dialog?.dismiss()
            }
        } catch (ex: java.lang.Exception) {
            "asdjap".showLog(ex.message.toString())
        }
    }

    interface RewardedAdListener {
        fun onRewardCollected()
        fun onFailToLoad(message: String)
    }
}
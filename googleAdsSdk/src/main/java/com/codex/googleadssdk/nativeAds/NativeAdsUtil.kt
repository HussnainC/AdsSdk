package com.codex.googleadssdk.nativeAds

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.codex.googleadssdk.R
import com.codex.googleadssdk.interfaces.AdCallBack
import com.codex.googleadssdk.utils.isNetworkConnected
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

object NativeAdsUtil {

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.apply {
            mediaView = findViewById(R.id.ad_media)
            headlineView = findViewById(R.id.ad_headline)
            bodyView = findViewById(R.id.ad_body)
            callToActionView = findViewById(R.id.ad_call_to_action_new)
            iconView = findViewById(R.id.ad_app_icon)
            (headlineView as TextView).text = nativeAd.headline
            nativeAd.mediaContent?.let {
                mediaView?.mediaContent = it
            }
        }
        nativeAd.body?.let { body ->
            adView.bodyView?.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = body
        } ?: run {
            adView.bodyView?.visibility = View.INVISIBLE
        }

        nativeAd.callToAction?.let { callToAction ->
            adView.callToActionView?.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = callToAction
        } ?: run {
            adView.callToActionView?.visibility = View.INVISIBLE
        }

        nativeAd.icon?.let {
            adView.iconView?.visibility = View.VISIBLE
            adView.iconView?.let { iconView ->
                (iconView as ImageView).setImageDrawable(it.drawable)
            }
        } ?: run {
            adView.iconView?.visibility = View.INVISIBLE
        }

        adView.setNativeAd(nativeAd)

        val vc = nativeAd.mediaContent?.videoController

        vc?.apply {
            if (hasVideoContent()) {
                this.mute(true)
            }
        }
    }


    fun loadNativeAd(
        isAdAllowed: Boolean,
        @LayoutRes loadingAdView: Int,
        adContainerView: ViewGroup,
        nativeId: String, context: Activity, adListener: AdCallBack? = null
    ) {
        if (isAdAllowed) {
            if (context.isNetworkConnected()) {
                val loadingView = LayoutInflater.from(context).inflate(loadingAdView, null, false)
                adContainerView.removeAllViews()
                adContainerView.addView(loadingView)

                val adLoader = AdLoader.Builder(context, nativeId)
                    .forNativeAd { nativeAd ->
                        adListener?.onNativeAdLoad(nativeAd)
                    }
                    .withAdListener(object : AdListener() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            adListener?.onAdFailToLoad(Exception(adError.message))
                        }
                    })
                    .withNativeAdOptions(
                        NativeAdOptions.Builder()
                            .setVideoOptions(VideoOptions.Builder().setStartMuted(true).build())
                            .build()
                    )
                    .build()
                adLoader.loadAd(AdRequest.Builder().build())
            } else {
                adContainerView.removeAllViews()
                adListener?.onAdFailToShow(Exception("Internet not connected"))
            }
        } else {
            adContainerView.removeAllViews()
            adListener?.onAdFailToShow(Exception("Ad not allowed"))
        }
    }

    fun loadNativeAd(
        isAdAllowed: Boolean,
        nativeId: String, context: Activity, adListener: AdCallBack? = null
    ) {
        if (isAdAllowed) {
            if (context.isNetworkConnected()) {
                val adLoader = AdLoader.Builder(context, nativeId)
                    .forNativeAd { nativeAd ->
                        adListener?.onNativeAdLoad(nativeAd)
                    }
                    .withAdListener(object : AdListener() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            adListener?.onAdFailToLoad(Exception(adError.message))
                        }
                    })
                    .withNativeAdOptions(
                        NativeAdOptions.Builder()
                            .setVideoOptions(VideoOptions.Builder().setStartMuted(true).build())
                            .build()
                    )
                    .build()
                adLoader.loadAd(AdRequest.Builder().build())
            } else {
                adListener?.onAdFailToShow(Exception("Internet not connected"))
            }
        } else {
            adListener?.onAdFailToShow(Exception("Ad not allowed"))
        }
    }

    fun populateNativeAd(
        context: Activity,
        adContainerView: ViewGroup,
        nativeAd: NativeAd, @LayoutRes nativeAdView: Int, adListener: AdCallBack?
    ) {
        adContainerView.removeAllViews()
        val nativeView = LayoutInflater.from(context).inflate(nativeAdView, null, false)
        if (nativeView is NativeAdView) {
            adListener?.onAdShown()
            adContainerView.addView(nativeView)
            populateNativeAdView(nativeAd, nativeView)
        } else {
            adListener?.onAdFailToShow(Exception("Type not match"))
        }

    }

    fun populateNativeAd(
        isAdAllowed: Boolean,
        @LayoutRes loadingAdView: Int,
        @LayoutRes nativeAdView: Int,
        adContainerView: ViewGroup,
        nativeId: String,
        context: Activity,
        adListener: AdCallBack? = null
    ) {
        loadNativeAd(
            isAdAllowed,
            loadingAdView,
            adContainerView,
            nativeId,
            context,
            object : AdCallBack() {
                override fun onNativeAdLoad(nativeAd: NativeAd) {
                    super.onNativeAdLoad(nativeAd)
                    adListener?.onNativeAdLoad(nativeAd)
                    populateNativeAd(context, adContainerView, nativeAd, nativeAdView, adListener)
                }

                override fun onAdFailToLoad(error: Exception) {
                    super.onAdFailToLoad(error)
                    adListener?.onAdFailToLoad(error)
                }

                override fun onAdFailToShow(error: Exception) {
                    super.onAdFailToShow(error)
                    adListener?.onAdFailToShow(error)
                }
            }
        )

    }

    fun populateNativeAd(
        isAdAllowed: Boolean,
        @LayoutRes nativeAdView: Int,
        adContainerView: ViewGroup,
        nativeId: String,
        context: Activity,
        adCallBack: AdCallBack?
    ) {
        loadNativeAd(
            isAdAllowed,
            nativeId,
            context,
            object : AdCallBack() {
                override fun onNativeAdLoad(nativeAd: NativeAd) {
                    super.onNativeAdLoad(nativeAd)
                    adCallBack?.onNativeAdLoad(nativeAd)
                    populateNativeAd(context, adContainerView, nativeAd, nativeAdView, adCallBack)
                }

                override fun onAdFailToLoad(error: Exception) {
                    super.onAdFailToLoad(error)
                    adCallBack?.onAdFailToLoad(error)
                }

                override fun onAdFailToShow(error: Exception) {
                    super.onAdFailToShow(error)
                    adCallBack?.onAdFailToShow(error)
                }
            }
        )

    }


}









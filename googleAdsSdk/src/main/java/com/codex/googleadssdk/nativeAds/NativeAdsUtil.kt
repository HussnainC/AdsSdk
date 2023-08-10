package com.codex.googleadssdk.nativeAds

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codex.googleadssdk.R
import com.codex.googleadssdk.enums.EnumAdType
import com.codex.googleadssdk.utils.isNetworkConnected
import com.codex.googleadssdk.utils.showLog
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

class NativeAdsUtil() {

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.apply {
            mediaView = findViewById<MediaView>(R.id.ad_media)
            headlineView = findViewById(R.id.ad_headline)
            bodyView = findViewById(R.id.ad_body)
            callToActionView = findViewById(R.id.ad_call_to_action_new)
            iconView = findViewById(R.id.ad_app_icon)
            // The headline and media content are guaranteed to be in every UnifiedNativeAd.
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

    private fun populateNativeAdViewSmall(nativeAd: NativeAd, adView: NativeAdView) {
        adView.apply {
            headlineView = findViewById(R.id.ad_headline)
            bodyView = findViewById(R.id.ad_body)
            callToActionView = findViewById(R.id.ad_call_to_action_new)
            iconView = findViewById(R.id.ad_app_icon)
            (headlineView as TextView).apply {
                text = nativeAd.headline
                isSelected = true
            }
        }

        nativeAd.body?.let { body ->
            adView.bodyView?.visibility = View.VISIBLE
            (adView.bodyView as TextView).apply {
                text = body
                isSelected = true
            }
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
            (adView.iconView as ImageView).setImageDrawable(it.drawable)
        } ?: run {
            adView.iconView?.visibility = View.INVISIBLE
        }
        adView.setNativeAd(nativeAd)

    }

    var nativeAdCount = 0
    fun loadNativeAd(
        isAdAllowed: Boolean,
        adFrame: FrameLayout,
        adType: EnumAdType,
        nativeId: String, activity: Activity
    ) {
        if (isAdAllowed) {
            if (activity.isNetworkConnected()) {
                adFrame.beVisible()
                var shimmerView: ShimmerFrameLayout? = null
                if (adType == EnumAdType.Small)
                    shimmerView = LayoutInflater.from(activity).inflate(
                        R.layout.shimmer_small_native_ad_view,
                        null,
                        false
                    ) as ShimmerFrameLayout
                else if (adType == EnumAdType.Small2)
                    shimmerView = LayoutInflater.from(activity).inflate(
                        R.layout.shimmer_native_ad_view_small_2,
                        null,
                        false
                    ) as ShimmerFrameLayout
                else if (adType == EnumAdType.Large)
                    shimmerView = LayoutInflater.from(activity).inflate(
                        R.layout.shimmer_native_ad_view,
                        null,
                        false
                    ) as ShimmerFrameLayout
                else if (adType == EnumAdType.Medium) {
                    shimmerView = LayoutInflater.from(activity).inflate(
                        R.layout.shimmer_native_ad_view_medium,
                        null,
                        false
                    ) as ShimmerFrameLayout
                } else if (adType == EnumAdType.Medium2) {
                    shimmerView = LayoutInflater.from(activity).inflate(
                        R.layout.shimmer_native_ad_view_medium_2,
                        null,
                        false
                    ) as ShimmerFrameLayout
                }

                adFrame.addView(shimmerView)
                adFrame.tag = 1
                val layoutRes = if (adType == EnumAdType.Small) {
                    R.layout.small_native_ad_view
                } else if (adType == EnumAdType.Small2) {
                    R.layout.native_ad_view_small_2
                } else if (adType == EnumAdType.Large) {
                    R.layout.native_ad_view
                } else if (adType == EnumAdType.Medium) {
                    R.layout.native_ad_view_medium
                } else
                    R.layout.native_ad_view_medium_2

                val builder = AdLoader.Builder(activity, nativeId)
                builder.forNativeAd { nativeAd ->
                    val adView =
                        LayoutInflater.from(activity)
                            .inflate(layoutRes, null, false) as NativeAdView
                    if (adType == EnumAdType.Small || adType==EnumAdType.Small2)
                        populateNativeAdViewSmall(nativeAd, adView)
                    else if (adType == EnumAdType.Large || adType == EnumAdType.Medium || adType == EnumAdType.Medium2)
                        populateNativeAdView(nativeAd, adView)
                    adFrame.removeAllViews()

                    adFrame.addView(adView)
                }

                val videoOptions = VideoOptions.Builder()
                    .setStartMuted(true)
                    .build()

                val adOptions = NativeAdOptions.Builder()
                    .setVideoOptions(videoOptions)
                    .build()

                builder.withNativeAdOptions(adOptions)

                val adLoader = builder.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        adFrame.removeAllViews()
                        adFrame.beGone()
                    }
                }).build()
                adLoader.loadAd(AdRequest.Builder().build())

            } else {
                adFrame.beGone()
                if (adFrame.childCount != 0) {
                    adFrame.removeAllViews()
                }
            }
        } else {
            adFrame.beGone()
            if (adFrame.childCount != 0) {
                adFrame.removeAllViews()
            }
        }


    }

    fun loadNativeAd(
        isAdAllowed: Boolean,
        adFrame: FrameLayout,
        adType: EnumAdType,
        nativeId: List<String>, activity: Activity
    ) {
        if (isAdAllowed) {
            if (activity.isNetworkConnected()) {
                adFrame.beVisible()
                adFrame.removeAllViews()
                var shimmerView: ShimmerFrameLayout? = null
                if (adType == EnumAdType.Small)
                    shimmerView = LayoutInflater.from(activity).inflate(
                        R.layout.shimmer_small_native_ad_view,
                        null,
                        false
                    ) as ShimmerFrameLayout
                else if (adType == EnumAdType.Small2)
                    shimmerView = LayoutInflater.from(activity).inflate(
                        R.layout.shimmer_native_ad_view_small_2,
                        null,
                        false
                    ) as ShimmerFrameLayout
                else if (adType == EnumAdType.Large)
                    shimmerView = LayoutInflater.from(activity).inflate(
                        R.layout.shimmer_native_ad_view,
                        null,
                        false
                    ) as ShimmerFrameLayout
                else if (adType == EnumAdType.Medium) {
                    shimmerView = LayoutInflater.from(activity).inflate(
                        R.layout.shimmer_native_ad_view_medium,
                        null,
                        false
                    ) as ShimmerFrameLayout
                } else if (adType == EnumAdType.Medium2) {
                    shimmerView = LayoutInflater.from(activity).inflate(
                        R.layout.shimmer_native_ad_view_medium_2,
                        null,
                        false
                    ) as ShimmerFrameLayout
                }

                adFrame.addView(shimmerView)
                adFrame.tag = 1
                val layoutRes = if (adType == EnumAdType.Small) {
                    R.layout.small_native_ad_view
                } else if (adType == EnumAdType.Small2) {
                    R.layout.native_ad_view_small_2
                } else if (adType == EnumAdType.Large) {
                    R.layout.native_ad_view
                } else if (adType == EnumAdType.Medium) {
                    R.layout.native_ad_view_medium
                } else
                    R.layout.native_ad_view_medium_2

                val builder = AdLoader.Builder(activity, nativeId[nativeAdCount])
                builder.forNativeAd { nativeAd ->
                    val adView = LayoutInflater.from(activity)
                        .inflate(layoutRes, null, false) as NativeAdView
                    if (adType == EnumAdType.Small || adType==EnumAdType.Small2)
                        populateNativeAdViewSmall(nativeAd, adView)
                    else if (adType == EnumAdType.Large || adType == EnumAdType.Medium || adType == EnumAdType.Medium2)
                        populateNativeAdView(nativeAd, adView)

                    adFrame.removeAllViews()
                    adFrame.addView(adView)
                }

                val videoOptions = VideoOptions.Builder()
                    .setStartMuted(true)
                    .build()

                val adOptions = NativeAdOptions.Builder()
                    .setVideoOptions(videoOptions)
                    .build()

                builder.withNativeAdOptions(adOptions)

                val adLoader = builder.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        nativeAdCount++
                        "nativeAds".showLog("${nativeAdCount}.....${nativeId.lastIndex}")
                        if (nativeAdCount < nativeId.size) {
                            "nativeAds".showLog("Again load at ${nativeAdCount}")
                            loadNativeAd(isAdAllowed, adFrame, adType, nativeId, activity)
                        } else {
                            "nativeAds".showLog("Fail")
                            nativeAdCount = 0
                            adFrame.removeAllViews()
                        }


                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        "nativeAds".showLog("loaded at $nativeId")
                        nativeAdCount = 0
                    }
                }).build()

                adLoader.loadAd(AdRequest.Builder().build())
            }
        } else {
            adFrame.removeAllViews()
        }


    }

    private fun View.beGone() {
        this.visibility = View.GONE
    }

    private fun View.beInvisible() {
        this.visibility = View.INVISIBLE
    }


    private fun View.beVisible() {
        this.visibility = View.VISIBLE
    }
}









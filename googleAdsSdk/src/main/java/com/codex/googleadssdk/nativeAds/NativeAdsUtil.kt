package com.codex.googleadssdk.nativeAds

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.codex.googleadssdk.GDPR.UMPConsent
import com.codex.googleadssdk.R
import com.codex.googleadssdk.ads.CodecxAd
import com.codex.googleadssdk.interfaces.AdCallBack
import com.codex.googleadssdk.utils.isNetworkConnected
import com.codex.googleadssdk.yandaxAds.YandexNativeAd
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.yandex.mobile.ads.common.AdRequestError.Code

object NativeAdsUtil {

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.apply {
            if (findViewById<View>(R.id.ad_media) != null)
                mediaView = findViewById(R.id.ad_media)
            advertiserView = findViewById(R.id.ad_advertiser)
            headlineView = findViewById(R.id.ad_headline)
            bodyView = findViewById(R.id.ad_body)
            try {
                starRatingView = findViewById(R.id.ad_stars)
            } catch (ex: Exception) {
                //
            }
            callToActionView = findViewById(R.id.ad_call_to_action_new)
            iconView = findViewById(R.id.ad_app_icon)
            (headlineView as TextView).text = nativeAd.headline
            nativeAd.mediaContent?.let {
                mediaView?.mediaContent = it
            }
        }
        nativeAd.starRating?.let { ratings ->
            adView.starRatingView?.visibility = View.VISIBLE
            if (adView.starRatingView != null) {
                (adView.starRatingView as RatingBar).rating = ratings.toFloat()
            } else {
                adView.starRatingView?.visibility = View.GONE
            }
        } ?: run {
            adView.starRatingView?.visibility = View.GONE
        }
        nativeAd.body?.let { body ->
            adView.bodyView?.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = body
        } ?: run {
            adView.bodyView?.visibility = View.INVISIBLE
        }

        nativeAd.advertiser?.let { advertiser ->
            adView.advertiserView?.visibility = View.VISIBLE
            (adView.advertiserView as TextView).text = advertiser
        } ?: run {
            adView.advertiserView?.visibility = View.GONE
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
        nativeAd.mediaContent?.let {
            val vc = it.videoController
            vc.apply {
                if (hasVideoContent()) {
                    this.mute(true)
                }
            }
        }

    }


    fun loadNativeAd(
        isAdAllowed: Boolean,
        @LayoutRes loadingAdView: Int,
        adContainerView: ViewGroup,
        nativeId: String, context: Activity, adListener: AdCallBack? = null
    ) {
        if (isAdAllowed && UMPConsent.isUMPAllowed) {
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
                            .setRequestMultipleImages(true)
                            .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                            .setRequestCustomMuteThisAd(true)
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
        if (isAdAllowed && UMPConsent.isUMPAllowed) {
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
        nativeId: String?,
        context: Activity,
        adListener: AdCallBack? = null
    ) {
        if (CodecxAd.getAdConfig()?.isYandexAllowed == true && CodecxAd.getAdConfig()?.isGoogleAdsAllowed == false) {
            populateYandexNative(
                isAdAllowed,
                loadingAdView,
                nativeAdView,
                adContainerView,
                context,
                adListener
            )
        } else {
            if (CodecxAd.getAdConfig()?.isGoogleAdsAllowed == true) {
                loadNativeAd(
                    isAdAllowed,
                    loadingAdView,
                    adContainerView,
                    nativeId ?: "ca-app-pub-3940256099942544/2247696110",
                    context,
                    object : AdCallBack() {
                        override fun onNativeAdLoad(nativeAd: NativeAd) {
                            super.onNativeAdLoad(nativeAd)
                            adListener?.onNativeAdLoad(nativeAd)
                            populateNativeAd(
                                context,
                                adContainerView,
                                nativeAd,
                                nativeAdView,
                                adListener
                            )
                        }

                        override fun onAdFailToLoad(error: Exception) {
                            super.onAdFailToLoad(error)
                            if (CodecxAd.getAdConfig()?.isYandexAllowed == true && CodecxAd.getAdConfig()?.shouldShowYandexOnGoogleAdFail == true) {
                                populateYandexNative(
                                    isAdAllowed,
                                    loadingAdView,
                                    nativeAdView,
                                    adContainerView,
                                    context,
                                    adListener
                                )
                            } else {
                                adListener?.onAdFailToLoad(error)
                            }
                        }

                        override fun onAdFailToShow(error: Exception) {
                            super.onAdFailToShow(error)
                            adListener?.onAdFailToShow(error)
                        }
                    }
                )
            }
        }


    }

    private fun populateYandexNative(
        isAdAllowed: Boolean,
        @LayoutRes loadingAdView: Int,
        @LayoutRes nativeAdView: Int,
        adContainerView: ViewGroup,
        context: Activity,
        adListener: AdCallBack? = null
    ) {
        YandexNativeAd.populateNativeAd(
            isAdAllowed,
            loadingAdView,
            nativeAdView,
            adContainerView,
            CodecxAd.getAdConfig()?.yandexAdIds?.nativeId
                ?: context.getString(R.string.yandexNativeTestId),
            context,
            object : YandexNativeAd.YandexNativeAdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    adListener?.onAdLoaded()
                }

                override fun onAdShown() {
                    super.onAdShown()
                    adListener?.onAdShown()

                }

                override fun onAdFailToLoad(error: Exception) {
                    super.onAdFailToLoad(error)
                    adListener?.onAdFailToLoad(error)

                }

                override fun onAdFailToShow(error: Exception) {
                    super.onAdFailToShow(error)
                    adListener?.onAdFailToShow(error)
                }

            })
    }

    fun populateNativeAd(
        isAdAllowed: Boolean,
        @LayoutRes nativeAdView: Int,
        adContainerView: ViewGroup,
        nativeId: String?,
        context: Activity,
        adCallBack: AdCallBack?
    ) {
        if (CodecxAd.getAdConfig()?.isYandexAllowed == true && CodecxAd.getAdConfig()?.isGoogleAdsAllowed == false) {
            populateYandexNative(
                isAdAllowed,
                R.layout.shimmer_native_ad_view,
                nativeAdView,
                adContainerView,
                context,
                adCallBack
            )
        } else {
            if (CodecxAd.getAdConfig()?.isGoogleAdsAllowed == true) {
                loadNativeAd(
                    isAdAllowed,
                    nativeId ?: "ca-app-pub-3940256099942544/2247696110",
                    context,
                    object : AdCallBack() {
                        override fun onNativeAdLoad(nativeAd: NativeAd) {
                            super.onNativeAdLoad(nativeAd)
                            adCallBack?.onNativeAdLoad(nativeAd)
                            populateNativeAd(
                                context,
                                adContainerView,
                                nativeAd,
                                nativeAdView,
                                adCallBack
                            )
                        }

                        override fun onAdFailToLoad(error: Exception) {
                            super.onAdFailToLoad(error)
                            if (CodecxAd.getAdConfig()?.isYandexAllowed == true && CodecxAd.getAdConfig()?.shouldShowYandexOnGoogleAdFail == true) {
                                populateYandexNative(
                                    isAdAllowed,
                                    R.layout.shimmer_native_ad_view,
                                    nativeAdView,
                                    adContainerView,
                                    context,
                                    adCallBack
                                )
                            } else {
                                adCallBack?.onAdFailToLoad(error)
                            }
                        }

                        override fun onAdFailToShow(error: Exception) {
                            super.onAdFailToShow(error)
                            adCallBack?.onAdFailToShow(error)
                            adContainerView.removeAllViews()
                        }
                    }
                )
            }

        }


    }


}









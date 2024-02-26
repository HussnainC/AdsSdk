package com.codex.googleadssdk.yandaxAds

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.codex.googleadssdk.R
import com.codex.googleadssdk.utils.isNetworkConnected
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener
import com.yandex.mobile.ads.nativeads.NativeAdLoader
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration
import com.yandex.mobile.ads.nativeads.NativeAdView
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder


object YandexNativeAd {

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        val nativeAdBinder = NativeAdViewBinder.Builder(adView)
        adView.apply {
            nativeAdBinder.setTitleView(findViewById(R.id.y_ad_title))
            nativeAdBinder.setDomainView(findViewById(R.id.y_domain))
            nativeAdBinder.setWarningView(findViewById(R.id.y_warning))
            nativeAdBinder.setSponsoredView(findViewById(R.id.y_sponsor))
            nativeAdBinder.setFeedbackView(findViewById(R.id.y_feedback))
            nativeAdBinder.setCallToActionView(findViewById(R.id.y_ad_call_to_action_new))
            nativeAdBinder.setMediaView(findViewById(R.id.y_ad_media))
            nativeAdBinder.setIconView(findViewById(R.id.y_ad_app_icon))
            nativeAdBinder.setPriceView(findViewById(R.id.y_price))
            nativeAdBinder.setFaviconView(findViewById(R.id.y_ad_fav_icon))
            nativeAdBinder.setReviewCountView(findViewById(R.id.y_reviewCount))
            nativeAdBinder.setBodyView(findViewById(R.id.y_ad_body))
        }
        nativeAd.bindNativeAd(nativeAdBinder.build())
    }

    private fun populateNativeAd(
        context: Activity,
        adContainerView: ViewGroup,
        nativeAd: NativeAd, @LayoutRes nativeAdView: Int, adListener: YandexNativeAdListener?
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
        adListener: YandexNativeAdListener? = null
    ) {
        loadNativeAd(
            isAdAllowed,
            loadingAdView,
            adContainerView,
            nativeId,
            context,
            object : YandexNativeAdListener() {
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
                    adListener?.onAdFailToLoad(error)
                }

                override fun onAdFailToShow(error: Exception) {
                    super.onAdFailToShow(error)
                    adListener?.onAdFailToShow(error)
                }
            }
        )

    }


    private fun loadNativeAd(
        isAdAllowed: Boolean,
        @LayoutRes loadingAdView: Int,
        adContainerView: ViewGroup,
        nativeId: String, context: Activity, adListener: YandexNativeAdListener? = null
    ) {
        if (isAdAllowed) {
            if (context.isNetworkConnected()) {
                if (adContainerView.childCount == 0) {
                    val loadingView =
                        LayoutInflater.from(context).inflate(loadingAdView, null, false)
                    adContainerView.removeAllViews()
                    adContainerView.addView(loadingView)
                }

                val adLoader = NativeAdLoader(context)
                adLoader.setNativeAdLoadListener(object : NativeAdLoadListener {
                    override fun onAdLoaded(p0: NativeAd) {
                        adListener?.onNativeAdLoad(p0)
                    }

                    override fun onAdFailedToLoad(p0: AdRequestError) {
                        adListener?.onAdFailToLoad(Exception(p0.description))
                    }

                })
                val nativeAdRequestConfiguration =
                    NativeAdRequestConfiguration.Builder(nativeId).build()
                adLoader.loadAd(nativeAdRequestConfiguration)
            } else {
                adContainerView.removeAllViews()
                adListener?.onAdFailToShow(Exception("Internet not connected"))
            }
        } else {
            adContainerView.removeAllViews()
            adListener?.onAdFailToShow(Exception("Ad not allowed"))
        }
    }

    abstract class YandexNativeAdListener {
        open fun onNativeAdLoad(nativeAd: NativeAd) {}
        open fun onAdLoaded() {}
        open fun onAdFailToLoad(error: Exception) {}
        open fun onAdShown() {}
        open fun onAdFailToShow(error: Exception) {}

    }
}
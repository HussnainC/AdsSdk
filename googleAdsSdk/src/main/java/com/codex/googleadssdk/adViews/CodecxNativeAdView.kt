package com.codex.googleadssdk.adViews

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.codex.googleadssdk.R
import com.codex.googleadssdk.interfaces.AdCallBack
import com.codex.googleadssdk.nativeAds.NativeAdsUtil

class CodecxNativeAdView @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defaultStyle: Int = 0
) :
    LinearLayout(context, attrSet, defaultStyle) {
    private var contentLayoutView: Int = R.layout.native_ad_view_medium

    init {
        val typedArray = context.obtainStyledAttributes(
            attrSet,
            R.styleable.CodecxNativeAdView,
            defaultStyle,
            android.R.style.Theme_Material_Light_NoActionBar
        )
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        orientation = LinearLayout.VERTICAL
        val loadingLayoutView = typedArray.getResourceId(
            R.styleable.CodecxNativeAdView_c_loadingView,
            R.layout.shimmer_native_ad_view_medium
        )
        contentLayoutView = typedArray.getResourceId(
            R.styleable.CodecxNativeAdView_c_contentAdView,
            R.layout.native_ad_view_medium
        )
        addShimmerLayout(loadingLayoutView)
    }

    fun populateNativeAd(adAllowed: Boolean, adId: String, activity: Activity) {
        NativeAdsUtil.populateNativeAd(
            adAllowed,
            contentLayoutView,
            this,
            adId,
            activity,
            null
        )
    }

    fun populateNativeAd(adId: String, activity: Activity) {
        NativeAdsUtil.populateNativeAd(true, contentLayoutView, this, adId, activity, null)

    }

    fun populateNativeAd(adId: String, activity: Activity, adCallBack: AdCallBack) {
        NativeAdsUtil.populateNativeAd(
            true,
            contentLayoutView,
            this,
            adId,
            activity,
            adCallBack
        )
    }


    private fun addShimmerLayout(loadingLayoutView: Int) {
        try {
            val loadingView = LayoutInflater.from(context).inflate(loadingLayoutView, null, false)
            this.removeAllViews()
            this.addView(loadingView)
        } catch (ex: Exception) {
            //
        }
    }
}
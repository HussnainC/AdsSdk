package com.codex.googleadssdk.adViews

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.codex.googleadssdk.R
import com.codex.googleadssdk.bannerAds.BannerAd
import com.codex.googleadssdk.collapsBannerAd.CollapseBannerAd
import com.codex.googleadssdk.interfaces.AdCallBack

class BannerAdView @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defaultStyle: Int = 0
) :
    LinearLayout(context, attrSet, defaultStyle) {
    init {
        val typedArray = context.obtainStyledAttributes(
            attrSet,
            R.styleable.BannerAdView,
            defaultStyle,
            android.R.style.Theme_Material_Light_NoActionBar
        )
        val loadingLayoutView = typedArray.getResourceId(
            R.styleable.BannerAdView_loadingView,
            R.layout.loading_banner_layout
        )
        addShimmerLayout(loadingLayoutView)
    }

    fun loadAd(adAllowed: Boolean, adId: String) {
        BannerAd.showBanner(adAllowed, this, adId, context, null)
    }

    fun loadAd(adId: String) {
        BannerAd.showBanner(true, this, adId, context, null)
    }

    fun loadAd(adId: String, adCallBack: AdCallBack) {
        BannerAd.showBanner(true, this, adId, context, adCallBack)
    }

    fun loadCollapseBannerAd(adId: String, adCallBack: AdCallBack, activity: Activity) {
        CollapseBannerAd.loadCollapseBanner(activity, true, this, adId, adCallBack)
    }

    fun loadCollapseBannerAd(adId: String, activity: Activity) {
        CollapseBannerAd.loadCollapseBanner(activity, true, this, adId,null)
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
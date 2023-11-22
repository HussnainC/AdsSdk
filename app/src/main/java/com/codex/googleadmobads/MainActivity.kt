package com.codex.googleadmobads

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codex.googleadssdk.adViews.BannerAdView
import com.codex.googleadssdk.ads.CodecxAd
import com.codex.googleadssdk.bannerAds.BannerAd
import com.codex.googleadssdk.dataclass.NativeAdDetail
import com.codex.googleadssdk.googleads.InterstitialAdHelper
import com.codex.googleadssdk.interfaces.AdCallBack
import com.codex.googleadssdk.nativeAds.NativeAdsUtil
import com.codex.googleadssdk.openAd.OpenAdConfig
import com.codex.googleadssdk.utils.showLog

class MainActivity : AppCompatActivity() {
    private lateinit var nativeAdsUtil: NativeAdsUtil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAds()
        OpenAdConfig.enableResumeAd()

        // loadNative()
        loadBanner()

        findViewById<Button>(R.id.btnInterstitial).setOnClickListener {
            CodecxAd.showGoogleInterstitial(showLoadingLayout = true,
                adId = getString(R.string.testInterstitialAdId),
                adAllowed = true,
                activity = this,
                startTimer = true, timerMilliSec = 5000L,
                adCallBack = object : AdCallBack() {
                    override fun onAdLoaded() {
                        "asdpan".showLog("Ad Loaded")
                    }

                    override fun onAdDismiss() {
                        super.onAdDismiss()
                        "asdpan".showLog("Ad Dismiss")
                    }

                    override fun onAdFailToLoad(error: Exception) {
                        super.onAdFailToLoad(error)
                        "asdpan".showLog("Fail to show ${error.message}")
                    }

                    override fun onAdShown() {
                        super.onAdShown()
                        "asdpan".showLog("Ad Visible")
                    }
                })
        }

    }

    private fun loadBanner() {
        val bannerContainer = findViewById<FrameLayout>(R.id.bannerAdLayout)
        CodecxAd.showBannerAd(
            getString(R.string.bannerTestId),
            true,
            com.codex.googleadssdk.R.layout.loading_banner_layout,
            bannerContainer,
            this
        )
    }

    private fun loadNative() {
        nativeAdsUtil.loadNativeAd(
            true,
            R.layout.placeholder,
            R.layout.native_ad,
            findViewById(R.id.nativeAdLayout),
            getString(R.string.nativeTestAd),
            this,
            object : NativeAdsUtil.NativeAdListener {
                override fun onAdLoad(
                    nativeAd: NativeAdDetail,
                    nativeAdView: View
                ) {

                }

                override fun onFailToLoad(message: String) {
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    private fun initAds() {
        nativeAdsUtil = NativeAdsUtil()

    }
}
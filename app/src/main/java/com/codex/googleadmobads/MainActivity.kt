package com.codex.googleadmobads

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.codex.googleadssdk.adViews.BannerAdView
import com.codex.googleadssdk.adViews.CodecxNativeAdView
import com.codex.googleadssdk.ads.CodecxAd
import com.codex.googleadssdk.ads.CodecxAdsConfig
import com.codex.googleadssdk.interfaces.AdCallBack
import com.codex.googleadssdk.nativeAds.NativeAdsUtil
import com.codex.googleadssdk.openAd.OpenAdConfig
import com.codex.googleadssdk.utils.showLog
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAds()
        OpenAdConfig.disableResumeAd()

        // loadNative()
        loadBanner()

//        findViewById<Button>(R.id.btnInterstitial).setOnClickListener {
//            CodecxAd.showGoogleInterstitial(showLoadingLayout = true,
//                adId = getString(R.string.testInterstitialAdId),
//                adAllowed = true,
//                activity = this,
//                startTimer = true, timerMilliSec = 5000L,
//                adCallBack = object : AdCallBack() {
//                    override fun onAdLoaded() {
//                        "asdpan".showLog("Ad Loaded")
//                    }
//
//                    override fun onAdDismiss() {
//                        super.onAdDismiss()
//                        "asdpan".showLog("Ad Dismiss")
//                    }
//
//                    override fun onAdFailToLoad(error: Exception) {
//                        super.onAdFailToLoad(error)
//                        "asdpan".showLog("Fail to show ${error.message}")
//                    }
//
//                    override fun onAdShown() {
//                        super.onAdShown()
//                        "asdpan".showLog("Ad Visible")
//                    }
//                })
//        }
        findViewById<Button>(R.id.btnInterstitial).setOnClickListener {
            CodecxAd.showGoogleInterstitial(
                getString(R.string.testInterstitialAdId),
                adAllowed = true,
                showLoadingLayout = true,
                activity = this,
                adCallBack = object : AdCallBack() {
                    override fun onNextMove() {
                        super.onNextMove()
                        startActivity(Intent(this@MainActivity, SecondActivity::class.java))
                    }

                    override fun onAdFailToLoad(error: Exception) {
                        super.onAdFailToLoad(error)
                        Snackbar.make(
                            window.decorView,
                            error.message.toString(),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    override fun onAdFailToShow(error: Exception) {
                        super.onAdFailToShow(error)
                        Snackbar.make(
                            window.decorView,
                            "Fail to show: ${error.message.toString()}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                })
        }

        loadNative()
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
        findViewById<CodecxNativeAdView>(R.id.nativeAdLayout).populateNativeAd(
            getString(R.string.nativeTestAd),
            this, object : AdCallBack() {
                override fun onAdFailToShow(error: Exception) {
                    super.onAdFailToShow(error)
                    Snackbar.make(
                        window.decorView,
                        "Native fail to show : ${error.message.toString()}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                override fun onAdFailToLoad(error: Exception) {
                    super.onAdFailToLoad(error)
                    Snackbar.make(
                        window.decorView,
                        "Native fail to load : ${error.message.toString()}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                override fun onAdShown() {
                    super.onAdShown()
                    Snackbar.make(
                        window.decorView,
                        "Ad Showed Successful",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Snackbar.make(
                        window.decorView,
                        "Ad Load Successful",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun initAds() {
        CodecxAd.initAds(
            CodecxAdsConfig.Builder().setIsDebugged(true).onNextInterstitial(true).build(), this
        )
    }
}
package com.codex.googleadmobads

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.codex.googleadssdk.adViews.CodecxNativeAdView
import com.codex.googleadssdk.ads.CodecxAd
import com.codex.googleadssdk.ads.CodecxAdsConfig
import com.codex.googleadssdk.interfaces.AdCallBack
import com.codex.googleadssdk.openAd.OpenAdConfig
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        OpenAdConfig.enableResumeAd()

        loadBanner()

        findViewById<Button>(R.id.btnInterstitial).setOnClickListener {
            CodecxAd.showInterstitial(
                "ca-app-pub-3940256099942544/1033173712",
                adAllowed = true,
                showLoadingLayout = true,
                activity = this,
                adCallBack = object : AdCallBack() {
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
            "ca-app-pub-3940256099942544/6300978111",
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


}
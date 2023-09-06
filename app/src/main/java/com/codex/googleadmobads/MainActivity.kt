package com.codex.googleadmobads

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codex.googleadssdk.bannerAds.BannerAd
import com.codex.googleadssdk.dataclass.NativeAdDetail
import com.codex.googleadssdk.interstitalAd.InterstitialAdClass
import com.codex.googleadssdk.nativeAds.NativeAdsUtil
import com.codex.googleadssdk.openAd.SplashOpenAppWithInterstitial

class MainActivity : AppCompatActivity() {
    private lateinit var nativeAdsUtil: NativeAdsUtil
    private lateinit var interstitialAdClass: InterstitialAdClass
    private lateinit var splashOpenAppWithInterstitial: SplashOpenAppWithInterstitial
    private lateinit var bannerAd: BannerAd
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAds()
//        if (savedInstanceState == null) {
//            splashOpenAppWithInterstitial.loadAndShowOpenAd(context = this, work = {
//                Toast.makeText(this, "Ad Dismiss", Toast.LENGTH_SHORT).show()
//            })
//        }
        loadNative()
        loadBanner()
        findViewById<Button>(R.id.btnInterstitial).setOnClickListener {
            interstitialAdClass.showInterstitialAdNew(
                isAdAllowed = true,
                activity = this,
                ids = listOf(getString(R.string.testInterstitialAdId))
            )
        }

    }

    private fun loadBanner() {
        bannerAd.showBanner(
            isAdAllowed = true,
            bannerLayout = findViewById(R.id.bannerAdLayout),
            unitId = getString(R.string.bannerTestId)
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
        interstitialAdClass = InterstitialAdClass(this)
        bannerAd = BannerAd(this)
        splashOpenAppWithInterstitial = SplashOpenAppWithInterstitial(
            mInterstitialAdClass = interstitialAdClass,
            openAdIds = listOf(getString(R.string.testOpenAdId)),
            interstitialAdIds = listOf(getString(R.string.testInterstitialAdId)),
            isInterstitialAdAllowed = true,
            isOpenAdAllowed = true
        )
    }
}
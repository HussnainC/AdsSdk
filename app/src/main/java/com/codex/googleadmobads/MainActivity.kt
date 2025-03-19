package com.codex.googleadmobads

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.codex.googleadssdk.CodecxAd
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
                adCallBack = object : AdCallBack {
                    override fun onAdLoaded() {

                    }

                    override fun onAdFailToLoad(error: Exception) {

                        Snackbar.make(
                            window.decorView,
                            error.message.toString(),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    override fun onAdShown() {
                    }

                    override fun onAdClick() {
                        TODO("Notx yet implemented")
                    }

                    override fun onAdDismiss() {
                    }

                    override fun onAdFailToShow(error: Exception) {

                        Snackbar.make(
                            window.decorView,
                            "Fail to show: ${error.message.toString()}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                })
        }

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


}
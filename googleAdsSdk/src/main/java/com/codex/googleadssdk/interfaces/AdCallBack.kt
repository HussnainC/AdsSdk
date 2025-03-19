package com.codex.googleadssdk.interfaces

import android.view.View
import com.google.android.gms.ads.nativead.NativeAd

interface AdCallBack {
    fun onAdLoaded()
    fun onAdFailToLoad(error: Exception)
    fun onAdShown()
    fun onAdClick()
    fun onAdDismiss()
    fun onAdFailToShow(error: Exception)
    fun onNativeAdLoad(nativeAd: NativeAd, adView: View) {}
    fun onNativeAdLoad(nativeAd: NativeAd) {}
}

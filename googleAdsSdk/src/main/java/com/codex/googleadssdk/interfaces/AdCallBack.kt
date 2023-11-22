package com.codex.googleadssdk.interfaces

abstract class AdCallBack {
    open fun onAdLoaded() {}
    open fun onAdFailToLoad(error: Exception) {}
    open fun onAdShown() {}
    open fun onAdClick() {}
    open fun onAdDismiss() {}
    open fun onAdFailToShow(error: Exception) {}
}
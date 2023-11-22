package com.codex.googleadssdk.openAd

object OpenAdConfig {
    private var isOpenAdAllowed: Boolean = false
    fun enableResumeAd() {
        isOpenAdAllowed = true
    }

    fun disableResumeAd() {
        isOpenAdAllowed = false
    }

    fun isAdEnable() = isOpenAdAllowed
}
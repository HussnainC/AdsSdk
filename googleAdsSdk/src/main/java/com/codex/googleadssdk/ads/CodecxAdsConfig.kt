package com.codex.googleadssdk.ads

import com.codex.googleadssdk.yandaxAds.YandexIds

class CodecxAdsConfig {
    var isDebugged = false
    var testDevices = listOf<String>()
    var yandexAdIds: YandexIds = YandexIds()
    var isYandexAllowed = false
    var isGoogleAdsAllowed: Boolean = false
    var shouldShowYandexOnGoogleAdFail = false
    var isDisableResumeAdOnClick = false

    class Builder {
        private var testDevices: List<String> = listOf()
        private var isDebugged: Boolean = false
        private var isYandexAllowed: Boolean = false
        private var yandexAdIds: YandexIds = YandexIds()
        private var isDisableResumeAdOnClick = false

        private var isGoogleAdsAllowed: Boolean = false
        private var shouldShowYandexOnGoogleAdFail: Boolean = false
        private var showNext: Boolean = false
        fun setIsDebugged(isDebugged: Boolean) = apply {
            this@Builder.isDebugged = isDebugged
        }
        fun setDisableResumeAdOnClick(isDisableResumeAdOnClick: Boolean) = apply {
            this@Builder.isDisableResumeAdOnClick = isDisableResumeAdOnClick
        }

        fun setIsYandexAllowed(isYandexAllowed: Boolean) = apply {
            this@Builder.isYandexAllowed = isYandexAllowed
        }

        fun setYandexAdIds(yandexIds: YandexIds) = apply {
            this@Builder.yandexAdIds = yandexIds
        }

        fun setIsGoogleAdsAllowed(isGoogleAdsAllowed: Boolean) = apply {
            this@Builder.isGoogleAdsAllowed = isGoogleAdsAllowed
        }

        fun setShowYandexOnGoogleAdFail(isShowYandexOnGoogleAdFail: Boolean) = apply {
            this@Builder.shouldShowYandexOnGoogleAdFail = isShowYandexOnGoogleAdFail
        }

        fun onNextInterstitial(value: Boolean) = apply {
            this@Builder.showNext = value
        }

        fun addTestDevices(testDevices: List<String>) =
            apply { this@Builder.testDevices = testDevices }

        fun build(): CodecxAdsConfig {
            val codecx = CodecxAdsConfig()
            codecx.apply {
                this.isGoogleAdsAllowed = this@Builder.isGoogleAdsAllowed
                this.isDebugged = this@Builder.isDebugged
                this.isYandexAllowed = this@Builder.isYandexAllowed
                this.shouldShowYandexOnGoogleAdFail = this@Builder.shouldShowYandexOnGoogleAdFail
                this.testDevices = this@Builder.testDevices
                this.yandexAdIds = this@Builder.yandexAdIds
            }
            return codecx
        }
    }
}
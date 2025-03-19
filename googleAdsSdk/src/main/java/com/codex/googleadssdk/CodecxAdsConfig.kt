package com.codex.googleadssdk

import com.codex.googleadssdk.yandaxAds.YandexIds

data class CodecxAdsConfig(
    val isDebugged: Boolean = false,
    val testDevices: List<String> = listOf(),
    val yandexAdIds: YandexIds = YandexIds(),
    val isYandexAllowed: Boolean = false,
    val isGoogleAdsAllowed: Boolean = false,
    val shouldShowYandexOnGoogleAdFail: Boolean = false,
    val isDisableResumeAdOnClick: Boolean = false,
    val requestTimeOut: Long = 10000L
) {
    companion object {
        fun builder() = Builder()
    }

    class Builder {
        private var isDebugged = false
        private var testDevices = listOf<String>()
        private var yandexAdIds = YandexIds()
        private var isYandexAllowed = false
        private var isGoogleAdsAllowed = false
        private var shouldShowYandexOnGoogleAdFail = false
        private var isDisableResumeAdOnClick = false
        private var requestTimeOut = 10000L

        fun setIsDebugged(value: Boolean) = apply { isDebugged = value }
        fun setRequestTimeOut(value: Long) = apply { requestTimeOut = value }
        fun setDisableResumeAdOnClick(value: Boolean) = apply { isDisableResumeAdOnClick = value }
        fun setIsYandexAllowed(value: Boolean) = apply { isYandexAllowed = value }
        fun setYandexAdIds(value: YandexIds) = apply { yandexAdIds = value }
        fun setIsGoogleAdsAllowed(value: Boolean) = apply { isGoogleAdsAllowed = value }
        fun setShowYandexOnGoogleAdFail(value: Boolean) = apply { shouldShowYandexOnGoogleAdFail = value }
        fun addTestDevices(value: List<String>) = apply { testDevices = value }

        fun build() = CodecxAdsConfig(
            isDebugged, testDevices, yandexAdIds,
            isYandexAllowed, isGoogleAdsAllowed,
            shouldShowYandexOnGoogleAdFail, isDisableResumeAdOnClick, requestTimeOut
        )
    }
}
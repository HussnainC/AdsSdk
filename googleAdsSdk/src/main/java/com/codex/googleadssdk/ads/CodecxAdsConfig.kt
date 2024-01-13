package com.codex.googleadssdk.ads

class CodecxAdsConfig constructor(
    val isDebugged: Boolean,
    val testDevices: List<String> = listOf(),
    val showNext: Boolean
) {
    class Builder() {
        private var testDevices: List<String> = listOf()
        private var isDebugged: Boolean = false
        private var showNext: Boolean = false
        fun setIsDebugged(isDebugged: Boolean) = apply {
            this@Builder.isDebugged = isDebugged
        }

        fun onNextInterstitial(value: Boolean) = apply {
            this@Builder.showNext = value
        }

        fun addTestDevices(testDevices: List<String>) =
            apply { this@Builder.testDevices = testDevices }

        fun build(): CodecxAdsConfig {
            return CodecxAdsConfig(isDebugged, testDevices, showNext)
        }
    }
}
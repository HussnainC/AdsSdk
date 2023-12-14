package com.codex.googleadssdk.ads

class CodecxAdsConfig constructor(
    val isDebugged: Boolean,
    val testDevices: List<String> = listOf()
) {
    class Builder() {
        private var testDevices: List<String> = listOf()
        private var isDebugged: Boolean = false
        fun setIsDebugged(isDebugged: Boolean) = apply {
            this@Builder.isDebugged = isDebugged
        }

        fun addTestDevices(testDevices: List<String>) =
            apply { this@Builder.testDevices = testDevices }

        fun build(): CodecxAdsConfig {
            return CodecxAdsConfig(isDebugged, testDevices)
        }
    }
}
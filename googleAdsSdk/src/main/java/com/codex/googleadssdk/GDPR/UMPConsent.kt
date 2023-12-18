package com.codex.googleadssdk.GDPR

import android.app.Activity
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

object UMPConsent {

    fun requestForUMP(
        activity: Activity,
        enableTest: Boolean = false,
        deviceHashId: String = "B3EEABB8EE11C2BE770B684D95219ECB", listener: UMPRequestListener
    ) {
        val debugSettings = ConsentDebugSettings.Builder(activity)
            .addTestDeviceHashedId(deviceHashId)
            .build()

        val params =
            if (enableTest) {
                ConsentRequestParameters.Builder().setConsentDebugSettings(debugSettings)
                    .setTagForUnderAgeOfConsent(false)
                    .build()
            } else {
                ConsentRequestParameters.Builder()
                    .setTagForUnderAgeOfConsent(false)
                    .build()
            }

        val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                listener.onRequest(consentInformation.canRequestAds())
            },
            {
                listener.onRequest(false)
            })
    }

    interface UMPRequestListener {
        fun onRequest(isAccepted: Boolean)
    }
}
# Project Settings
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	} 
# Implementation Gradle Groovy
    dependencies {
		implementation 'com.github.HussnainC:AdsSdk:v1.0.8'
	  }

# Implementation Gradle Kotlin
    dependencies {
		implementation("com.github.HussnainC:AdsSdk:v1.0.8")
	  }

# InterstitalAd Implementation
    \\Create instance of  interstitialAdClass
    val mInterstital= InterstitialAdClass(context)
    mInterstital.showInterstitialAdNew(
                isAdAllowed = true,
                activity = this,
                ids = listOf(getString(R.string.testInterstitialAdId)),
                isTimerEnable = true,
                timerMilliSec = 10000L, //10 sec delay
                isLoadingDialogShow = true,
                loadingAdViewLayoutId = InterstitialAdLayout.DefaultLayout.adLayoutId,
                work={
                //Do your work when interstitial dismiss
                }
            )

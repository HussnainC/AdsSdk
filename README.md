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
    \\Create instance of interstitialAdClass
    val mInterstital= InterstitialAdClass(context)
    mInterstital.showInterstitialAdNew(
                isAdAllowed = true,
                activity = this,
                ids = listOf("ca-app-pub-3940256099942544/1033173712")),
                isTimerEnable = true,
                timerMilliSec = 10000L, //10 sec delay
                isLoadingDialogShow = true,
                loadingAdViewLayoutId = InterstitialAdLayout.DefaultLayout.adLayoutId,
                work={
                //Do your work when interstitial dismiss
                }
            )

# BannerAd Implementation
    \\Create instance of BannerAdClass
    val bannerAd= BannerAd(context)
    bannerAd.showBanner(
            isAdAllowed = true,
            bannerLayout = findViewById(R.id.bannerAdLayout),\\FrameLayout to for BannerAd View
            unitId = "ca-app-pub-3940256099942544/6300978111"
        )

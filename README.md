# Project Settings
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	} 
# Implementation Gradle Groovy
    dependencies {
		implementation 'com.github.HussnainC:AdsSdk:v1.1.1'
	  }

# Implementation Gradle Kotlin
    dependencies {
		implementation("com.github.HussnainC:AdsSdk:v1.1.0")
	  }

# Android Manifest
	<meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
	    /*Test app_Id="ca-app-pub-3940256099942544~3347511713" */
            android:value="@string/app_id" />

# InterstitalAd Implementation
    \\Create instance of interstitialAdClass
    val mInterstital= InterstitialAdClass(context)
    mInterstital.showInterstitialAdNew(
                isAdAllowed = true,
                activity = this,
                ids = listOf("ca-app-pub-3940256099942544/1033173712"),
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
            bannerLayout = findViewById(R.id.bannerAdLayout),\\FrameLayout for BannerAd View
            unitId = "ca-app-pub-3940256099942544/6300978111"
        )

 # OpenAd Implementation
    \\Initialize and load in Application Class 
    OpenApp(
            globalClass = applicationContext,
            adIds = listOf("ca-app-pub-3940256099942544/3419835294"),
            isAdAllowed = true,
	    restrictedScreens = listOf(),\\Pass list of restricted screen classes name like (SplashScreen::class.java.name)
            loadingAdViewLayoutId: Int = OpenAdLayout.DefaultLayout.layoutId
        )

  # NativeAd Implementation
    \\Create instance of NativeAdUtil
    val nativeAd= NativeAdUtil()
    nativeAd.loadNativeAd(
            isAdAllowed = true,
            adFrame = findViewById(R.id.nativeAdLayout),\\FrameLayout for NativeAd View
            adType = EnumAdType.Medium, /*(EnumAdType.Medium,EnumAdType.Small,EnumAdType.Large)*/
            nativeId = "ca-app-pub-3940256099942544/2247696110", activity
        )

# Project Settings
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	} 
# Implementation Gradle Groovy
    dependencies {
		implementation 'com.github.HussnainC:AdsSdk:v1.2.6'
	  }

# Implementation Gradle Kotlin
    dependencies {
		implementation("com.github.HussnainC:AdsSdk:v1.2.6")
	  }

# Android Manifest
	<meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
	    /*Test app_Id="ca-app-pub-3940256099942544~3347511713" */
            android:value="@string/app_id" />

# Show UMP Message form
	       
	 UMPConsent.requestForUMP(
           activity = this,
            debuge = true, //for test mode
            deviceHashId = "4A8FC7E40BF382E55A6D04A6965BB32F", //for test mode
            object : UMPConsent.UMPRequestListener {
                override fun onRequest(isAccepted: Boolean) {
                    if (isAccepted) {
                        initAds()
                    }
                }

            })

# Both Open and Interstitial Ad show if any ad load

	CodecxAd.showOpenOrInterstitialAd(openAdId ,
                interAdId,
                openAdAllowed ,
                interAdAllowed , context,
                object : AdCallBack() {
                    override fun onAdDismiss() {
                        super.onAdDismiss()
                       
                    }

                    override fun onAdFailToLoad(error: Exception) {
                        super.onAdFailToLoad(error)
                     
                    }

                    override fun onAdFailToShow(error: Exception) {
                        super.onAdFailToShow(error)
                       
                    }
                })

# InterstitalAd Implementation
    CodecxAd.showGoogleInterstitial(
            adId ="adId",
            adAllowed =true,
            startTimer = true,
            showLoadingLayout = true,
            timerMilliSec = 10000L,
            activity = this,
            object : AdCallBack() {

            }
        )

	 CodecxAd.showGoogleInterstitial(
        adId: String,
        adAllowed: Boolean,
        startTimer: Boolean,
        timerMilliSec: Long,
        showLoadingLayout: Boolean,
        @LayoutRes
        loadingLayout: Int,
        activity: Activity,
        adCallBack: AdCallBack
    )

    CodecxAd.showGoogleInterstitial(
        adId: String,
        adAllowed: Boolean,
        showLoadingLayout: Boolean,
        activity: Activity,
        adCallBack: AdCallBack
    ) 

    CodecxAd.showGoogleInterstitial(
        adId: String,
        adAllowed: Boolean,
        activity: Activity,
        adCallBack: AdCallBack
    )

    
# BannerAd Implementation
    
 	  CodecxAd.showBannerAd(
            getString(R.string.bannerTestId),
            true,
            R.layout.loadingView,
            bannerContainer,
            context
        )
	//Banner View
	<com.codex.googleadssdk.adViews.BannerAdView
	    android:layout_width="match_parent"
            android:id="@+id/bannerAdView"
 	    app:loadingView="@layout/loading_banner_layout"
   	    android:layout_height="wrap_content"/>

	 findViewById<BannerAdView>(R.id.bannerAdView).loadAd(adId: String)
  	findViewById<BannerAdView>(R.id.bannerAdView).loadAd(adAllowed: Boolean, adId: String)
  	 findViewById<BannerAdView>(R.id.bannerAdView).loadAd(adId: String, adCallBack: AdCallBack)
   	findViewById<BannerAdView>(R.id.bannerAdView).loadCollapseBannerAd(adId: String, adCallBack: AdCallBack, activity: Activity)
   	findViewById<BannerAdView>(R.id.bannerAdView).loadCollapseBannerAd(adId: String, activity: Activity)

	 CodecxAd.showBannerAd(
        adId: String,
        adAllowed: Boolean,
        @LayoutRes loadingLayout: Int,
        adContainer: ViewGroup,
        activity: Activity,
        adCallBack: AdCallBack
    )

    CodecxAd.showCollapseBannerAd(
        adId: String,
        adAllowed: Boolean,
        @LayoutRes loadingLayout: Int,
        adContainer: ViewGroup,
        activity: Activity,
        adCallBack: AdCallBack
    )

     CodecxAd.showCollapseBannerAd(
        adId: String,
        adAllowed: Boolean,
        @LayoutRes loadingLayout: Int,
        adContainer: ViewGroup,
        activity: Activity
    )

    
 # OpenAd Implementation
    \\Initialize and load in Application Class 
  	 OpenApp(this, getString(R.string.testOpenAdId), true)
    \\Help to enable Open Ad
	        OpenAdConfig.enableResumeAd()
     \\ Help to disable Open Ad
             OpenAdConfig.disableResumeAd()

  # NativeAd Implementation
    \\Create instance of NativeAdUtil
      <com.codex.googleadssdk.adViews.CodecxNativeAdView
        android:id="@+id/nativeAdLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:c_contentAdView="@layout/native_ad_view_medium" \\Place your any nativeAdview content layout
        app:c_loadingView="@layout/shimmer_native_ad_view_medium" \\Place your any nativeAdview loading layout
        />

	findViewById<CodecxNativeAdView>(R.id.nativeAdLayout).populateNativeAd(
            getString(R.string.nativeTestAd),
            this, object : AdCallBack() {
                override fun onAdFailToShow(error: Exception) {
                    super.onAdFailToShow(error)
                }

                override fun onAdFailToLoad(error: Exception) {
                    super.onAdFailToLoad(error)
                
                }

                override fun onAdShown() {
                    super.onAdShown()
                
                }
                override fun onAdLoaded() {
                    super.onAdLoaded()
        
                }
            }
        )

	\\By code implementation
 	\\Load NativeAd
 	NativeAdUtils. loadNativeAd(
        isAdAllowed: Boolean,
        @LayoutRes loadingAdView: Int,
        adContainerView: ViewGroup,
        nativeId: String, context: Activity, adListener: AdCallBack? = null
    	)
     \\Populate Native Ad
  	 NativeAdUtils. populateNativeAd(
        context: Activity,
        adContainerView: ViewGroup,
        nativeAd: NativeAd, @LayoutRes nativeAdView: Int, adListener: AdCallBack?
  	  )
     \\Load and populate Native Ad
	NativeAdUtils.populateNativeAd(
        isAdAllowed: Boolean,
        @LayoutRes loadingAdView: Int,
        @LayoutRes nativeAdView: Int,
        adContainerView: ViewGroup,
        nativeId: String,
        context: Activity,
        adListener: AdCallBack? = null
    )

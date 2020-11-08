package com.treecode.GloShop

import android.app.Application
import android.preference.PreferenceManager
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.treecode.GloShop.ui.main.BaseActivity
import java.util.*

@Suppress("DEPRECATION")

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

        var change = ""
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val language = sharedPreferences.getString("language", "English")
        if (language == "ar") {
            change="ar"
        } else if (language=="English" ) {
            change = "en"
        }else {
            change =""
        }

        BaseActivity.dLocale = Locale(change) //set any locale you want here
    }
}


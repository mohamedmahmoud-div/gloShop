package com.treecode.GloShop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.treecode.GloShop.ui.main.home.ui.main.HomeActivity
import com.treecode.GloShop.ui.main.registration.BaseRegisterActivity
import com.treecode.GloShop.util.SessionManager

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sessionManager = SessionManager(this)
        val token = sessionManager.fetchAuthToken()
        if (token != null && token != "") {
            startActivity(Intent(this, HomeActivity::class.java))
        }else {
            startActivity(Intent(this, BaseRegisterActivity::class.java))

        }
        finish()

    }
}
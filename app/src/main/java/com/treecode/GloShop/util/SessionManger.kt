package com.treecode.GloShop.util

import android.content.Context
import android.content.SharedPreferences
import com.treecode.GloShop.util.Constants.Companion.prefrencKey

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(prefrencKey, Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }
fun logout(){
    val editor = prefs.edit()
    editor.putString(USER_TOKEN, null)
    editor.apply()
}
    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
}
package com.treecode.GloShop.util

import android.content.Context
import android.content.SharedPreferences
import com.treecode.GloShop.data.model.login.AccountUser
import com.treecode.GloShop.data.model.profile.UserAddress
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserAccountManger (context: Context){
    private var prefs: SharedPreferences = context.getSharedPreferences(Constants.prefrencKey, Context.MODE_PRIVATE)
    fun saveUser(accountUser: AccountUser){
        val gson = Gson()
        val prefsEditor: SharedPreferences.Editor = prefs.edit()

        val type = object : TypeToken<UserAddress>(){}.type
       // val json = prefs.getString(Constants.USER__ACCOUNT, "")

      /*  var user: AccountUser = gson.fromJson(json, type)
        if (addressBook != null){
            addressBook.add(accountUser)
        }else {
            addressBook = HashSet()
            addressBook.add(accountUser)
        }
*/
        val jsonToSave = gson.toJson(accountUser)
        prefsEditor.putString(Constants.USER__ACCOUNT,jsonToSave)
        prefsEditor.apply()
    }

    fun getAccountUSer(): AccountUser?{
        val gson = Gson()
        val prefsEditor: SharedPreferences.Editor = prefs.edit()

        val type = object : TypeToken<AccountUser>(){}.type
        val json = prefs.getString(Constants.USER__ACCOUNT, "")
        var userAccount: AccountUser = gson.fromJson(json, type)
        return  userAccount
    }
}
package com.treecode.GloShop.util

import android.content.Context
import android.content.SharedPreferences
import com.treecode.GloShop.data.model.profile.AddressBook
import com.treecode.GloShop.data.model.profile.CreateLocationRequest
import com.treecode.GloShop.data.model.profile.UserAddress
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddressManger(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(Constants.prefrencKey, Context.MODE_PRIVATE)
    companion object {

    }
    init {

    }
    fun saveAddress(address: CreateLocationRequest){
        val gson = Gson()
        val prefsEditor: SharedPreferences.Editor = prefs.edit()

        val type = object : TypeToken<Set<UserAddress>>(){}.type
        val json = prefs.getString(Constants.USER_ADDRESS, "")

        var addressBook: HashSet<CreateLocationRequest>? = gson.fromJson(json, type)
        if (addressBook != null){
            addressBook.add(address)
        }else {
            addressBook = HashSet()
            addressBook.add(address)
        }

        val jsonToSave = gson.toJson(addressBook)
        prefsEditor.putString(Constants.sharedKey_Add_Cart,jsonToSave)
        prefsEditor.apply()
    }
    fun updateAddress(address: CreateLocationRequest){
       val gson = Gson()
        val prefsEditor: SharedPreferences.Editor = prefs.edit()

        val type = object : TypeToken<Set<CreateLocationRequest>>(){}.type
        val json = prefs.getString(Constants.USER_ADDRESS, "")
        var addressBook: HashSet<CreateLocationRequest>? = gson.fromJson(json, type)
        if (addressBook != null) {
            if (addressBook.contains(address)){

                addressBook.remove(address)
                addressBook.add(address)
            }
        }else{
            //saveAddress(address)
        }

    }
    fun setDefaultAddressBook(address: AddressBook){
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        val gson = Gson()
        val jsonToSave = gson.toJson(address)
        prefsEditor.putString(Constants.DEFAULt_ADDRESS,jsonToSave)
        prefsEditor.apply()

    }
    fun removeDefaultAddress(){
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        val gson = Gson()
        val jsonToSave = gson.toJson(null)
        prefsEditor.putString(Constants.DEFAULt_ADDRESS,jsonToSave)
        prefsEditor.apply()

    }
    fun getDefaultAddress(): AddressBook?{
        val gson = Gson()
        val type = object : TypeToken<AddressBook?>(){}.type
        val json = prefs.getString(Constants.DEFAULt_ADDRESS, "")
        val defaulAddress: AddressBook? = gson.fromJson(json, AddressBook::class.java)
        return defaulAddress
    }
    fun getAddressBook(): HashSet<CreateLocationRequest>? {
        val gson = Gson()
        val prefsEditor: SharedPreferences.Editor = prefs.edit()

        val type = object : TypeToken<Set<CreateLocationRequest>>(){}.type
        val json = prefs.getString(Constants.USER_ADDRESS, "")
        var addressBook: HashSet<CreateLocationRequest>? = gson.fromJson(json, type)
        return  addressBook
    }
}
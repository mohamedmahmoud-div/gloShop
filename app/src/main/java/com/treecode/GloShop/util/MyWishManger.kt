package com.treecode.GloShop.util

import android.content.Context
import android.content.SharedPreferences
import com.treecode.GloShop.data.model.home.CartProduct
import com.treecode.GloShop.data.model.home.Product
import com.treecode.GloShop.data.model.search.Specification
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MyWishManger (context: Context){
    private var prefs: SharedPreferences = context.getSharedPreferences(Constants.prefrencKey, Context.MODE_PRIVATE)

    fun saveWish(product: Product){
        val gson = Gson()
        val prefsEditor: SharedPreferences.Editor = prefs.edit()

        val type = object : TypeToken<Set<Product>>(){}.type
        val json = prefs.getString(Constants.sharedKey_Add_Wish, "")

        var wishList: HashSet<Product>? = gson.fromJson(json, type)
        if (wishList != null){
            wishList.add(product)
        }else {
            wishList = HashSet()
            wishList.add(product)
        }

        val jsonToSave = gson.toJson(wishList)
        prefsEditor.putString(Constants.sharedKey_Add_Wish,jsonToSave)
        prefsEditor.apply()
    }
    fun getWishList(): HashSet<CartProduct>? {
        val gson = Gson()
        val prefsEditor: SharedPreferences.Editor = prefs.edit()

        val type = object : TypeToken<Set<CartProduct>>(){}.type
        val json = prefs.getString(Constants.sharedKey_Add_Wish, "")
        var wishList: HashSet<CartProduct>? = gson.fromJson(json, type)
        return  wishList
    }
    fun adapterToProductInCart(product:Product):CartProduct {
        val id = product.id
        val name = product.name
        val spec = product.specification
        var productSpecs = ArrayList<Specification>()
        spec.let { it?.let { it1 -> productSpecs.add(it1) } }
        val price = product.price
        val discount  = product.discount
        val img = product.main_img
        val stars = product.stars
        val categoryName = product.categoryName
        val availability = product.availability
        val product = CartProduct(id=id,name= name,price = price,totalPirce = price,categoryName = categoryName.name,quantity = 1,selectedIDS = ArrayList(),main_img = img,totalAvailbilty = availability,afterPrice = null)
        return  product
    }
    fun updateCarts(products:HashSet<CartProduct>){
        val gson = Gson()
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        val jsonToSave = gson.toJson(products)
        prefsEditor.putString(Constants.sharedKey_Add_Wish, jsonToSave)
        prefsEditor.apply()
    }
}
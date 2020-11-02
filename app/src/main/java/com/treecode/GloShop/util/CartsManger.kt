package com.treecode.GloShop.util

import android.content.Context
import android.content.SharedPreferences
import com.treecode.GloShop.data.model.home.CartProduct
import com.treecode.GloShop.data.model.home.Product
import com.treecode.GloShop.data.model.search.ProductDetailsData
import com.treecode.GloShop.data.model.search.Specification
import com.treecode.GloShop.ui.main.search.SelectedSpec
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartsManger(context:Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(Constants.prefrencKey, Context.MODE_PRIVATE)
    fun saveCart(productDetails: ProductDetailsData){
        val gson = Gson()
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
val product = productAdapter(productDetails)

        val type = object : TypeToken<Set<CartProduct>>(){}.type
        val json = prefs.getString(Constants.sharedKey_Add_Cart, "")

        var carts: HashSet<CartProduct>? = gson.fromJson(json, type)
        if (carts != null){
            carts.add(product)
        }else {
            carts = HashSet()
            carts.add(product)
        }

        val jsonToSave = gson.toJson(carts)
        prefsEditor.putString(Constants.sharedKey_Add_Cart,jsonToSave)
        prefsEditor.apply()
    }
fun saveCart(product:Product){
val productInCart = adapterToProductInCart(product)
    saveCart(productInCart)
}

  fun saveCart(product:CartProduct){
      val gson = Gson()
product.quantity = 1
      val type = object : TypeToken<Set<CartProduct>>(){}.type
      val json = prefs.getString(Constants.sharedKey_Add_Cart, "")
      val prefsEditor: SharedPreferences.Editor = prefs.edit()

      var carts: HashSet<CartProduct>? = gson.fromJson(json, type)
      if (carts ==null) {
          carts = HashSet()
          carts.add(product)
          val jsonToSave = gson.toJson(carts)
          prefsEditor.putString(Constants.sharedKey_Add_Cart,jsonToSave)
          prefsEditor.apply()
        return
      }

      var productWithSameID = carts?.filter { product.id == it.id }
      val productsHasSpecs =      productWithSameID.filter {!it.selectedIDS.isNullOrEmpty() }

      if (productWithSameID.isNullOrEmpty()){
          carts?.add(product)
          val jsonToSave = gson.toJson(carts)
          prefsEditor.putString(Constants.sharedKey_Add_Cart,jsonToSave)
          prefsEditor.apply()
      }else {
          if(!product.selectedIDS.isNullOrEmpty()) {
              if (productsHasSpecs.isNullOrEmpty()){
                  carts?.add(product)
                  applySave(carts)
              }else { // both have specs
                  var fullFilterResult = ArrayList<SelectedSpec>()
                  val specvaluesId = product.selectedIDS!!.map { it.specChangeID }
                  val result =   productWithSameID.map{
                        val filterResult =      it.selectedIDS?.filter {selectedSpec ->
                         specvaluesId.contains(selectedSpec.specChangeID)
                            } as ArrayList<SelectedSpec>
                      if (!filterResult.isNullOrEmpty())
                     fullFilterResult.addAll(filterResult)
                     }
             if (fullFilterResult.isNullOrEmpty()){
                carts?.add(product)
                 applySave(carts)
            }
              }
          }else if (product.selectedIDS.isNullOrEmpty()) {
              if (!productsHasSpecs.isNullOrEmpty()){
                  carts?.add(product)
                  applySave(carts)
              }
          }
      }
  }
private fun applySave(carts:HashSet<CartProduct>){
    val gson = Gson()
    val jsonToSave = gson.toJson(carts)
    val prefsEditor: SharedPreferences.Editor = prefs.edit()
    prefsEditor.putString(Constants.sharedKey_Add_Cart,jsonToSave)
    prefsEditor.apply()
}
fun getAllProducts():HashSet<CartProduct>?{
    val gson = Gson()
    val type = object : TypeToken<Set<CartProduct>>(){}.type
    val json = prefs.getString(Constants.sharedKey_Add_Cart, "")
    var carts: HashSet<CartProduct>? = gson.fromJson(json, type)
    return carts

}

     fun adapterToProductInCart(product:Product):CartProduct {
        val id = product.id
        val name = product.name
        val spec = product.specification
        var productSpecs = ArrayList<Specification>()
        spec.let { it?.let { it1 -> productSpecs.add(it1) } }
        val price = product.price
         val afterPrice = product.offer?.afterPrice

        val discount  = product.discount
        val img = product.main_img
        val stars = product.stars
        val categoryName = product.categoryName
         val availability = product.availability

         var quantity = product.pieceCount
         if (quantity == 0)
             quantity++

        val product = CartProduct(id=id,name= name,price = price,totalPirce = price,categoryName = categoryName.name,quantity = quantity,selectedIDS = ArrayList(),main_img = img,totalAvailbilty = availability,afterPrice = afterPrice)
    return  product
    }
     fun productAdapter(productDetails: ProductDetailsData) :CartProduct{
        val id = productDetails.id
        val name = productDetails.name
        val spec = productDetails.specification
        var productSpecs = ArrayList<Specification>()
spec.let { it?.let { it1 -> productSpecs.add(it1) } }
        val price = productDetails.price
         val afterPrice = productDetails.priceAfterDicount
        val discount  = productDetails.discount
        val img = productDetails.img
        val stars = productDetails.stars
        val categoryName = productDetails.category
         val availability = productDetails.availability
        val product = CartProduct(id=id,name= name,price = price,totalPirce = price,categoryName = categoryName.name,quantity = 1,selectedIDS = ArrayList(),main_img = img,totalAvailbilty = availability,afterPrice = afterPrice)
        return  product
    }
     fun getProduct(productID:Int):Product?{
        val gson = Gson()
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        val type = object : TypeToken<Set<Product>>(){}.type
        val json = prefs.getString(Constants.sharedKey_Add_Cart, "")
         var carts: HashSet<Product>? = gson.fromJson(json, type)
         if (!carts.isNullOrEmpty()){
             val products  = carts?.filter {
                 it.id== productID
             }
             if (products.isNullOrEmpty() )
                 return  null
             else{
                 val product = products.first()
                 return product
             }


         }
          return null
    }
    fun updateCarts(products:HashSet<CartProduct>){
        val gson = Gson()
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        val jsonToSave = gson.toJson(products)
        prefsEditor.putString(Constants.sharedKey_Add_Cart, jsonToSave)
        prefsEditor.apply()
    }
    fun emptyCart(){
        val emptyList = HashSet<CartProduct>()
        val gson = Gson()
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        val jsonToSave = gson.toJson(emptyList)
        prefsEditor.putString(Constants.sharedKey_Add_Cart, jsonToSave)
        prefsEditor.apply()
    }
    fun<T> isEqual(first: List<T>, second: List<T>): Boolean {

        if (first.size != second.size) {
            return false
        }

        return first.zip(second).all { (x, y) -> x == y }
    }
}
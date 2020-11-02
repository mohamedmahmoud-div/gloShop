package com.treecode.GloShop.data.model.checkout

import com.treecode.GloShop.data.model.home.CartProduct
import com.google.gson.annotations.SerializedName

class AvailableProductsQuantityRequest(
    @SerializedName("items")
    val items:ArrayList<AvailableProductQuantity>
)
class AvailableProductQuantity(
    @SerializedName("product")
    val productID:Int,
    @SerializedName("spec_value_id")
    val latestSpecID:Int?
)
class AvailableProductsResponse(
    @SerializedName("status")
    var status: Int? = 0,

    @SerializedName("status_code")
    var statusCode:Int? = 0,

    @SerializedName("message")
    var message:String? = "",

    @SerializedName("errors")
    var error:ArrayList<String> ?= null,
    @SerializedName("data")
    val  data: ProductAvailabilityData
)
class ProductAvailabilityData(
    @SerializedName("items")
    val products:ArrayList<ProductAvailability>
)
class ProductAvailability(
    @SerializedName("product")
    val productID: Int,
    @SerializedName("availability")
    val availability:Int
)
class AvailableProductsAdapter{
    fun convertCartProduct(cartProduct: CartProduct):AvailableProductQuantity{
        var latestSpecID:Int? = null
        val productID = cartProduct.id
        if (!cartProduct.selectedIDS.isNullOrEmpty())
         latestSpecID = cartProduct.selectedIDS?.last()?.id
        return  AvailableProductQuantity(productID = productID,latestSpecID = latestSpecID)
    }
}
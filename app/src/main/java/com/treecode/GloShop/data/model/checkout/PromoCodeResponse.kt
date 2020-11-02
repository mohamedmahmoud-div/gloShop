package com.treecode.GloShop.data.model.checkout

import com.google.gson.annotations.SerializedName

class PromoCodeResponse(
    @SerializedName("status")
    var status: Int? = 0,

    @SerializedName("status_code")
    var statusCode:Int? = 0,

    @SerializedName("message")
    var message:String? = "",

    @SerializedName("errors")
    var error:ArrayList<String> ?= null,
    @SerializedName("data")
    val data:PromoCodeData?

)
class PromoCodeData(
@SerializedName("total_order") val totalOrder:Double?,
@SerializedName("shipping_fees") val shippingFees:Double?,
@SerializedName("total_cost_before")val totalBeforeDiscount:Double?,
@SerializedName("price_discount")val discount:Double?,
@SerializedName("total_cost")val totalCost:Double?
)
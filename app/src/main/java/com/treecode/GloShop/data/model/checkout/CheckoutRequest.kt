package com.treecode.GloShop.data.model.checkout

import com.treecode.GloShop.data.model.home.CartProduct
import com.google.gson.annotations.SerializedName

class CheckoutRequest (
    @SerializedName("address_book_id") val addressBookID:Int,
    @SerializedName("items") val productsInCart:HashSet<CartProduct>,
    @SerializedName("cash") var cashOnDelievry:Boolean

)
class CheckoutResponse (
    @SerializedName("status")
    var status: Int? = 0,

    @SerializedName("status_code")
var statusCode:Int? = 0,

@SerializedName("message")
var message:String? = "",

    @SerializedName("errors")
    var error:ArrayList<String> ?= null,

    @SerializedName("data")
    var checkoutData :CheckoutData
)

class CheckoutData(
    @SerializedName("total_order")
    var orderCost: Double? = 0.0,

    @SerializedName("shipping_fees")
    var shippingCost:Double? = 0.0,

    @SerializedName("total_cost")
    var totalCost:Double? = 0.0,

    @SerializedName("arrival_date")
    var arrivalDate:String? = null,
    @SerializedName("address_book_id")
    var addressBookID: Int
    )
/*

{
    "address_book_id": 1,
    "items": [
    {
        "product": 2,
        "specs": "color: red, size: 2X",
        "quantity": 2
    },
    {
        "product": 3,
        "specs": "color: red, size: 2X",
        "quantity": 2
    }
    ],
    "cash": true
}*/

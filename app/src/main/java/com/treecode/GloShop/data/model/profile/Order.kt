package com.treecode.GloShop.data.model.profile

import com.google.gson.annotations.SerializedName

class Order (
    @SerializedName("id") val id : Int,
    @SerializedName("order_number") val orderNumber : String,
    @SerializedName("delivered_date") val deliveredDate : String,
    @SerializedName("arrival_date") val arrivalDate : String,
    
    @SerializedName("address") val shippingAddress : String,
    @SerializedName("payment_method") val paymentMethod : PaymentMethod,
    @SerializedName("recipient") val recipient : String?,
    @SerializedName("cash_amount") val productsCost : String,
    @SerializedName("shipping_fees") val shippingFees : String?,
    @SerializedName("status") val orderStatus: OrderStatus

    )
class OrderStatus(
    @SerializedName("key")
    val id: Int,
    @SerializedName("value")
    val value:String
)
class  PaymentMethod(
    @SerializedName("key")
    val id: Int,
    @SerializedName("value")
    val value:String
)
class OrderResponse(

    @SerializedName("status") var status : Int,
    @SerializedName("status_code") var  status_code : Int,
    @SerializedName("message") var message : String,
    @SerializedName("errors")
    var error:ArrayList<String>,
    @SerializedName("data")
    var orders:ArrayList<Order>
)